import { Component, inject, OnInit } from '@angular/core';
import { RoleService } from '../roles/role.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TranslateService } from '../../../../core/service/translate.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Role } from '../../../../core/model/role.model';
import { Button } from "primeng/button";
import { Toolbar } from "primeng/toolbar";
import { FormInput } from "../../../../shared/components/form-input/form-input";
import { Overlay } from "primeng/overlay";
import { ToggleSwitchModule } from 'primeng/toggleswitch';
import { ActivatedRoute } from '@angular/router';
import { MenuItemModel } from '../../../../core/model/menuItem.model';
import { Divider } from "primeng/divider";
import { Checkbox } from "primeng/checkbox";
import { ToggleButtonComponent } from "../../../../shared/components/toggle-button.component";

@Component({
  selector: 'app-edit-system-role',
  imports: [Button, FormInput, ReactiveFormsModule, FormsModule, ToggleSwitchModule, Divider, ToggleButtonComponent],
  templateUrl: './edit-system-role.html',
  styleUrls: ['./edit-system-role.scss'],
  providers: [MessageService, ConfirmationService]
})
export class EditSystemRole implements OnInit {

  rolesService = inject(RoleService);

  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  private readonly route = inject(ActivatedRoute);
  readonly translate = inject(TranslateService);




  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      this.rolesService.loadRole(+id!);
    });

    this.rolesService.loadMenuItems();
    this.rolesService.loadModules();
    this.rolesService.loadAllRolePermissions();
  }


  save() { }




  getRoleIdAndMenuItemId(  menuItemId: number) {
    const permission = this.rolesService.allRolePermissions().find((perm) => perm.roleId === this.rolesService.selectedRole()?.id && perm.menuItemId === menuItemId);
    return permission;
  }

  onChange(
    checked: boolean,
    menuItemId: number,
    field: 'canCreate' | 'canRead' | 'canEdit' | 'canDelete'
  ) {
    const row = this.getRoleIdAndMenuItemId(menuItemId);
    let payload: any;

    if(row){
       row[field] = checked;
        console.log({ ...row });
        payload = { ...row };
    } else{
      // If no existing permission, create a new one
       payload =
        {
        roleId: this.rolesService.selectedRole()?.id,
        menuItemId: menuItemId,
        canCreate: field === 'canCreate' ? checked : false,
        canRead: field === 'canRead' ? checked : false,
        canEdit: field === 'canEdit' ? checked : false,
        canDelete: field === 'canDelete' ? checked : false,
      };
    }




    this.rolesService.updateOrCreateRolePermission(payload);
  }

}
