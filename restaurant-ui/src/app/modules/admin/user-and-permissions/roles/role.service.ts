

import { inject, Injectable, signal } from '@angular/core';
import { Role } from '../../../../core/model/role.model';
import { ApiResponse } from '../../../../core/model/api-response.model';
import { env } from '../../../../../environment/env';
import { MessageService } from 'primeng/api';
import { HttpClient } from '@angular/common/http';
import { MenuItemModel } from '../../../../core/model/menuItem.model';
import { ModuleModel } from '../../../../core/model/module.model';
import { TranslateService } from '../../../../core/service/translate.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

export const EMPTY_ROLE_PERMISSION = {
  id: 0,
  roleId: 0,
  menuItemId: 0,
  canRead: false,
  canCreate: false,
  canEdit: false,
  canDelete: false
};


@Injectable({ providedIn: 'root' })
export class RoleService {

  private fb = inject(FormBuilder);
  roleForm!: FormGroup;


  selectedRole = signal<Role | null>(null);
  allRolePermissions = signal<any[]>([]);
  authUserRolePermissions = signal<any[]>([]);
  menuRolePermission = signal<any>(EMPTY_ROLE_PERMISSION);
  roles = signal<Role[]>([]);
  menuItems = signal<MenuItemModel[]>([]);
  modules = signal<ModuleModel[]>([]);
  loadingModules = signal(false);
  menuItemsLoading = signal(false);
  loading = signal(false);
  loadingSave = signal(false);
  savedSuccess = signal(false);

  roleDialog = signal(false);



  error = signal<string | null>(null);
  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  private translate = inject(TranslateService);


  loadRoles() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<ApiResponse<Role[]>>(env.apiUrl + '/roles').subscribe({
      next: (res) => {
        this.roles.set(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  loadRole(id: number) {
    this.loading.set(true);
    this.error.set(null);
    this.http.get<ApiResponse<null>>(env.apiUrl + '/roles/' + id).subscribe({
      next: (res) => {
        this.selectedRole.set(res.data);
        this.setForm(res.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  loadMenuItems() {
    this.menuItemsLoading.set(true);
    this.http.get<ApiResponse<MenuItemModel[]>>(env.apiUrl + '/menu-items').subscribe({
      next: (res: ApiResponse<MenuItemModel[]>) => {
        if (res.status) {
          this.menuItems.set(res.data);
        }
        this.menuItemsLoading.set(false);
      },
      error: () => {
        this.menuItemsLoading.set(false);
      }
    });
  }

  loadModules() {
    this.loadingModules.set(true);
    this.http.get<ApiResponse<ModuleModel[]>>(env.apiUrl + '/modules').subscribe({
      next: (res: ApiResponse<ModuleModel[]>) => {
        if (res.status) {
          this.modules.set(res.data);
        }
        this.loadingModules.set(false);
      }
      ,
      error: () => {
        this.loadingModules.set(false);
      }
    });
  }

  loadAllRolePermissions() {
    this.http.get<ApiResponse<any[]>>(env.apiUrl + '/role-permissions').subscribe({
      next: (res) => {
        if (res.status) {
          this.allRolePermissions.set(res.data);
        }
      }
    });
  }

  loadMenuItemRolePermissions(menuItemId: number) {
    this.http.get<ApiResponse<any[]>>(env.apiUrl + '/role-permissions/by-menu-item/' + menuItemId).subscribe({
      next: (res) => {
        if (res.status) {
          this.menuRolePermission.set(res.data);
        }
      }
    });
  }

  loadRolePermissionsByAuthUserRoles(){
    this.http.get<ApiResponse<any[]>>(env.apiUrl + '/role-permissions/by-authenticated-user-roles').subscribe({
      next: (res) => {
        if (res.status) {
          this.authUserRolePermissions.set(res.data);
          localStorage.setItem('authUserRolePermissions', JSON.stringify(res.data));
        }
      }
    });
  }


  updateOrCreateRolePermission(permission: any) {

    this.http.put<ApiResponse<any>>(env.apiUrl + '/role-permissions/update-or-create', permission).subscribe({
      next: (res) => {
        if (res.status) {
          this.loadAllRolePermissions();
          this.loadRolePermissionsByAuthUserRoles();
          //this.loadMenuItemRolePermissions(permission.menuItemId);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('msg_permission_updated') });
        }
      }
    });
  }

  saveRole(formValue: Role) {
    this.loadingSave.set(true);
    this.error.set(null);

    this.http.post<ApiResponse<Role>>(env.apiUrl + '/roles', formValue).subscribe({
      next: (res) => {
        if (res.status) {
          this.roles.update((roles) => [...roles, res.data]);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('msg_role_created') });
          this.roleDialog.set(false);
        }
        this.loadingSave.set(false);
      },
      error: () => {
        this.loadingSave.set(false);
      }
    });

  }

  deleteRole(id: number) {
    this.loading.set(true);
    this.error.set(null);
    this.http.delete<any>(env.apiUrl + '/roles/' + id).subscribe({
      next: (res: ApiResponse<null>) => {
        if (res.status) {
          this.roles.update((roles) => roles.filter(role => role.id !== id));
        } else {
          this.error.set(res.message);
        }
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }


  setForm(role: Role | null) {
    if (!role) return;
    this.roleForm = this.fb.group({
      id: [role.id],
      name: [role.name, Validators.required],
      description: [role.description, Validators.required],
      status: [role.status]
    });
  }

}
