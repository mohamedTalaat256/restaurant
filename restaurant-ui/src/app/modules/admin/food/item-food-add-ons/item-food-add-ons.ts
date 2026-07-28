import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { ProgressBarModule } from 'primeng/progressbar';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Toast } from "primeng/toast";
import { ItemFoodAddOnsService } from './item-food-add-ons.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { ItemFoodAddOns } from '../../../../core/model/item-food-add-ons.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';

@Component({
  selector: 'app-item-food-add-ons',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './item-food-add-ons.html',
  styleUrls: ['./item-food-add-ons.scss'],
  providers: [MessageService, ConfirmationService, ItemFoodAddOnsService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class ItemFoodAddOnsComponent implements OnInit {

  submitted: boolean = false;
  @ViewChild('dt') dt!: Table;

  itemFoodAddOnsForm!: FormGroup;

  itemFoodAddOnsService = inject(ItemFoodAddOnsService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'item_food_add_ons')?.id || 0;

  ngOnInit(): void {
    this.itemFoodAddOnsService.loadItemFoodAddOns();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.itemFoodAddOnsService.itemFoodAddOnsDialog.set(true);
  }

  editItemFoodAddOns(itemFoodAddOns: ItemFoodAddOns) {
    this.itemFoodAddOnsService.itemFoodAddOnsDialog.set(true);
    this.setForm(itemFoodAddOns);
  }

  hideDialog() {
    this.itemFoodAddOnsService.itemFoodAddOnsDialog.set(false);
  }

  deleteItemFoodAddOns(itemFoodAddOns: ItemFoodAddOns) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_item_food_add_ons'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.itemFoodAddOnsService.deleteItemFoodAddOns(itemFoodAddOns.id);
      }
    });
  }

  saveItemFoodAddOns() {
    if (this.itemFoodAddOnsForm.invalid) {
      this.itemFoodAddOnsForm.markAllAsTouched();
      return;
    }

    if (this.itemFoodAddOnsForm.value.id) {
      this.itemFoodAddOnsService.updateItemFoodAddOns(this.itemFoodAddOnsForm.value);
    } else {
      this.itemFoodAddOnsService.saveItemFoodAddOns(this.itemFoodAddOnsForm.value);
    }
  }

  initiatForm() {
    this.itemFoodAddOnsForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      price: [null, Validators.required],
      status: [true],
    });
  }

  setForm(itemFoodAddOns: ItemFoodAddOns) {
    this.itemFoodAddOnsForm = this.fb.group({
      id: [itemFoodAddOns.id],
      name: [itemFoodAddOns.name, Validators.required],
      price: [itemFoodAddOns.price, Validators.required],
      status: [itemFoodAddOns.status],
    });
  }
}
