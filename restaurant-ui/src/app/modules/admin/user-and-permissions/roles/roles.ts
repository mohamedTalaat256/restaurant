import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, computed, inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { RatingModule } from 'primeng/rating';
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
import { FormInput } from '../../../../shared/components/form-input/form-input';
import { Toast } from 'primeng/toast';
import { RoleService } from './role.service';
import { TranslateService } from '../../../../core/service/translate.service';
import { Role } from '../../../../core/model/role.model';
import { Router } from '@angular/router';
import { env } from '../../../../../environment/env';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';



@Component({
  selector: 'app-users',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, RatingModule, InputTextModule, TextareaModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './roles.html',
  styleUrls: ['./roles.scss'],
  providers: [MessageService, ConfirmationService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class Roles implements OnInit {

  submitted: boolean = false;
  statusOptions!: any[];
  @ViewChild('dt') dt!: Table;

  roleForm!: FormGroup;

  roleService = inject(RoleService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private router = inject(Router);

  menuItemId: number = env.menuItems.find(item => item.name === 'roles')?.id || 0;

  ngOnInit(): void {
    this.roleService.loadRoles();
  }


  exportCSV() {
    this.dt.exportCSV();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.initiatForm();
    this.roleService.roleDialog.set(true);
  }

  editRole(role: Role) {
    this.roleService.selectedRole.set(role);
    this.router.navigate(['/admin/roles/edit-system-role', role.id]);
  }



  hideDialog() {
    this.roleService.roleDialog.set(false);
  }

  deleteRole(role: Role) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_role'),
      header: this.translate.instant('label_confrim'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.roleService.deleteRole(role.id);
      }
    });

  }

  saveRole() {
    if(this.roleForm.invalid){
      this.roleForm.markAllAsTouched();
      return;
    }
    this.roleService.saveRole(this.roleForm.value);
  }

  initiatForm() {
    this.roleForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      description: ['', Validators.required],
      status: [true]
    });
  }

  setForm(role: Role) {
    this.roleForm = this.fb.group({
      id: [role.id],
      name: [role.name, Validators.required],
      description: [role.description, Validators.required],
      status: [role.status]
    });
  }

}
