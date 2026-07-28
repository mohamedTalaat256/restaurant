import { CommonModule } from '@angular/common';
import { Component, effect, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import Aura from '@primeuix/themes/aura';
import Lara from '@primeuix/themes/lara';
import Nora from '@primeuix/themes/nora';
import { LayoutService } from '../../../core/service/layout.service';
import { StyleClassModule } from 'primeng/styleclass';
import { MenuItem } from 'primeng/api';
import { AppConfigurator } from "./app.configurator";
import { Button } from "primeng/button";
import { ApplicationSetting } from '../../../core/model/application-setting.model';
import { env } from '../../../../environment/env';

const presets = {
  Aura,
  Lara,
  Nora
} as const;

declare type KeyOfType<T> = keyof T extends infer U ? U : never;

declare type SurfacesType = {
  name?: string;
  palette?: {
    0?: string;
    50?: string;
    100?: string;
    200?: string;
    300?: string;
    400?: string;
    500?: string;
    600?: string;
    700?: string;
    800?: string;
    900?: string;
    950?: string;
  };
};


@Component({
  selector: 'app-admin-tool-bar',
  imports: [RouterModule, CommonModule, StyleClassModule, AppConfigurator],
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




                    <!--   @if(authService.authenticated()){
                        @if(authService.authUser()){
                      <a class="p-ripple px-0 pt-2 text-surface-900 dark:text-surface-0 font-medium text-xl">
                            {{ authService.authUser()!['preferred_username'] }}
                        </a>

                        }
                      }@else {
                        <p-button [label]="'auth.loginOAuth2' | translate" severity="contrast" styleClass="w-full" (click)="login()"></p-button>
                      } -->


                    <button type="button" class="layout-topbar-action" (click)="logout()">
                        <i class="pi pi-sign-out"></i>
                        <span>Logout</span>
                    </button>
                </div>
            </div>
        </div>
    </div>
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
  applicationSettings: ApplicationSetting | null = null;
  imageUrl = env.baseUrl;

  constructor(private router: Router) {
    this.applicationSettings = JSON.parse(localStorage.getItem('applicationSettings') || '{}');

    effect(() => {

    });
  }

  logout() {

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
