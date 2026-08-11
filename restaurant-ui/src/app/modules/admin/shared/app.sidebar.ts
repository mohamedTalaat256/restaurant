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
          { label: 'label_dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/admin/dashboard'] },
          { label: 'label_pos', icon: 'pi pi-fw pi-desktop', routerLink: ['/admin/pos'] }
        ],
      },

      {
        label: 'title_operations',
        items: [
          {
            label: 'label_purchases',
            icon: 'pi pi-fw pi-cart-minus text-red-500',
            path: '/admin/purchases',
            items: [
              { label: 'label_purchases', routerLink: ['/admin/purchases'] },
              { label: 'label_ingredients', routerLink: ['/admin/ingredients'] },
              { label: 'label_suppliers', routerLink: ['/admin/suppliers'] },

            ]
          },
          {
            label: 'label_orders_management',
            icon: 'pi pi-fw pi-receipt text-blue-500',
            path: '/admin/orders',
            items: [
              { label: 'label_pos', routerLink: ['/admin/pos'] },
              { label: 'label_cash_counters', routerLink: ['/admin/cash-counters'] },
              { label: 'label_cash_registers', routerLink: ['/admin/cash-registers'] }
            ]
          },
          {
            label: 'label_delivery_module',
            icon: 'pi pi-fw pi-truck text-green-500',
            path: '/admin/deliveries',
            items: [
              { label: 'label_deliveries', routerLink: ['/admin/deliveries'] },
              { label: 'label_drivers', routerLink: ['/admin/drivers'] },
            ]
          }

        ],
      },


      {
        label: 'label_application_settings',
        items: [
           {
            label: 'label_accounts',
            icon: 'pi pi-fw pi-calculator text-yellow-500',
            path: '/admin/accounts',
            items: [
              { label: 'label_accounts', routerLink: ['/admin/accounts'] },
              { label: 'label_customer_account_report',  routerLink: ['/admin/customer-account-report'] },
              { label: 'label_supplier_account_report',  routerLink: ['/admin/supplier-account-report'] },
              { label: 'label_cost_centers', routerLink: ['/admin/cost-centers'] },
              { label: 'label_fiscal_periods', routerLink: ['/admin/fiscal-periods'] },
              { label: 'label_journal_entries', routerLink: ['/admin/journal-entries'] },
              { label: 'label_general_ledger', routerLink: ['/admin/general-ledger'] },
              { label: 'label_trial_balance', routerLink: ['/admin/trial-balance'] },
              { label: 'label_profit_loss', routerLink: ['/admin/profit-loss'] },
              { label: 'label_balance_sheet', routerLink: ['/admin/balance-sheet'] },
              { label: 'label_year_end_closing', routerLink: ['/admin/year-end-closing'] }
            ]
          },
          {
            label: 'label_food_management',
            icon: 'pi pi-fw pi-database text-gray-500',
            path: '/food-management',
            items: [
              { label: 'label_item_categories', routerLink: ['/admin/item-categories'] },
              { label: 'label_menu_types', routerLink: ['/admin/menu-types'] },
              { label: 'label_item_foods', routerLink: ['/admin/item-foods'] },
              { label: 'label_item_food_add_ons', routerLink: ['/admin/item-food-add-ons'] },
            ]

          },
          {
            label: 'label_settings',
            icon: 'pi pi-fw pi-cog',
            path: '/restaurant-settings',
            items: [
              { label: 'label_application_settings', routerLink: ['/admin/application-settings'] },
              { label: 'label_currencies', routerLink: ['/admin/currencies'] },
              { label: 'label_languages', routerLink: ['/admin/languages'] },
              { label: 'label_language_translations', routerLink: ['/admin/language-translations'] },
              { label: 'label_units_of_measurment', routerLink: ['/admin/uoms'] },
              { label: 'label_floors', routerLink: ['/admin/floors'] },
              { label: 'label_tables', routerLink: ['/admin/tables'] },
              { label: 'label_kitchens', routerLink: ['/admin/kitchens'] },

            ]
          },
          {
            label: 'label_customers',
            icon: 'pi pi-fw pi-users',
            path: '/admin/customers',
            items: [
              { label: 'label_customers', routerLink: ['/admin/customers'] },
              { label: 'label_customer_types', routerLink: ['/admin/customer-types'] },
              { label: 'label_third_party_customers', routerLink: ['/admin/third-party-customers'] }
            ]
          },
          {
            label: 'label_users',
            icon: 'pi pi-fw pi-users',
            path: '/admin/users',
            items: [
              { label: 'label_manage_users', routerLink: ['/admin/users'] },
              { label: 'label_manage_roles', routerLink: ['/admin/roles'] }
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
