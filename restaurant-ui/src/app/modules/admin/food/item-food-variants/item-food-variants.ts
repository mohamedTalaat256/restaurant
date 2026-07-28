import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, effect, inject, input, OnInit, output, ViewChild } from '@angular/core';
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
import { ItemFoodVariantService } from './item-food-variant.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { ItemFoodVariant } from '../../../../core/model/item-food-variant.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';
import { ItemFood } from '../../../../core/model/item-food.model';

@Component({
  selector: 'app-item-food-variants',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './item-food-variants.html',
  styleUrls: ['./item-food-variants.scss'],
  providers: [MessageService, ConfirmationService, ItemFoodVariantService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class ItemFoodVariants implements OnInit {

  @ViewChild('dt') dt!: Table;
  selectedItemFood = input<ItemFood | null>(null);
  itemFoodVariantForm!: FormGroup;

  itemFoodVariantService = inject(ItemFoodVariantService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  menuItemId: number = env.menuItems.find(item => item.name === 'item_food_variants')?.id || 0;


  constructor() {
    effect(() => {
      this.itemFoodVariantService.itemFoodVariants.set( this.selectedItemFood()?.variants ?? []);
    });
  }

  ngOnInit(): void {

  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.itemFoodVariantService.itemFoodVariantDialog.set(true);
  }

  editItemFoodVariant(itemFoodVariant: ItemFoodVariant) {
    this.itemFoodVariantService.itemFoodVariantDialog.set(true);
    this.setForm(itemFoodVariant);
  }

  hideDialog() {
    this.itemFoodVariantService.itemFoodVariantDialog.set(false);
  }

  deleteItemFoodVariant(itemFoodVariant: ItemFoodVariant) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_item_food_variant'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.itemFoodVariantService.deleteItemFoodVariant(itemFoodVariant.id);
      }
    });
  }

  saveItemFoodVariant() {
    if (this.itemFoodVariantForm.invalid) {
      this.itemFoodVariantForm.markAllAsTouched();
      return;
    }

    if (this.itemFoodVariantForm.value.id) {
      this.itemFoodVariantService.updateItemFoodVariant(this.itemFoodVariantForm.value);
    } else {
      this.itemFoodVariantService.saveItemFoodVariant(this.itemFoodVariantForm.value);
    }
  }

  initiatForm() {
    this.itemFoodVariantForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      price: [null, Validators.required],
      itemFoodId: [this.selectedItemFood()?.id, Validators.required],
    });
  }

  setForm(itemFoodVariant: ItemFoodVariant) {
    this.itemFoodVariantForm = this.fb.group({
      id: [itemFoodVariant.id],
      name: [itemFoodVariant.name, Validators.required],
      price: [itemFoodVariant.price, Validators.required],
      itemFoodId: [this.selectedItemFood()?.id, Validators.required],
    });
  }
}
