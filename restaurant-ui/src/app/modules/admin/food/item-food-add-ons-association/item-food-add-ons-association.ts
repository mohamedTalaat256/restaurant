import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, effect, inject, input, OnInit, ViewChild } from '@angular/core';
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
import { ItemFoodAddOnsAssociationService } from './item-food-add-ons-association.service';
import { ItemFoodAddOnsService } from '../item-food-add-ons/item-food-add-ons.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { ItemFoodAddOnsAssociation } from '../../../../core/model/item-food-add-ons-association.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';
import { ItemFood } from '../../../../core/model/item-food.model';

@Component({
  selector: 'app-item-food-add-ons-association',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './item-food-add-ons-association.html',
  styleUrls: ['./item-food-add-ons-association.scss'],
  providers: [MessageService, ConfirmationService, ItemFoodAddOnsAssociationService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class ItemFoodAddOnsAssociationComponent implements OnInit {

  @ViewChild('dt') dt!: Table;
  selectedItemFood = input<ItemFood | null>(null);
  itemFoodAddOnsAssociationForm!: FormGroup;

  itemFoodAddOnsAssociationService = inject(ItemFoodAddOnsAssociationService);
  itemFoodAddOnsService = inject(ItemFoodAddOnsService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  menuItemId: number = env.menuItems.find(item => item.name === 'item_food_add_ons_association')?.id || 0;

  constructor() {
    effect(() => {
      this.itemFoodAddOnsAssociationService.itemFoodAddOnsAssociations.set( this.selectedItemFood()?.addOnsAssociations ?? []);
    });
  }

  itemFoodAddOnsOptions = computed(() => {
    return this.itemFoodAddOnsService.itemFoodAddOns().map(addOn => ({ label: addOn.name, value: addOn.id }));
  });

  ngOnInit(): void {
    this.itemFoodAddOnsService.loadItemFoodAddOns();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.itemFoodAddOnsAssociationService.itemFoodAddOnsAssociationDialog.set(true);
  }

  editItemFoodAddOnsAssociation(association: ItemFoodAddOnsAssociation) {
    this.itemFoodAddOnsAssociationService.itemFoodAddOnsAssociationDialog.set(true);
    this.setForm(association);
  }

  hideDialog() {
    this.itemFoodAddOnsAssociationService.itemFoodAddOnsAssociationDialog.set(false);
  }

  deleteItemFoodAddOnsAssociation(association: ItemFoodAddOnsAssociation) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_item_food_add_ons_association'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.itemFoodAddOnsAssociationService.deleteItemFoodAddOnsAssociation(association.id);
      }
    });
  }

  saveItemFoodAddOnsAssociation() {
    console.log('Form Value:', this.itemFoodAddOnsAssociationForm.value);
    if (this.itemFoodAddOnsAssociationForm.invalid) {
      this.itemFoodAddOnsAssociationForm.markAllAsTouched();
      return;
    }

    if (this.itemFoodAddOnsAssociationForm.value.id) {
      this.itemFoodAddOnsAssociationService.updateItemFoodAddOnsAssociation(this.itemFoodAddOnsAssociationForm.value);
    } else {
      this.itemFoodAddOnsAssociationService.saveItemFoodAddOnsAssociation(this.itemFoodAddOnsAssociationForm.value);
    }
  }

  initiatForm() {
    this.itemFoodAddOnsAssociationForm = this.fb.group({
      id: [null],
      itemFoodId: [this.selectedItemFood()?.id, Validators.required],
      itemFoodAddOnsId: [null, Validators.required],
    });
  }

  setForm(association: ItemFoodAddOnsAssociation) {
    this.itemFoodAddOnsAssociationForm = this.fb.group({
      id: [association.id],
      itemFoodId: [this.selectedItemFood()?.id, Validators.required],
      itemFoodAddOnsId: [association.itemFoodAddOns?.id || association.itemFoodAddOnsId, Validators.required],
    });
  }
}
