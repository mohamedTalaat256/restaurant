import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, OnInit, ViewChild } from '@angular/core';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { ProgressBarModule } from 'primeng/progressbar';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TooltipModule } from 'primeng/tooltip';
import { PurchaseService } from './purchase.service';
import { Purchase } from '../../../../core/model/purchase.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { Toast } from 'primeng/toast';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';
import { Router } from '@angular/router';

@Component({
  selector: 'app-purchases',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule, TooltipModule,
    Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './purchases.html',
  styleUrls: ['./purchases.scss'],
  providers: [MessageService, ConfirmationService, PurchaseService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Purchases implements OnInit {

  selectedPurchases!: Purchase[] | null;
  @ViewChild('dt') dt!: Table;

  purchaseService = inject(PurchaseService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private router = inject(Router);

  menuItemId: number = env.menuItems.find(item => item.name === 'purchases')?.id || 0;

  getTotalAmount(): number {
    return this.purchaseService.purchases().reduce((sum, p) => sum + (p.totalAmount || 0), 0);
  }

  getTotalPaid(): number {
    return this.purchaseService.purchases().reduce((sum, p) => sum + (p.paidAmount || 0), 0);
  }

  ngOnInit(): void {
    this.purchaseService.loadPurchases();
  }



  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.router.navigate(['/admin/purchases/edit', 'new']);
  }

  editPurchase(purchase: Purchase) {
    this.router.navigate(['/admin/purchases/edit', purchase.id]);
  }

  deletePurchase(purchase: Purchase) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_purchase'),
      header: this.translate.instant('label_confrim'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.purchaseService.deletePurchase(purchase.id);
      }
    });
  }

}
