import {
  Component,
  inject,
  Input,
  OnInit,
  OnChanges,
  OnDestroy,
  SimpleChanges,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { TableModule } from 'primeng/table';
import { DeliveryService } from '../delivery.service';
import { DeliveryStatus } from '../../../../core/model/delivery.model';
import { TranslateService } from '../../../../core/service/translate.service';

@Component({
  selector: 'app-delivery-tracking-map',
  imports: [CommonModule, CardModule, ButtonModule, TagModule, TooltipModule, TableModule],
  templateUrl: './delivery-tracking-map.html',
  styleUrls: ['./delivery-tracking-map.scss'],
})
export class DeliveryTrackingMapComponent implements OnInit, OnChanges, OnDestroy {
  @Input() deliveryId = 0;
  @Input() deliveryStatus: DeliveryStatus = 'ON_THE_WAY';

  readonly deliveryService = inject(DeliveryService);
  readonly translate = inject(TranslateService);
  private sanitizer = inject(DomSanitizer);

  private pollingInterval: ReturnType<typeof setInterval> | null = null;
  mapUrl: SafeResourceUrl | null = null;

  ngOnInit() {
    this.deliveryService.loadTrackingPoints(this.deliveryId);
    this.deliveryService.loadLatestPoint(this.deliveryId);
    this.updateMapUrl();
    if (this.deliveryStatus === 'ON_THE_WAY') {
      this.startPolling();
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['deliveryStatus']) {
      if (this.deliveryStatus === 'ON_THE_WAY') {
        this.startPolling();
      } else {
        this.stopPolling();
      }
    }
    if (changes['deliveryId']) {
      this.deliveryService.loadTrackingPoints(this.deliveryId);
      this.deliveryService.loadLatestPoint(this.deliveryId);
      this.updateMapUrl();
    }
  }

  ngOnDestroy() {
    this.stopPolling();
  }

  private startPolling() {
    this.stopPolling();
    this.pollingInterval = setInterval(() => {
      this.deliveryService.loadLatestPoint(this.deliveryId);
      this.deliveryService.loadTrackingPoints(this.deliveryId);
      this.updateMapUrl();
    }, 10_000);
  }

  private stopPolling() {
    if (this.pollingInterval) {
      clearInterval(this.pollingInterval);
      this.pollingInterval = null;
    }
  }

  private updateMapUrl() {
    const pos = this.deliveryService.latestPoint();
    if (!pos) {
      this.mapUrl = null;
      return;
    }
    const delta = 0.01;
    const west = (pos.longitude - delta).toFixed(6);
    const south = (pos.latitude - delta).toFixed(6);
    const east = (pos.longitude + delta).toFixed(6);
    const north = (pos.latitude + delta).toFixed(6);
    const url =
      `https://www.openstreetmap.org/export/embed.html` +
      `?bbox=${west},${south},${east},${north}` +
      `&layer=mapnik&marker=${pos.latitude},${pos.longitude}`;
    this.mapUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  refreshMap() {
    this.deliveryService.loadLatestPoint(this.deliveryId);
    this.deliveryService.loadTrackingPoints(this.deliveryId);
    this.updateMapUrl();
  }

  get latest() {
    return this.deliveryService.latestPoint();
  }

  get hasTracking(): boolean {
    return (
      this.deliveryService.trackingPoints().length > 0 || !!this.deliveryService.latestPoint()
    );
  }

  openInGoogleMaps() {
    const pos = this.deliveryService.latestPoint();
    if (!pos) return;
    window.open(
      `https://www.google.com/maps?q=${pos.latitude},${pos.longitude}`,
      '_blank',
      'noopener,noreferrer',
    );
  }
}
