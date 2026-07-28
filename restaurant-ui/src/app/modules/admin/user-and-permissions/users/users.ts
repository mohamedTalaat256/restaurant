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
import { UserService } from './user.service';
import { FormInput } from "../../../../shared/components/form-input/form-input";
import { User } from '../../../../core/model/user.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { env } from '../../../../../environment/env';
import { AvatarModule } from 'primeng/avatar';
import { Toast } from "primeng/toast";
import { RoleService } from '../roles/role.service';
import { ShowIfCanCreateDirective } from '../../../../core/directives/showIfCanCreate';
import { ShowIfCanEditDirective } from '../../../../core/directives/showIfCanEdit';
import { ShowIfCanDeleteDirective } from '../../../../core/directives/showIfCanDelete';

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
  selector: 'app-users',
  imports: [CommonModule, TableModule, ButtonModule,
    ToolbarModule, RatingModule, InputTextModule, TextareaModule, SelectModule,
    DialogModule, TagModule, InputIconModule,
    IconFieldModule, ConfirmDialogModule,
    ProgressBarModule, AvatarModule,
    ReactiveFormsModule, FormInput, Toast, ShowIfCanCreateDirective, ShowIfCanEditDirective, ShowIfCanDeleteDirective],
  templateUrl: './users.html',
  styleUrls: ['./users.scss'],
  providers: [MessageService, ConfirmationService, UserService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class Users implements OnInit {

  selectedUsers!: User[] | null;
  submitted: boolean = false;
  statusOptions!: any[];
  @ViewChild('dt') dt!: Table;
  exportColumns!: ExportColumn[];
  cols!: Column[];

  userForm!: FormGroup;

  usersService = inject(UserService);
  roleService = inject(RoleService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  readonly translate = inject(TranslateService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  menuItemId: number = env.menuItems.find(item => item.name === 'users')?.id || 0;

  roleOptions = computed(() => {
    return this.roleService.roles().map(role => ({ label: role.name, value: role.id }));
  });

  defaultPreview: string | ArrayBuffer | null = '/images/person.jpg';
  imageFile: File | null = null;
  imagesUrl = env.baseUrl;

  ngOnInit(): void {
    this.usersService.loadUsers();
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
    this.usersService.userDialog.set(true);
  }

  editUser(user: User) {
    this.usersService.userDialog.set(true);
    if(user.image){
      this.defaultPreview = this.imagesUrl+user.image;
    }
    this.setForm(user);
  }

  deleteSelectedUsers() {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete the selected users?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.usersService.users.set(this.usersService.users().filter((val) => !this.selectedUsers?.includes(val)));
        this.selectedUsers = null;
        this.messageService.add({
          severity: 'success',
          summary: 'Successful',
          detail: 'Users Deleted',
          life: 3000
        });
      }
    });
  }

  hideDialog() {
    this.usersService.userDialog.set(false);
  }

  deleteUser(user: User) {
    this.confirmationService.confirm({
      message: this.translate.instant('confirm_delete_user'),
      header: this.translate.instant('label_confrim'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.usersService.deleteUser(user.id);
      }
    });

  }

  saveUser() {
    if(this.userForm.invalid){
      this.userForm.markAllAsTouched();
      return;
    }

    if(this.userForm.value.id){
      this.usersService.updateUser(this.userForm.value, this.imageFile);
    } else {
      this.usersService.saveUser(this.userForm.value, this.imageFile);
    }
  }

  initiatForm() {
    this.userForm = this.fb.group({
      id: [null],
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      about: [''],
      status: [true],
      isMonitor: [false],
      roleIds: [[], Validators.required],

    });
  }

  setForm(user: User) {
    this.userForm = this.fb.group({
      id: [user.id],
      firstname: [user.firstname, Validators.required],
      lastname: [user.lastname, Validators.required],
      email: [user.email, [Validators.required, Validators.email]],
      password: [user.password, [Validators.required, Validators.minLength(6)]],
      about: [user.about],
      status: [user.status],
      isMonitor: [user.isMonitor],
      roleIds: [user.roleIds, Validators.required],
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
    this.defaultPreview = '/images/person.jpg';

    const fileInput = document.getElementById('inputTag') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
    this.cdr.markForCheck();
  }

}
