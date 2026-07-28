import { Component, effect, ElementRef, inject, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { filter, Subject, takeUntil } from 'rxjs';
import { LayoutService } from '../../../core/service/layout.service';
import { MenuItem } from 'primeng/api';
import { AppMenuitem } from './app.menuitem';
import { CommonModule } from '@angular/common';
import { TranslateService } from '../../../core/service/translate.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterModule, CommonModule, AppMenuitem, RouterModule],
  template: `
        <div class="layout-sidebar">
            <ul class="layout-menu">
        @for (item of model; track item.label) {
            @if (!item.separator) {
              <!-- i will pass  -->
                <li app-menuitem [item]="item" [root]="true"></li>
            } @else {
                <li class="menu-separator"></li>
            }
        }
    </ul>
        </div>
    `
})
export class AppSidebar implements OnInit, OnDestroy {
  layoutService = inject(LayoutService);
  translate = inject(TranslateService);
  model: MenuItem[] = [];

  router = inject(Router);

  el = inject(ElementRef);

  private outsideClickListener: ((event: MouseEvent) => void) | null = null;

  private destroy$ = new Subject<void>();

  constructor() {
    effect(() => {
      const state = this.layoutService.layoutState();

      if (this.layoutService.isDesktop()) {
        if (state.overlayMenuActive) {
          this.bindOutsideClickListener();
        } else {
          this.unbindOutsideClickListener();
        }
      } else {
        if (state.mobileMenuActive) {
          this.bindOutsideClickListener();
        } else {
          this.unbindOutsideClickListener();
        }
      }
    });
  }

  ngOnInit() {
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        takeUntil(this.destroy$)
      )
      .subscribe((event) => {
        const navEvent = event as NavigationEnd;
        this.onRouteChange(navEvent.urlAfterRedirects);
      });

    this.onRouteChange(this.router.url);

