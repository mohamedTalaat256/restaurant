package com.mtalaat.restaurant.modules.auth.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SideMenuItemDto {

    private String label;
    private String icon;
    private String routerLink;
    private Boolean isPage;
    private List<SideMenuItemDto> items;

}


/*

 [
      {
        label: 'Home',
        items: [{ label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/admin/dashboard'] }]
      },


      {
        label: this.translate.instant('label_users'),
        items: [
          { label: this.translate.instant('label_users'), icon: 'pi pi-fw pi-users', routerLink: ['/admin/users'] },
          {
            label: this.translate.instant('label_roles'),
            icon: 'pi pi-fw pi-users',
            path: '/admin',
              items:[
                { label: this.translate.instant('label_manage_roles'), icon: 'pi pi-fw pi-users', routerLink: ['/admin/roles'] },
                { label: this.translate.instant('label_create_system_role'), icon: 'pi pi-fw pi-user-plus', routerLink: ['/admin/roles/create-system-role'] },
              ]
            }
         ]
      },
    ]
*/