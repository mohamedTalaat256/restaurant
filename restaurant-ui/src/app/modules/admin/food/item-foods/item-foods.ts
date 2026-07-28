import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, computed, inject, OnInit, signal, ViewChild } from '@angular/core';
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
import { ItemFoodService } from './item-food.service';
import { ItemCategoryService } from '../item-categories/item-category.service';
import { KitchenService } from '../../settings/kitchens/kitchen.service';
import { MenuTypeService } from '../menu-types/menu-type.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { ItemFood } from '../../../../core/model/item-food.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ItemFoodVariantService } from '../item-food-variants/item-food-variant.service';
import { ItemFoodVariants } from "../item-food-variants/item-food-variants";
import { ItemFoodAddOnsAssociationComponent } from "../item-food-add-ons-association/item-food-add-ons-association";
import { ShowIfCanReadDirective } from '../../../../core/directives/showIfCanRead';


@Component({
  selector: 'app-item-foods',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective
      ,ShowIfCanReadDirective
    , ItemFoodVariants, ItemFoodAddOnsAssociationComponent],
  templateUrl: './item-foods.html',
  styleUrls: ['./item-foods.scss'],
  providers: [MessageService, ConfirmationService, ItemFoodService, ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class ItemFoods implements OnInit {

  @ViewChild('dt') dt!: Table;

  itemFoodForm!: FormGroup;

  imageFile: File | null = null;
  defaultPreview: string | ArrayBuffer | null = '/images/placeholder.png';
  imagesUrl = env.baseUrl;

  itemFoodService = inject(ItemFoodService);
  itemFoodVariantService = inject(ItemFoodVariantService);
  itemCategoryService = inject(ItemCategoryService);
  kitchenService = inject(KitchenService);
  menuTypeService = inject(MenuTypeService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);


  selectedItemFood = signal<ItemFood | null>(null);

  categoryOptions = computed(() => {
    return this.itemCategoryService.itemCategories().map(cat => ({ label: cat.name, value: cat.id }));
  });

  kitchenOptions = computed(() => {
    return this.kitchenService.kitchens().map(k => ({ label: k.name, value: k.id }));
  });

  menuTypeOptions = computed(() => {
    return this.menuTypeService.menuTypes().map(mt => ({ label: mt.name, value: mt.id }));
  });

  menuItemFoodId: number = env.menuItems.find(item => item.name === 'item_foods')?.id || 0;
  menuItemAssociationId: number = env.menuItems.find(item => item.name === 'item_food_add_ons_association')?.id || 0;
  menuItemAddonsId: number = env.menuItems.find(item => item.name === 'item_food_add_ons')?.id || 0;
  menuVariantId: number = env.menuItems.find(item => item.name === 'item_food_variants')?.id || 0;

  ngOnInit(): void {
    this.itemFoodService.loadItemFoods();
    this.itemCategoryService.loadItemCategories();
    this.kitchenService.loadKitchens();
    this.menuTypeService.loadMenuTypes();
  }

  exportCSV() {
    this.dt.exportCSV();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.imageFile = null;
    this.defaultPreview = '/images/placeholder.png';
    this.itemFoodService.itemFoodDialog.set(true);
  }

  editItemFood(itemFood: ItemFood) {
    this.itemFoodService.itemFoodDialog.set(true);
    if (itemFood.image) {
      this.defaultPreview = this.imagesUrl + itemFood.image;
    }
    this.setForm(itemFood);
  }

  hideDialog() {
    this.itemFoodService.itemFoodDialog.set(false);
  }

  deleteItemFood(itemFood: ItemFood) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_item_food'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.itemFoodService.deleteItemFood(itemFood.id);
      }
    });
  }

  saveItemFood() {
    if (this.itemFoodForm.invalid) {
      this.itemFoodForm.markAllAsTouched();
      return;
    }

    if (this.itemFoodForm.value.id) {
      this.itemFoodService.updateItemFood(this.itemFoodForm.value, this.imageFile);
    } else {
      this.itemFoodService.saveItemFood(this.itemFoodForm.value, this.imageFile);
    }
  }

  initiatForm() {
    this.itemFoodForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      descrip: [''],
      component: [''],
      note: [''],
      position: [null],
      isGroup: [false],
      cookedTime: [null],
      offerIsAvailable: [false],
      offerRate: [null],
      offerStartDate: [null],
      offerEndDate: [null],
      status: [true],
      categoryId: [null, Validators.required],
      kitchenId: [null],
      isCustomQty: [false],
      isSpecial: [false],
      productVat: [null],
      tax0: [null],
      tax1: [null],
      menuTypeId: [null],
    });
  }

  setForm(itemFood: ItemFood) {
    this.itemFoodForm = this.fb.group({
      id: [itemFood.id],
      name: [itemFood.name, Validators.required],
      descrip: [itemFood.descrip],
      component: [itemFood.component],
      note: [itemFood.note],
      position: [itemFood.position],
      isGroup: [itemFood.isGroup],
      cookedTime: [itemFood.cookedTime],
      offerIsAvailable: [itemFood.offerIsAvailable],
      offerRate: [itemFood.offerRate],
      offerStartDate: [itemFood.offerStartDate],
      offerEndDate: [itemFood.offerEndDate],
      status: [itemFood.status],
      categoryId: [itemFood.category?.id || itemFood.categoryId, Validators.required],
      kitchenId: [itemFood.kitchen?.id || itemFood.kitchenId],
      isCustomQty: [itemFood.isCustomQty],
      isSpecial: [itemFood.isSpecial],
      productVat: [itemFood.productVat],
      tax0: [itemFood.tax0],
      tax1: [itemFood.tax1],
      menuTypeId: [itemFood.menuType?.id || itemFood.menuTypeId],
    });
  }

  onImageSelected(event: any) {
    this.imageFile = (event.target as HTMLInputElement)?.files?.[0] || null;
    if (this.imageFile) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.defaultPreview = e.target.result;
        this.cdr.markForCheck();
      };
      reader.readAsDataURL(this.imageFile);
    }
  }

  clearImage() {
    this.imageFile = null;
    this.defaultPreview = '/images/placeholder.png';
  }


  openVariantDialog(itemFood: ItemFood) {
    this.selectedItemFood.set(itemFood);
    this.itemFoodService.itemFoodVariantDialog.set(true);
  }

  openAddOnsDialog(itemFood: ItemFood) {
    this.selectedItemFood.set(itemFood);
    this.itemFoodService.itemFoodAddonsDialog.set(true);
  }


  closeVariantDialog() {
    this.itemFoodService.itemFoodVariantDialog.set(false);
  }

  closeAddOnsDialog() {
    this.itemFoodService.itemFoodAddonsDialog.set(false);
  }


}
