import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, inject, Input, OnChanges, Output } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { env } from '../../../../../environment/env';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { ApplicationSetting, DEFAULT_APPLICATION_SETTING } from '../../../../core/model/application-setting.model';
import { Order } from '../../../../core/model/order.model';
import { TranslateService } from '../../../../core/service/translate.service';

@Component({
  selector: 'app-pos-print-dialog',
  imports: [CommonModule, DialogModule, ButtonModule],
  template: `
    <p-dialog
      [header]="translate.instant('label_pos_invoice')"
      [visible]="visible"
      (visibleChange)="visibleChange.emit($event)"
      [modal]="true"
      [style]="{ width: '560px' }"
      [closable]="true"
      [maximizable]="true"
    >
      <ng-template #content>
        @if (loading) {
          <div class="d-flex justify-content-center p-4">
            <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
          </div>
        } @else if (order) {
          <div id="pos-invoice-print-area" class="pos-receipt">
            <div class="receipt-header">
              @if (logoUrl) {
                <img [src]="logoUrl" alt="Logo" class="receipt-logo" />
              }
              <h4 class="m-0">{{ restaurantName }}</h4>
              @if (applicationSettings.address) {
                <div class="text-muted small">{{ applicationSettings.address }}</div>
              }
              @if (applicationSettings.phone) {
                <div class="text-muted small">{{ translate.instant('label_phone') }}: {{ applicationSettings.phone }}</div>
              }
              @if (applicationSettings.taxNumber) {
                <div class="text-muted small">{{ translate.instant('label_tax_number') }}: {{ applicationSettings.taxNumber }}</div>
              }
            </div>

            <div class="receipt-divider"></div>

            <div class="receipt-meta">
              <div><strong>{{ translate.instant('label_order_number') }}:</strong> {{ order.orderNumber }}</div>
              <div><strong>{{ translate.instant('label_order_type') }}:</strong> {{ translate.instant(order.orderType) }}</div>
              <div><strong>{{ translate.instant('label_status') }}:</strong> {{ translate.instant(order.status) }}</div>
              <div><strong>{{ translate.instant('label_created_at') }}:</strong> {{ order.createdAt | date: 'short' }}</div>
              <div><strong>{{ translate.instant('label_table') }}:</strong> {{ order.tableName || '-' }}</div>
              <div><strong>{{ translate.instant('label_waiter') }}:</strong> {{ order.waiterName || '-' }}</div>
              <div><strong>{{ translate.instant('label_customer') }}:</strong> {{ order.customerName || '-' }}</div>
            </div>

            <div class="receipt-divider"></div>

            <table class="receipt-table">
              <thead>
                <tr>
                  <th>{{ translate.instant('label_item_food') }}</th>
                  <th class="text-center">{{ translate.instant('label_quantity') }}</th>
                  <th class="text-end">{{ translate.instant('label_total') }}</th>
                </tr>
              </thead>
              <tbody>
                @for (item of order.orderItems; track item.id) {
                  <tr>
                    <td>
                      <div>{{ item.itemFoodName }}</div>
                      @if (item.variantName) {
                        <small class="text-muted">{{ translate.instant('label_variant') }}: {{ item.variantName }}</small>
                      }
                      @if (item.orderItemAddOns.length) {
                        <small class="text-muted d-block">
                          {{ translate.instant('label_add_ons') }}:
                          {{ item.orderItemAddOns.map(addon => addon.addOnName).join(', ') }}
                        </small>
                      }
                    </td>
                    <td class="text-center">{{ item.quantity }}</td>
                    <td class="text-end">{{ formatAmount(item.totalPrice) }}</td>
                  </tr>
                }
              </tbody>
            </table>

            <div class="receipt-divider"></div>

            <div class="receipt-total-row">
              <span class="f-w-bold">{{ translate.instant('label_total') }}</span>
              <span class="f-w-bold">{{ formatAmount(order.totalAmount) }}</span>
            </div>

            @if (applicationSettings.footerText) {
              <div class="receipt-footer">{{ applicationSettings.footerText }}</div>
            }
          </div>
        } @else {
          <div class="text-center p-4 text-muted">No invoice data available.</div>
        }
      </ng-template>

      <ng-template #footer>
        <p-button
          [label]="translate.instant('label_cancel')"
          icon="pi pi-times"
          [text]="true"
          (click)="visibleChange.emit(false)"
        />
        <p-button
          [label]="translate.instant('label_print_pos')"
          icon="pi pi-print"
          severity="success"
          [disabled]="!order"
          (click)="printInvoice()"
        />
      </ng-template>
    </p-dialog>
  `,
  styles: [`
    .pos-receipt {
      max-width: 100%;
      font-size: 0.92rem;
      color: #1f2937;
    }

    .receipt-header {
      text-align: center;
      margin-bottom: 0.5rem;
    }

    .receipt-logo {
      width: 64px;
      height: 64px;
      object-fit: contain;
      margin-bottom: 0.5rem;
    }

    .receipt-divider {
      border-top: 1px dashed #d1d5db;
      margin: 0.75rem 0;
    }

    .receipt-meta {
      display: grid;
      gap: 0.3rem;
      font-size: 0.88rem;
    }

    .receipt-table {
      width: 100%;
      border-collapse: collapse;
      font-size: 0.88rem;
    }

    .receipt-table th,
    .receipt-table td {
      padding: 0.4rem 0.25rem;
      vertical-align: top;
      border-bottom: 1px solid #f3f4f6;
    }

    .receipt-total-row {
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 1rem;
    }

    .receipt-footer {
      margin-top: 1rem;
      text-align: center;
      color: #6b7280;
      font-size: 0.8rem;
    }
  `],
})
export class PosPrintDialogComponent implements OnChanges {
  @Input() visible = false;
  @Input() orderId: number | null = null;
  @Output() visibleChange = new EventEmitter<boolean>();

