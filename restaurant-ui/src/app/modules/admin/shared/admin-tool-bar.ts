import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import Aura from '@primeuix/themes/aura';
import Lara from '@primeuix/themes/lara';
import Nora from '@primeuix/themes/nora';
import { LayoutService } from '../../../core/service/layout.service';
import { StyleClassModule } from 'primeng/styleclass';
import { ConfirmationService, MenuItem } from 'primeng/api';
import { AppConfigurator } from "./app.configurator";
import { ApplicationSetting } from '../../../core/model/application-setting.model';
import { env } from '../../../../environment/env';
import { LoginService } from '../../login/login.service';
import { TranslateService } from '../../../core/service/translate.service';
import { ConfirmDialog } from "primeng/confirmdialog";

const presets = {
  Aura,
  Lara,
  Nora
} as const;

@Component({
  selector: 'app-admin-tool-bar',
  imports: [RouterModule, CommonModule, StyleClassModule, AppConfigurator, ConfirmDialog],
  providers: [ConfirmationService],
  template: `
     <div class="layout-topbar">
        <div class="layout-topbar-logo-container">
            <button class="layout-menu-button layout-topbar-action" (click)="layoutService.onMenuToggle()">
                <i class="pi pi-bars"></i>
            </button>
            <a class="layout-topbar-logo" routerLink="/">
                <img [src]="imageUrl + applicationSettings?.logo" alt="Logo" class="layout-topbar-logo-image" />
                <span>{{ applicationSettings?.applicationTitle}}</span>
            </a>
        </div>

        <div class="layout-topbar-actions">
            <div class="layout-config-menu">
                <button type="button" class="layout-topbar-action" (click)="toggleDarkMode()">
                    <i [ngClass]="{ 'pi ': true, 'pi-moon': layoutService.isDarkTheme(), 'pi-sun': !layoutService.isDarkTheme() }"></i>
                </button>
                <div class="relative">
                    <button
                        class="layout-topbar-action layout-topbar-action-highlight"
                        pStyleClass="@next"
                        enterFromClass="hidden"
                        enterActiveClass="animate-scalein"
                        leaveToClass="hidden"
                        leaveActiveClass="animate-fadeout"
                        [hideOnOutsideClick]="true"
                    >
                        <i class="pi pi-palette"></i>
                    </button>
                    <app-configurator />
                </div>
            </div>

            <button class="layout-topbar-menu-button layout-topbar-action" pStyleClass="@next" enterFromClass="hidden" enterActiveClass="animate-scalein" leaveToClass="hidden" leaveActiveClass="animate-fadeout" [hideOnOutsideClick]="true">
                <i class="pi pi-ellipsis-v"></i>
            </button>

            <div class="layout-topbar-menu hidden lg:block">
                <div class="layout-topbar-menu-content">

                    <button type="button" class="layout-topbar-action">
                        <i class="pi pi-inbox"></i>
                        <span>Messages</span>
                    </button>
                    <button type="button" class="layout-topbar-action">
                        <i class="pi pi-user"></i>
                        <span>Profile</span>
                    </button>


                    <button type="button" class="layout-topbar-action" (click)="logout()">
                        <i class="pi pi-sign-out"></i>
                        <span>Logout</span>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <p-confirmdialog [style]="{ width: '450px' }" />
  `,
  styles: `
    .layout-topbar-logo-image{
      width: 100px;
      height: 40px;
      border-radius: 6px;
      object-fit: cover;
    }
  `
})
export class AdminToolBar {
  items!: MenuItem[];

  layoutService = inject(LayoutService);
  readonly loginService = inject(LoginService);
  readonly translate = inject(TranslateService);
  private confirmationService = inject(ConfirmationService);
  applicationSettings: ApplicationSetting | null = null;
  imageUrl = env.baseUrl;

  constructor() {
    this.applicationSettings = JSON.parse(localStorage.getItem('applicationSettings') || '{}');
  }

  logout() {

     this.confirmationService.confirm({
      message: this.translate.instant('label_confirm_logout_message'),
      header: this.translate.instant('label_confirm_logout'),
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.loginService.logout();
      }
    });

  }
  login() {
  }

  isLoggedIn(): boolean {
    return false;
  }
  toggleDarkMode() {
    this.layoutService.layoutConfig.update((state) => ({
      ...state,
      darkTheme: !state.darkTheme
    }));
  }
}
