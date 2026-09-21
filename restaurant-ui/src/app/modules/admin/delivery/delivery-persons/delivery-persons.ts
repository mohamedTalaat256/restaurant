import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { ProgressBarModule } from 'primeng/progressbar';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToggleSwitchModule } from 'primeng/toggleswitch';
import { SelectButtonModule } from 'primeng/selectbutton';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { DeliveryPersonService } from './delivery-person.service';
import { DeliveryDetails, DELIVERY_VEHICLE_TYPE_OPTIONS } from '../../../../core/model/delivery-details.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';
import { DeliveryPersonFormDialog } from './delivery-person-form-dialog/delivery-person-form-dialog';

@Component({
  selector: 'app-delivery-persons',
  imports: [CommonModule, FormsModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule,
    TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule, ToggleSwitchModule, SelectButtonModule,
    Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective, DeliveryPersonFormDialog],
  templateUrl: './delivery-persons.html',
  styleUrls: ['./delivery-persons.scss'],
  providers: [MessageService, ConfirmationService, DeliveryPersonService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DeliveryPersons implements OnInit {

  selectedDeliveries!: DeliveryDetails[] | null;
  @ViewChild('dt') dt!: Table;

  deliveryDialogVisible = false;
  editingDelivery: DeliveryDetails | null = null;

  statusFilter: boolean | null = null;
  statusFilterOptions = [
    { label: 'label_all', value: null },
    { label: 'label_active', value: true },
    { label: 'label_inactive', value: false },
  ];

  readonly vehicleTypeOptions = DELIVERY_VEHICLE_TYPE_OPTIONS;

  deliveryService = inject(DeliveryPersonService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);

  menuItemId: number = env.menuItems.find(item => item.name === 'deliveries')?.id || 0;

  ngOnInit(): void {
    this.deliveryService.loadDeliveries(this.statusFilter);
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  onStatusFilterChange() {
    this.deliveryService.loadDeliveries(this.statusFilter);
  }

  vehicleLabel(value?: string): string {
    const option = this.vehicleTypeOptions.find(o => o.value === value);
    return option ? this.translate.instant(option.label) : '';
  }

  openNew() {
    this.editingDelivery = null;
    this.deliveryDialogVisible = true;
  }

  editDelivery(delivery: DeliveryDetails) {
    this.editingDelivery = delivery;
    this.deliveryDialogVisible = true;
  }

  onDialogVisibleChange(visible: boolean) {
    this.deliveryDialogVisible = visible;
    if (!visible) {
      this.editingDelivery = null;
    }
  }

  toggleStatus(delivery: DeliveryDetails, status: boolean) {
    this.deliveryService.updateStatus(delivery.id, status);
  }

  deleteDelivery(delivery: DeliveryDetails) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_delivery'),
      header: this.translate.instant('label_confrim'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.deliveryService.deleteDelivery(delivery.id);
      }
    });
  }

  onDeliverySaved() {
    this.deliveryService.loadDeliveries(this.statusFilter);
  }
}