    this.model = [
      {
        label: 'Home',
        items: [
          { label: this.translate.instant('label_dashboard'), icon: 'pi pi-fw pi-home', routerLink: ['/admin/dashboard'] },
          { label: this.translate.instant('label_pos'), icon: 'pi pi-fw pi-desktop', routerLink: ['/admin/pos'] }
        ],
      },

      {
        label: this.translate.instant('title_operations'),
        items: [
          {
            label: this.translate.instant('label_purchases'),
            icon: 'pi pi-fw pi-cart-minus',
            path: '/admin/purchases',
            items: [
              { label: this.translate.instant('label_purchases'), routerLink: ['/admin/purchases'] },
              { label: this.translate.instant('label_ingredients'), routerLink: ['/admin/ingredients'] },
              { label: this.translate.instant('label_suppliers'), routerLink: ['/admin/suppliers'] },

            ]
          },
          {
            label: this.translate.instant('label_orders_management'),
            icon: 'pi pi-fw pi-receipt',
            path: '/admin/orders',
            items: [
              { label: this.translate.instant('label_pos'), routerLink: ['/admin/pos'] },
              { label: this.translate.instant('label_cash_counters'), routerLink: ['/admin/cash-counters'] },
              { label: this.translate.instant('label_cash_registers'), routerLink: ['/admin/cash-registers'] }
            ]
          }

        ],
      },


      {
        label: this.translate.instant('label_application_settings'),
        items: [
           {
            label: this.translate.instant('label_accounts'),
            icon: 'pi pi-fw pi-calculator',
            path: '/admin/accounts',
            items: [
              { label: this.translate.instant('label_accounts'), routerLink: ['/admin/accounts'] },
              { label: this.translate.instant('label_customer_account_report'),  routerLink: ['/admin/customer-account-report'] },
              { label: this.translate.instant('label_supplier_account_report'),  routerLink: ['/admin/supplier-account-report'] },
              { label: this.translate.instant('label_cost_centers'), routerLink: ['/admin/cost-centers'] },
              { label: this.translate.instant('label_fiscal_periods'), routerLink: ['/admin/fiscal-periods'] },
              { label: this.translate.instant('label_journal_entries'), routerLink: ['/admin/journal-entries'] },
              { label: this.translate.instant('label_general_ledger'), routerLink: ['/admin/general-ledger'] },
              { label: this.translate.instant('label_trial_balance'), routerLink: ['/admin/trial-balance'] },
              { label: this.translate.instant('label_profit_loss'), routerLink: ['/admin/profit-loss'] },
              { label: this.translate.instant('label_balance_sheet'), routerLink: ['/admin/balance-sheet'] },
              { label: this.translate.instant('label_year_end_closing'), routerLink: ['/admin/year-end-closing'] }
            ]
          },
          {
            label: this.translate.instant('label_food_management'),
            icon: 'pi pi-fw pi-database',
            path: '/food-management',
            items: [
              { label: this.translate.instant('label_item_categories'), routerLink: ['/admin/item-categories'] },
              { label: this.translate.instant('label_menu_types'), routerLink: ['/admin/menu-types'] },
              { label: this.translate.instant('label_item_foods'), routerLink: ['/admin/item-foods'] },
              { label: this.translate.instant('label_item_food_add_ons'), routerLink: ['/admin/item-food-add-ons'] },
            ]

          },
          {
            label: this.translate.instant('label_settings'),
            icon: 'pi pi-fw pi-cog',
            path: '/restaurant-settings',
            items: [
              { label: this.translate.instant('label_application_settings'), routerLink: ['/admin/application-settings'] },
              { label: this.translate.instant('label_currencies'), routerLink: ['/admin/currencies'] },
              { label: this.translate.instant('label_languages'), routerLink: ['/admin/languages'] },
              { label: this.translate.instant('label_language_translations'), routerLink: ['/admin/language-translations'] },
              { label: this.translate.instant('label_units_of_measurment'), routerLink: ['/admin/uoms'] },
              { label: this.translate.instant('label_floors'), routerLink: ['/admin/floors'] },
              { label: this.translate.instant('label_tables'), routerLink: ['/admin/tables'] },
              { label: this.translate.instant('label_kitchens'), routerLink: ['/admin/kitchens'] },

            ]
          },
          {
            label: this.translate.instant('label_customers'),
            icon: 'pi pi-fw pi-users',
            path: '/admin/customers',
            items: [
              { label: this.translate.instant('label_customers'), routerLink: ['/admin/customers'] },
              { label: this.translate.instant('label_customer_types'), routerLink: ['/admin/customer-types'] },
              { label: this.translate.instant('label_third_party_customers'), routerLink: ['/admin/third-party-customers'] }
            ]
          },
          {
            label: this.translate.instant('label_users'),
            icon: 'pi pi-fw pi-users',
            path: '/admin/users',
            items: [
              { label: this.translate.instant('label_manage_users'), routerLink: ['/admin/users'] },
              { label: this.translate.instant('label_manage_roles'), routerLink: ['/admin/roles'] }
            ]
          }
        ]
      }
    ];
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
    this.unbindOutsideClickListener();
  }

  private onRouteChange(path: string) {
    this.layoutService.layoutState.update((val) => ({
      ...val,
      activePath: path,
      overlayMenuActive: false,
      staticMenuMobileActive: false,
      mobileMenuActive: false,
      menuHoverActive: false
    }));
  }

  private bindOutsideClickListener() {
    if (!this.outsideClickListener) {
      this.outsideClickListener = (event: MouseEvent) => {
        if (this.isOutsideClicked(event)) {
          this.layoutService.layoutState.update((val) => ({
            ...val,
            overlayMenuActive: false,
            staticMenuMobileActive: false,
            mobileMenuActive: false,
            menuHoverActive: false
          }));
        }
      };

      document.addEventListener('click', this.outsideClickListener);
    }
  }

  private unbindOutsideClickListener() {
    if (this.outsideClickListener) {
      document.removeEventListener('click', this.outsideClickListener);
      this.outsideClickListener = null;
    }
  }

  private isOutsideClicked(event: MouseEvent): boolean {
    const topbarButtonEl = document.querySelector('.topbar-start > button');
    const sidebarEl = this.el.nativeElement;

    return !(
      sidebarEl?.isSameNode(event.target as Node) ||
      sidebarEl?.contains(event.target as Node) ||
      topbarButtonEl?.isSameNode(event.target as Node) ||
      topbarButtonEl?.contains(event.target as Node)
    );
  }
}
