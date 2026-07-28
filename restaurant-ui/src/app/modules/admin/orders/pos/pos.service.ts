import { computed, inject, Injectable } from "@angular/core";
import { ItemCategoryService } from "../../food/item-categories/item-category.service";
import { ItemFoodService } from "../../food/item-foods/item-food.service";
import { CustomerService } from "../../settings/customers/customer.service";
import { ThirdPartyCustomerService } from "../../settings/third-party-customers/third-party-customer.service";
import { UserService } from "../../user-and-permissions/users/user.service";
import { TableService } from "../../settings/tables/table.service";
import { TranslateService } from "../../../../core/service/translate.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { CustomerType } from "../../../../core/enum/customerType.enum";
import { CashRegisterService } from "../cash-registers/cash-register.service";
import { PaymentMethod } from "../../../../core/enum/paymentMethod.enum";


@Injectable({
  providedIn: 'root'
})
export class PosService {

  posForm!: FormGroup;

  readonly translate = inject(TranslateService);
  readonly itemCategoryService = inject(ItemCategoryService);
  readonly itemFoodService = inject(ItemFoodService);
  readonly customerService = inject(CustomerService);
  readonly thirdPartyCustomerService = inject(ThirdPartyCustomerService);
  readonly tableService = inject(TableService);
  readonly userService = inject(UserService);
  readonly cashRegisterService = inject(CashRegisterService);
  readonly  fb = inject(FormBuilder);

  customerTypesOptions = computed(() => this.customerService.customerTypes().map(ct => ({ label: ct.description, value: ct.type })));
  customerOptions = computed(() => this.customerService.customers().map(c => ({ label: c.name, value: c.id })));
  thirdPartyCustomerOptions = computed(() => this.thirdPartyCustomerService.thirdPartyCustomers().map(c => ({ label: c.name, value: c.id })));
  tableOptions = computed(() => this.tableService.tables().map(t => ({ label: t.name + ' ' + t.capacity + ' ' + this.translate.instant('label_seats'), value: t.id })));
  waiterOptions = computed(() => this.userService.users().map(u => ({ label: u.firstname + ' ' + u.lastname, value: u.id })));
  paymentMethodOptions = computed(() => Object.values(PaymentMethod).map(m => ({ label: m, value: m })));
  currentOpenRegister = this.cashRegisterService.currentOpenRegister;

  constructor() {
    this.itemCategoryService.loadItemCategories();
    this.itemFoodService.loadItemFoods();
    this.customerService.loadCustomers();
    this.customerService.loadCustomerTypes();
    this.thirdPartyCustomerService.loadThirdPartyCustomers();
    this.tableService.loadTables();
    this.userService.loadUsers();
    this.cashRegisterService.getMyOpenCashRegister();
    this.initializeForm();
  }



  initializeForm() {
    this.posForm = this.fb.group({
      customerType: [CustomerType.TAKEAWAY_CUSTOMER, [Validators.required]],
      customerId: [null],
      thirdPartyCustomerId: [null],
      tableId: [null],
      waiterId: [null, Validators.required],
      paidAmount: [0, [Validators.required, Validators.min(0)]],
      paymentMethodId: [null, Validators.required],
      orderItems: this.fb.array([])
    });
  }






}
