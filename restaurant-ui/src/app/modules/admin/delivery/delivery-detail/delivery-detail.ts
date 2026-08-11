import { Component, inject, OnInit, OnDestroy, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { FormsModule } from '@angular/forms';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { DeliveryService } from '../delivery.service';
import {
  Delivery,
  DeliveryStatus,
  DELIVERY_STATUS_SEVERITY,
  TERMINAL_DELIVERY_STATUSES,
} from '../../../../core/model/delivery.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { AssignDriverDialogComponent } from '../assign-driver-dialog/assign-driver-dialog';
import { ReassignDriverDialogComponent } from '../reassign-driver-dialog/reassign-driver-dialog';
import { DeliveryTrackingMapComponent } from '../delivery-tracking-map/delivery-tracking-map';

@Component({
  selector: 'app-delivery-detail',
  imports: [
    CommonModule,
    ButtonModule,
    TagModule,
    CardModule,
    DividerModule,
    DialogModule,
    InputTextModule,
    TextareaModule,
    FormsModule,
    ConfirmDialogModule,
    Toast,
    AssignDriverDialogComponent,
    ReassignDriverDialogComponent,
    DeliveryTrackingMapComponent,
  ],
  templateUrl: './delivery-detail.html',
  styleUrls: ['./delivery-detail.scss'],
  providers: [MessageService, ConfirmationService],
})
export class DeliveryDetail implements OnInit, OnDestroy {
  readonly deliveryService = inject(DeliveryService);
  readonly translate = inject(TranslateService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private confirmationService = inject(ConfirmationService);

  showAssignDialog = signal(false);
  showReassignDialog = signal(false);
  showCancelDialog = signal(false);
  cancelReason = '';

  deliveryId = 0;

  readonly statusTimeline: { status: DeliveryStatus; label: string; timestampKey: keyof Delivery }[] = [
    { status: 'CREATED', label: 'label_status_created', timestampKey: 'createdAt' },
    { status: 'ASSIGNED', label: 'label_status_assigned', timestampKey: 'assignedAt' },
    { status: 'ACCEPTED', label: 'label_status_accepted', timestampKey: 'acceptedAt' },
    { status: 'ARRIVED_AT_RESTAURANT', label: 'label_status_arrived', timestampKey: 'assignedAt' },
    { status: 'PICKED_UP', label: 'label_status_picked_up', timestampKey: 'pickedUpAt' },
    { status: 'ON_THE_WAY', label: 'label_status_on_the_way', timestampKey: 'pickedUpAt' },
    { status: 'DELIVERED', label: 'label_status_delivered', timestampKey: 'deliveredAt' },
  ];

  readonly statusOrder: DeliveryStatus[] = [
    'CREATED',
    'WAITING_ASSIGNMENT',
    'ASSIGNED',
    'ACCEPTED',
    'ARRIVED_AT_RESTAURANT',
    'PICKED_UP',
    'ON_THE_WAY',
    'DELIVERED',
    'FAILED',
    'CANCELLED',
  ];

  ngOnInit() {
    this.deliveryId = Number(this.route.snapshot.paramMap.get('id'));
    this.deliveryService.loadDeliveryById(this.deliveryId);
    this.deliveryService.loadTrackingPoints(this.deliveryId);
  }

  ngOnDestroy() {
    this.deliveryService.delivery.set(null);
  }

  get delivery(): Delivery | null {
    return this.deliveryService.delivery();
  }

  getStatusSeverity(status: DeliveryStatus) {
    return DELIVERY_STATUS_SEVERITY[status] ?? 'secondary';
  }

  isTerminal(status: DeliveryStatus): boolean {
    return TERMINAL_DELIVERY_STATUSES.includes(status);
  }

  isStatusReached(status: DeliveryStatus): boolean {
    const current = this.delivery?.status;
    if (!current) return false;
    return this.statusOrder.indexOf(current) >= this.statusOrder.indexOf(status);
  }

  canAssign(): boolean {
    return this.delivery?.status === 'WAITING_ASSIGNMENT' || this.delivery?.status === 'CREATED';
  }

  canReassign(): boolean {
    const s = this.delivery?.status;
    return (
      !!this.delivery?.driver &&
      s !== undefined &&
      !this.isTerminal(s) &&
      (s === 'ASSIGNED' || s === 'ACCEPTED' || s === 'ARRIVED_AT_RESTAURANT')
    );
  }

  canComplete(): boolean {
    return this.delivery?.status === 'ON_THE_WAY';
  }

  canCancel(): boolean {
    return !!this.delivery && !this.isTerminal(this.delivery.status);
  }

  showTrackingMap(): boolean {
    const s = this.delivery?.status;
    return s === 'ON_THE_WAY' || s === 'PICKED_UP' || s === 'ARRIVED_AT_RESTAURANT';
  }

  completeDelivery() {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_complete_delivery'),
      accept: () => {
        this.deliveryService.updateStatus(this.deliveryId, 'complete');
      },
    });
  }

  onCancelSubmit() {
    if (!this.cancelReason.trim()) return;
    this.deliveryService.cancelDelivery(
      this.deliveryId,
      { reason: this.cancelReason },
      () => {
        this.showCancelDialog.set(false);
        this.cancelReason = '';
      },
    );
  }

  onDriverAssigned() {
    this.showAssignDialog.set(false);
    this.deliveryService.loadDeliveryById(this.deliveryId);
  }

  onDriverReassigned() {
    this.showReassignDialog.set(false);
    this.deliveryService.loadDeliveryById(this.deliveryId);
  }

  goBack() {
    this.router.navigate(['/admin/deliveries']);
  }
}
