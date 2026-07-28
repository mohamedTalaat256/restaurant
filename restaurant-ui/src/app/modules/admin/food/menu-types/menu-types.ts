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
import { MenuTypeService } from './menu-type.service';
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { MenuType } from '../../../../core/model/menu-type.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';

interface Column {
  field: string;
  header: string;
  customExportHeader?: string;
}

interface ExportColumn {
  title: string;
  dataKey: string;
}


@Component({
  selector: 'app-menu-types',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, InputTextModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast,ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './menu-types.html',
  styleUrls: ['./menu-types.scss'],
  providers: [MessageService, ConfirmationService, MenuTypeService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class MenuTypes implements OnInit {

  submitted: boolean = false;
  @ViewChild('dt') dt!: Table;
  exportColumns!: ExportColumn[];
  cols!: Column[];

  menuTypeForm!: FormGroup;
  imageFile: File | null = null;
  defaultPreview: string | ArrayBuffer | null = '/images/placeholder.png';
  imagesUrl = env.baseUrl;

  menuTypeService = inject(MenuTypeService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'menu_types')?.id || 0;
  ngOnInit(): void {
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
    this.menuTypeService.menuTypeDialog.set(true);
  }

  editMenuType(menuType: MenuType) {
    this.menuTypeService.menuTypeDialog.set(true);
    if (menuType.image) {
      this.defaultPreview = this.imagesUrl + menuType.image;
    }
    this.setForm(menuType);
  }

  hideDialog() {
    this.menuTypeService.menuTypeDialog.set(false);
  }

  deleteMenuType(menuType: MenuType) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_menu_type'),
      header: this.translate.instant('label_confirm'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.menuTypeService.deleteMenuType(menuType.id);
      }
    });
  }

  saveMenuType() {
    if (this.menuTypeForm.invalid) {
      this.menuTypeForm.markAllAsTouched();
      return;
    }

    if (this.menuTypeForm.value.id) {
      this.menuTypeService.updateMenuType(this.menuTypeForm.value, this.imageFile);
    } else {
      this.menuTypeService.saveMenuType(this.menuTypeForm.value, this.imageFile);
    }
  }

  initiatForm() {
    this.menuTypeForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      status: [true],
    });
  }

  setForm(menuType: MenuType) {
    this.menuTypeForm = this.fb.group({
      id: [menuType.id],
      name: [menuType.name, Validators.required],
      status: [menuType.status],
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
