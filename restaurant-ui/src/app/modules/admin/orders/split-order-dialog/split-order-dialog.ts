import { Component, EventEmitter, inject, Input, OnChanges, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputNumberModule } from 'primeng/inputnumber';
import { OrderService } from '../orders/order.service';
import { TranslateService } from '../../../../core/service/translate.service';
import { Order, OrderItem } from '../../../../core/model/order.model';

interface SplitGroup {
  items: { orderItemId: number; itemFoodName: string; maxQty: number; quantity: number }[];
}

@Component({
  selector: 'app-split-order-dialog',
  imports: [CommonModule, DialogModule, ButtonModule, InputNumberModule, FormsModule],
  template: `
    <p-dialog
      [header]="translate.instant('label_split_order')"
      [visible]="visible"
      (visibleChange)="visibleChange.emit($event)"
      [modal]="true"
      [style]="{ width: '700px' }"
      [closable]="true"
    >
      <ng-template #content>
        <p class="text-muted mb-3">{{ translate.instant('msg_split_order_hint') }}</p>

        <div class="row">
          @for (group of groups; track $index; let gi = $index) {
            <div class="col-md-6 mb-3">
              <div class="border rounded p-3">
                <div class="d-flex align-items-center justify-content-between mb-2">
                  <strong>{{ translate.instant('label_group') }} {{ gi + 1 }}</strong>
                  @if (groups.length > 2) {
                    <p-button icon="pi pi-times" [text]="true" severity="danger" size="small"
                      (click)="removeGroup(gi)" />
                  }
                </div>
                @for (item of group.items; track item.orderItemId) {
                  <div class="d-flex align-items-center justify-content-between mb-2">
                    <span class="me-2" style="flex:1">{{ item.itemFoodName }}</span>
                    <p-inputnumber
                      [(ngModel)]="item.quantity"
                      [min]="0"
                      [max]="item.maxQty"
                      [showButtons]="true"
                      buttonLayout="horizontal"
                      decrementButtonClass="p-button-danger"
                      incrementButtonClass="p-button-success"
                      [inputStyle]="{ width: '50px' }"
                    />
                  </div>
                }
              </div>
            </div>
          }
        </div>

        <p-button
          [label]="translate.instant('label_add_group')"
          icon="pi pi-plus"
          severity="secondary"
          [outlined]="true"
          (click)="addGroup()"
          class="mt-2"
        />
      </ng-template>
      <ng-template #footer>
        <p-button [label]="translate.instant('label_cancel')" icon="pi pi-times" [text]="true" (click)="visibleChange.emit(false)" />
        <p-button
          [label]="translate.instant('label_split')"
          icon="pi pi-share-alt"
          severity="warn"
          [loading]="orderService.loadingSave()"
          (click)="submit()"
        />
      </ng-template>
    </p-dialog>
  `,
})
export class SplitOrderDialogComponent implements OnChanges {
  @Input() visible = false;
  @Input() order!: Order;
  @Output() visibleChange = new EventEmitter<boolean>();
  @Output() splitDone = new EventEmitter<void>();

  readonly orderService = inject(OrderService);
  readonly translate = inject(TranslateService);

  groups: SplitGroup[] = [];

  ngOnChanges() {
    if (this.visible && this.order) {
      this.initGroups();
    }
  }

  private initGroups() {
    const makeGroup = (): SplitGroup => ({
      items: this.order.orderItems.map(item => ({
        orderItemId: item.id,
        itemFoodName: item.itemFoodName + (item.variantName ? ` (${item.variantName})` : ''),
        maxQty: item.quantity,
        quantity: 0,
      })),
    });

    const g1 = makeGroup();
    const g2 = makeGroup();

    // Distribute all items to group 1 by default
    g1.items.forEach((item, i) => {
      item.quantity = this.order.orderItems[i].quantity;
    });

    this.groups = [g1, g2];
  }

  addGroup() {
    this.groups.push({
      items: this.order.orderItems.map(item => ({
        orderItemId: item.id,
        itemFoodName: item.itemFoodName + (item.variantName ? ` (${item.variantName})` : ''),
        maxQty: item.quantity,
        quantity: 0,
      })),
    });
  }

  removeGroup(index: number) {
    this.groups.splice(index, 1);
  }

  submit() {
    const splitGroups = this.groups.map(g =>
      g.items
        .filter(item => item.quantity > 0)
        .map(item => ({ orderItemId: item.orderItemId, quantity: item.quantity }))
    ).filter(g => g.length > 0);

    if (splitGroups.length < 2) return;

    this.orderService.splitOrder(this.order.id, { splitGroups }, () => {
      this.visibleChange.emit(false);
      this.splitDone.emit();
    });
  }
}
