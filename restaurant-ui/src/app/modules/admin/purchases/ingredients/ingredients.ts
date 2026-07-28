import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { ProgressBarModule } from 'primeng/progressbar';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { IngredientService } from './ingredient.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { Ingredient } from '../../../../core/model/ingredient.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { Toast } from 'primeng/toast';
import { UomService } from '../../settings/unit-of-measurements/uom.service';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';

@Component({
  selector: 'app-ingredients',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, TextareaModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './ingredients.html',
  styleUrls: ['./ingredients.scss'],
  providers: [MessageService, ConfirmationService, IngredientService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Ingredients implements OnInit {

  selectedIngredients!: Ingredient[] | null;
  @ViewChild('dt') dt!: Table;

  ingredientForm!: FormGroup;

  ingredientService = inject(IngredientService);
  uomService = inject(UomService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);

  menuItemId: number = env.menuItems.find(item => item.name === 'ingredients')?.id || 0;

  uomOptions = computed(() => {
    return this.uomService.uoms().map(uom => ({ label: uom.name, value: uom.id }));
  });

  ngOnInit(): void {
    this.ingredientService.loadIngredients();
    this.uomService.loadUoms();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.ingredientService.ingredientDialog.set(true);
  }

  editIngredient(ingredient: Ingredient) {
    this.ingredientService.ingredientDialog.set(true);
    this.setForm(ingredient);
  }


  hideDialog() {
    this.ingredientService.ingredientDialog.set(false);
  }

  deleteIngredient(ingredient: Ingredient) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_ingredient'),
      header: this.translate.instant('label_confrim'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.ingredientService.deleteIngredient(ingredient.id);
      }
    });
  }

  saveIngredient() {
    if (this.ingredientForm.invalid) {
      this.ingredientForm.markAllAsTouched();
      return;
    }

    if (this.ingredientForm.value.id) {
      this.ingredientService.updateIngredient(this.ingredientForm.value);
    } else {
      this.ingredientService.saveIngredient(this.ingredientForm.value);
    }
  }

  initiatForm() {
    this.ingredientForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      uomId: [null, Validators.required],
      stockQuantity: [0, Validators.required],
      minStockQuantity: [0, Validators.required],
      status: [true],
    });
  }

  setForm(ingredient: Ingredient) {
    this.ingredientForm = this.fb.group({
      id: [ingredient.id],
      name: [ingredient.name, Validators.required],
      uomId: [ingredient.uomId, Validators.required],
      stockQuantity: [ingredient.stockQuantity, Validators.required],
      minStockQuantity: [ingredient.minStockQuantity, Validators.required],
      status: [ingredient.status],
    });
  }
}