  readonly translate = inject(TranslateService);
  private readonly http = inject(HttpClient);

  loading = false;
  order: Order | null = null;
  applicationSettings: ApplicationSetting = { ...DEFAULT_APPLICATION_SETTING };
  imageUrl = env.baseUrl;

  get restaurantName(): string {
    return this.applicationSettings.storeName || this.applicationSettings.applicationTitle || 'Restaurant';
  }

  get logoUrl(): string {
    const logo = this.applicationSettings.logo;
    if (!logo) return '';
    if (logo.startsWith('http://') || logo.startsWith('https://')) return logo;
    return `${this.imageUrl}${logo}`;
  }

  ngOnChanges() {
    if (!this.visible || !this.orderId) return;
    this.loadApplicationSettings();
    this.loadOrderDetails(this.orderId);
  }

  private loadApplicationSettings() {
    try {
      const stored = JSON.parse(localStorage.getItem('applicationSettings') || '{}');
      this.applicationSettings = { ...DEFAULT_APPLICATION_SETTING, ...stored };
    } catch {
      this.applicationSettings = { ...DEFAULT_APPLICATION_SETTING };
    }
  }

  private loadOrderDetails(id: number) {
    this.loading = true;
    this.http.get<ApiResponse<Order>>(`${env.apiUrl}/orders/${id}`).subscribe({
      next: (res) => {
        this.order = res.data;
        this.loading = false;
      },
      error: () => {
        this.order = null;
        this.loading = false;
      }
    });
  }

  formatAmount(value: number): string {
    const symbol = this.applicationSettings.currencySymbol || this.applicationSettings.currency?.symbol || '';
    return `${Number(value ?? 0).toFixed(2)} ${symbol}`.trim();
  }

  private get printDirection(): 'rtl' | 'ltr' {
    const appDirection = (this.applicationSettings.applicationDirection || '').toLowerCase();
    return appDirection === 'rtl' ? 'rtl' : 'ltr';
  }

  printInvoice() {
    const content = document.getElementById('pos-invoice-print-area')?.innerHTML;
    if (!content) return;

    const printWindow = window.open('', '_blank', 'width=420,height=720');
    if (!printWindow) return;

    const direction = this.printDirection;
    const baseTextAlign = direction === 'rtl' ? 'right' : 'left';

    printWindow.document.write(`
      <html dir="${direction}">
        <head>
          <title>${this.translate.instant('label_pos_invoice')}</title>
          <style>
            body { font-family: Arial, sans-serif; margin: 12px; color: #111827; direction: ${direction}; text-align: ${baseTextAlign}; }
            .pos-receipt { font-size: 12px; }
            .receipt-header { text-align: center; }
            .receipt-logo { width: 60px; height: 60px; object-fit: contain; margin-bottom: 6px; }
            .receipt-divider { border-top: 1px dashed #9ca3af; margin: 10px 0; }
            .receipt-meta { display: grid; gap: 4px; font-size: 11px; }
            .receipt-table { width: 100%; border-collapse: collapse; font-size: 11px; }
            .receipt-table th, .receipt-table td { padding: 4px 2px; border-bottom: 1px solid #e5e7eb; }
            .receipt-total-row { display: flex; justify-content: space-between; font-size: 13px; font-weight: 700; }
            .receipt-footer { margin-top: 12px; text-align: center; font-size: 10px; color: #6b7280; }
            .text-muted { color: #6b7280; }
            .text-center { text-align: center; }
            .text-end { text-align: right; }
            .d-block { display: block; }
          </style>
        </head>
        <body>${content}</body>
      </html>
    `);
    printWindow.document.close();
    printWindow.focus();
    setTimeout(() => {
      printWindow.print();
      printWindow.close();
    }, 200);
  }
}
