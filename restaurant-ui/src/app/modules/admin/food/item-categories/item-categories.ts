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
import { ItemCategoryService } from './item-category.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { ItemCategory } from '../../../../core/model/item-category.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';

@Component({
  selector: 'app-item-categories',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './item-categories.html',
  styleUrls: ['./item-categories.scss'],
  providers: [ConfirmationService, ItemCategoryService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class ItemCategories implements OnInit {

  submitted: boolean = false;
  @ViewChild('dt') dt!: Table;

  itemCategoryForm!: FormGroup;
  imageFile: File | null = null;
  defaultPreview: string | ArrayBuffer | null = '/images/placeholder.png';
  imagesUrl = env.baseUrl;


  itemCategoryService = inject(ItemCategoryService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'item_categories')?.id || 0;

  ngOnInit(): void {
    this.itemCategoryService.loadItemCategories();
  }


  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.imageFile = null;
    this.defaultPreview = '/images/placeholder.png';
    this.itemCategoryService.itemCategoryDialog.set(true);
  }

  editItemCategory(itemCategory: ItemCategory) {
    this.itemCategoryService.itemCategoryDialog.set(true);
    if (itemCategory.image) {
      this.defaultPreview = this.imagesUrl + itemCategory.image;
    }
    this.setForm(itemCategory);
  }

  hideDialog() {
    this.itemCategoryService.itemCategoryDialog.set(false);
  }

  deleteItemCategory(itemCategory: ItemCategory) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_item_category'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.itemCategoryService.deleteItemCategory(itemCategory.id);
      }
    });
  }

  saveItemCategory() {
    if (this.itemCategoryForm.invalid) {
      this.itemCategoryForm.markAllAsTouched();
      return;
    }

    if (this.itemCategoryForm.value.id) {
      this.itemCategoryService.updateItemCategory(this.itemCategoryForm.value, this.imageFile);
    } else {
      this.itemCategoryService.saveItemCategory(this.itemCategoryForm.value, this.imageFile);
    }
  }

  initiatForm() {
    this.itemCategoryForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      position: [null],
      isOffer: [false],
      offerStartDate: [null],
      offerEndDate: [null],
      status: [true],
    });
  }

  setForm(itemCategory: ItemCategory) {
    this.itemCategoryForm = this.fb.group({
      id: [itemCategory.id],
      name: [itemCategory.name, Validators.required],
      position: [itemCategory.position],
      isOffer: [itemCategory.isOffer],
      offerStartDate: [itemCategory.offerStartDate],
      offerEndDate: [itemCategory.offerEndDate],
      status: [itemCategory.status],
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
}
