import { Routes } from "@angular/router";
import { AdminDashboard } from "./admin-dashboard/admin-dashboard";
import { Users } from "./user-and-permissions/users/users";
import { Admin } from "./admin";
import { Roles } from "./user-and-permissions/roles/roles";
import { EditSystemRole } from "./user-and-permissions/edit-system-role/edit-system-role";
import { UnitOfMeasurements } from "./settings/unit-of-measurements/unit-of-measurements";
import { Floors } from "./settings/floors/floors";
import { Tables } from "./settings/tables/tables";
import { Kitchens } from "./settings/kitchens/kitchens";
import { Currencies } from "./settings/currencies/currencies";
import { Languages } from "./settings/languages/languages";
import { LanguageTranslations } from "./settings/language-translations/language-translations";
import { ApplicationSettings } from "./settings/application-settings/application-settings";
import { ItemCategories } from "./food/item-categories/item-categories";
import { MenuTypes } from "./food/menu-types/menu-types";
import { ItemFoods } from "./food/item-foods/item-foods";
import { ItemFoodVariants } from "./food/item-food-variants/item-food-variants";
import { ItemFoodAddOnsComponent } from "./food/item-food-add-ons/item-food-add-ons";
import { ItemFoodAddOnsAssociationComponent } from "./food/item-food-add-ons-association/item-food-add-ons-association";
import { CustomerTypes } from "./settings/customer-types/customer-types";
import { Customers } from "./settings/customers/customers";
import { ThirdPartyCustomers } from "./settings/third-party-customers/third-party-customers";
import { Accounts } from "./accounts/accounts/accounts";
import { CustomerAccountReport } from "./accounts/customer-account-report/customer-account-report";
import { Suppliers } from "./purchases/suppliers/suppliers";
import { Ingredients } from "./purchases/ingredients/ingredients";
import { Purchases } from "./purchases/purchases/purchases";
import { EditPurchase } from "./purchases/edit-purchase/edit-purchase";
import { SupplierAccountReport } from "./accounts/supplier-account-report/supplier-account-report";
import { Pos } from "./orders/pos/pos";
import { CashCounters } from "./orders/cash-counters/cash-counters";
import { CashRegisters } from "./orders/cash-registers/cash-registers";
import { Orders } from "./orders/orders/orders";
import { OrderDetail } from "./orders/order-detail/order-detail";
import { KitchenDashboard } from "./orders/kitchen-dashboard/kitchen-dashboard";
import { OrderTracking } from "./orders/order-tracking/order-tracking";
import { CostCenters } from "./accounts/cost-centers/cost-centers";
import { FiscalPeriods } from "./accounts/fiscal-periods/fiscal-periods";
import { JournalEntries } from "./accounts/journal-entries/journal-entries";
import { GeneralLedger } from "./accounts/financial-reports/general-ledger/general-ledger";
import { TrialBalanceComponent } from "./accounts/financial-reports/trial-balance/trial-balance";
import { ProfitLossComponent } from "./accounts/financial-reports/profit-loss/profit-loss";
import { BalanceSheetComponent } from "./accounts/financial-reports/balance-sheet/balance-sheet";
import { YearEndClosingComponent } from "./accounts/financial-reports/year-end-closing/year-end-closing";
import { Deliveries } from "./delivery/deliveries/deliveries";
import { DeliveryDetail } from "./delivery/delivery-detail/delivery-detail";
import { Drivers } from "./delivery/drivers/drivers";
import { DriverDetail } from "./delivery/driver-detail/driver-detail";

export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    component: Admin,
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      { path: 'dashboard', component: AdminDashboard },

      /* user and permissions */
      { path: 'users', component: Users },
      { path: 'roles', component: Roles },
      { path: 'roles/edit-system-role/:id', component: EditSystemRole},


      /* settings */
      {path:'uoms', component: UnitOfMeasurements},
      {path:'floors', component: Floors},
      {path:'tables', component: Tables},
      {path:'kitchens', component: Kitchens},
      {path:'currencies', component: Currencies},
      {path:'languages', component: Languages},
      {path:'language-translations', component: LanguageTranslations},
      {path:'application-settings', component: ApplicationSettings},

      /* food management */

      {path:'menu-types', component: MenuTypes},
      {path:'item-categories', component: ItemCategories},
      {path:'item-foods', component: ItemFoods},
      {path:'item-food-add-ons', component: ItemFoodAddOnsComponent},

      /* payment & customers */
      {path:'customer-types', component: CustomerTypes},
      {path:'customers', component: Customers},
      {path:'third-party-customers', component: ThirdPartyCustomers},

      /* accounts */
      {path:'accounts', component: Accounts},
      {path: 'customer-account-report', component: CustomerAccountReport},
      {path: 'supplier-account-report', component: SupplierAccountReport},
      {path: 'cost-centers', component: CostCenters},
      {path: 'fiscal-periods', component: FiscalPeriods},
      {path: 'journal-entries', component: JournalEntries},
      {path: 'general-ledger', component: GeneralLedger},
      {path: 'trial-balance', component: TrialBalanceComponent},
      {path: 'profit-loss', component: ProfitLossComponent},
      {path: 'balance-sheet', component: BalanceSheetComponent},
      {path: 'year-end-closing', component: YearEndClosingComponent},

      /* purchases */
      {path:'suppliers', component: Suppliers},
      {path:'ingredients', component: Ingredients},
      {path:'purchases', component: Purchases},
      {path:'purchases/edit/:id', component: EditPurchase},


      /* orders */
      {path:'pos', component: Pos},
      {path:'orders', component: Orders},
      {path:'orders/:id', component: OrderDetail},
      {path:'orders/:id/tracking', component: OrderTracking},
      {path:'kitchen-dashboard', component: KitchenDashboard},
      {path:'cash-counters', component: CashCounters},
      {path:'cash-registers', component: CashRegisters},

      /* delivery */
      {path:'deliveries', component: Deliveries},
      {path:'deliveries/:id', component: DeliveryDetail},
      {path:'drivers', component: Drivers},
      {path:'drivers/:id', component: DriverDetail},
    ],

  }
];
