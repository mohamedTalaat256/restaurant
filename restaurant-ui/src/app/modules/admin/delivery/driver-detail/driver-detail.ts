import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { TableModule } from 'primeng/table';
import { SelectModule } from 'primeng/select';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { DriverService } from '../driver.service';
import { DriverStatus, DRIVER_STATUS_SEVERITY } from '../../../../core/model/driver.model';
import { DELIVERY_STATUS_SEVERITY, DeliveryStatus } from '../../../../core/model/delivery.model';
import { TranslateService } from '../../../../core/service/translate.service';

@Component({
  selector: 'app-driver-detail',
  imports: [
    CommonModule,
    ButtonModule,
    TagModule,
    CardModule,
    DividerModule,
    TableModule,
    SelectModule,
    FormsModule,
    Toast,
  ],
  templateUrl: './driver-detail.html',
  styleUrls: ['./driver-detail.scss'],
  providers: [MessageService],
})
export class DriverDetail implements OnInit, OnDestroy {
  readonly driverService = inject(DriverService);
  readonly translate = inject(TranslateService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  driverId = 0;

  readonly statusOptions: { label: string; value: DriverStatus }[] = [
    { label: 'ONLINE', value: 'ONLINE' },
    { label: 'OFFLINE', value: 'OFFLINE' },
    { label: 'BUSY', value: 'BUSY' },
    { label: 'BREAK', value: 'BREAK' },
  ];

  ngOnInit() {
    this.driverId = Number(this.route.snapshot.paramMap.get('id'));
    this.driverService.loadDriverById(this.driverId);
    this.driverService.loadCurrentDelivery(this.driverId);
    this.driverService.loadDeliveryHistory(this.driverId);
  }

  ngOnDestroy() {
    this.driverService.driver.set(null);
    this.driverService.currentDelivery.set(null);
    this.driverService.deliveryHistory.set([]);
  }

  getDriverStatusSeverity(status: DriverStatus) {
    return DRIVER_STATUS_SEVERITY[status] ?? 'secondary';
  }

  getDeliveryStatusSeverity(status: DeliveryStatus) {
    return DELIVERY_STATUS_SEVERITY[status] ?? 'secondary';
  }

  onChangeStatus(newStatus: DriverStatus) {
    this.driverService.changeStatus(this.driverId, { status: newStatus });
  }

  goToDelivery(deliveryId: number) {
    this.router.navigate(['/admin/deliveries', deliveryId]);
  }

  goBack() {
    this.router.navigate(['/admin/drivers']);
  }
}
