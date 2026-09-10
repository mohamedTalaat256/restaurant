import { Component, Input } from '@angular/core';
import { RippleModule } from 'primeng/ripple';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { ItemFood } from '../../../../core/model/item-food.model';

@Component({
    standalone: true,
    selector: 'app-recent-sales-widget',
    imports: [CommonModule, TableModule, ButtonModule, RippleModule],
    template: `<div class="card mb-8!">
        <div class="font-semibold text-xl mb-4">Recent Sales</div>
        <p-table [value]="topSellingItems" [paginator]="true" [rows]="5" responsiveLayout="scroll">
            <ng-template #header>
                <tr>
                    <th pSortableColumn="itemName">Name <p-sortIcon field="itemName"></p-sortIcon></th>
                    <th pSortableColumn="quantity">Quantity <p-sortIcon field="quantity"></p-sortIcon></th>
                    <th pSortableColumn="totalSales">Total Sales <p-sortIcon field="totalSales"></p-sortIcon></th>
                    <th>View</th>
                </tr>
            </ng-template>
            <ng-template #body let-item>
                <tr>
                    <td style="width: 40%; min-width: 7rem;">{{ item.itemName }}</td>
                    <td style="width: 25%; min-width: 7rem;">{{ item.quantity }}</td>
                    <td style="width: 20%; min-width: 8rem;">{{ item.totalSales | currency: 'USD' }}</td>
                    <td style="width: 15%;">
                        <button pButton pRipple type="button" icon="pi pi-search" class="p-button p-component p-button-text p-button-icon-only"></button>
                    </td>
                </tr>
            </ng-template>
        </p-table>
    </div>`,
})
export class RecentSalesWidget {
  @Input() topSellingItems: ItemFood[] = [];
}
