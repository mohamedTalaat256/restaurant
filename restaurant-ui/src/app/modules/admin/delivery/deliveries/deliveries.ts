import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { SelectModule } from 'primeng/select';
import { DialogModule } from 'primeng/dialog';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { InputNumberModule } from 'primeng/inputnumber';
import { DatePickerModule } from 'primeng/datepicker';
import { TooltipModule } from 'primeng/tooltip';
import { TextareaModule } from 'primeng/textarea';
import { DeliveryService } from '../delivery.service';
import { Delivery, DeliveryStatus, DELIVERY_STATUS_SEVERITY } from '../../../../core/model/delivery.model';
import { TranslateService } from '../../../../core/service/translate.service';

@Component({
  selector: 'app-deliveries',
  imports: [
    CommonModule,
    TableModule,
    ButtonModule,
    ToolbarModule,
    InputTextModule,
    TagModule,
    InputIconModule,
    IconFieldModule,
    SelectModule,
    DialogModule,
    FormsModule,
    ReactiveFormsModule,
    Toast,
    InputNumberModule,
    DatePickerModule,
    TooltipModule,
    TextareaModule,
  ],
  templateUrl: './deliveries.html',
  styleUrls: ['./deliveries.scss'],
  providers: [MessageService],
})
export class Deliveries implements OnInit {
  @ViewChild('dt') dt!: Table;

  readonly deliveryService = inject(DeliveryService);
  readonly translate = inject(TranslateService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  selectedStatus: DeliveryStatus | null = null;
  showCreateDialog = signal(false);

  statusOptions = [
    { label: this.translate.instant('label_all'), value: null },
    ...([
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
    ] as DeliveryStatus[]).map((s) => ({ label: s, value: s })),
  ];

  createForm = this.fb.group({
    orderId: [null as number | null, [Validators.required, Validators.min(1)]],
    deliveryFee: [null as number | null],
    estimatedDeliveryTime: [null as Date | null],
    notes: [''],
  });

  ngOnInit() {
    this.loadDeliveries();
  }

  loadDeliveries() {
    this.deliveryService.loadDeliveries(this.selectedStatus ?? undefined);
  }

  onStatusChange() {
    this.loadDeliveries();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openCreateDialog() {
    this.createForm.reset();
    this.deliveryService.savedSuccess.set(false);
    this.showCreateDialog.set(true);
  }

  submitCreate() {
    if (this.createForm.invalid) return;
    const val = this.createForm.value;
    this.deliveryService.createDelivery(
      {
        orderId: val.orderId!,
        deliveryFee: val.deliveryFee ?? undefined,
        estimatedDeliveryTime: val.estimatedDeliveryTime
          ? (val.estimatedDeliveryTime as Date).toISOString()
          : undefined,
        notes: val.notes || undefined,
      },
      () => {
        this.showCreateDialog.set(false);
      },
    );
  }

  goToDetail(delivery: Delivery) {
    this.router.navigate(['/admin/deliveries', delivery.id]);
  }

  getStatusSeverity(status: DeliveryStatus) {
    return DELIVERY_STATUS_SEVERITY[status] ?? 'secondary';
  }
}
