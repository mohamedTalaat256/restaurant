import { computed, inject, Injectable } from "@angular/core";
import { ItemCategoryService } from "../../food/item-categories/item-category.service";
import { ItemFoodService } from "../../food/item-foods/item-food.service";
import { CustomerService } from "../../settings/customers/customer.service";
import { ThirdPartyCustomerService } from "../../settings/third-party-customers/third-party-customer.service";
import { UserService } from "../../user-and-permissions/users/user.service";
import { TableService } from "../../settings/tables/table.service";
import { TranslateService } from "../../../../core/service/translate.service";
import { FormArray, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { CustomerType } from "../../../../core/enum/customerType.enum";
import { CashRegisterService } from "../cash-registers/cash-register.service";
import { PaymentMethod } from "../../../../core/enum/paymentMethod.enum";
import { DeliveryPersonService } from "../../delivery/delivery-persons/delivery-person.service";


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
  readonly deliveryPersonService = inject(DeliveryPersonService);
  readonly  fb = inject(FormBuilder);

  customerTypesOptions = computed(() => this.customerService.customerTypes().map(ct => ({ label: ct.description, value: ct.type })));
  customerOptions = computed(() => this.customerService.customers().map(c => ({ label: c.name, value: c.id })));
  thirdPartyCustomerOptions = computed(() => this.thirdPartyCustomerService.thirdPartyCustomers().map(c => ({ label: c.name, value: c.id })));
  tableOptions = computed(() => this.tableService.tables().map(t => ({ label: t.name + ' ' + t.capacity + ' ' + this.translate.instant('label_seats'), value: t.id })));
  waiterOptions = computed(() => this.userService.users().map(u => ({ label: u.firstname + ' ' + u.lastname, value: u.id })));
  deliveryPersonOptions = computed(() => this.deliveryPersonService.deliveries().map(d => ({ label: d.firstname + ' ' + d.lastname, value: d.userId })));
  paymentMethodOptions = computed(() => Object.values(PaymentMethod).map(m => ({ label: this.translate.instant(m), value: m })));
  currentOpenRegister = this.cashRegisterService.currentOpenRegister;

  constructor() {
    this.itemCategoryService.loadItemCategories();
    this.itemFoodService.loadItemFoods();
    this.customerService.loadCustomers();
    this.customerService.loadCustomerTypes();
    this.thirdPartyCustomerService.loadThirdPartyCustomers();
    this.tableService.loadTables();
    this.userService.loadUsers();
    this.deliveryPersonService.loadDeliveries(true);
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
      deliveryAddress: [null],
      deliveryCost: [0],
      deliveryPersonId: [null],
      paidAmount: [0, [Validators.required, Validators.min(0)]],
      paymentMethodId: [PaymentMethod.CASH, Validators.required],
      orderItems: this.fb.array([])
    });

    // Handle customer type changes (for TAKEAWAY_CUSTOMER, auto-set to CASH)
    this.posForm.get('customerType')?.valueChanges.subscribe((customerType) => {
      const paymentMethodControl = this.posForm.get('paymentMethodId');
      if (customerType === CustomerType.TAKEAWAY_CUSTOMER) {
        paymentMethodControl?.setValue(PaymentMethod.CASH, { emitEvent: false });
        const invoiceTotal = this.calculateInvoiceTotal();
        this.posForm.get('paidAmount')?.setValue(invoiceTotal, { emitEvent: false });
      }
    });

    // Auto-update paidAmount based on paymentMethodId and disable/enable control
    this.posForm.get('paymentMethodId')?.valueChanges.subscribe((paymentMethod) => {
      const paidAmountControl = this.posForm.get('paidAmount');
      if (paymentMethod === PaymentMethod.CASH) {
        const invoiceTotal = this.calculateInvoiceTotal();
        paidAmountControl?.setValue(invoiceTotal, { emitEvent: false });
        paidAmountControl?.disable({ emitEvent: false });
      } else {
        paidAmountControl?.setValue(0, { emitEvent: false });
        paidAmountControl?.enable({ emitEvent: false });
      }
    });

    // Also update paidAmount when orderItems change if payment method is CASH
    this.posForm.get('orderItems')?.valueChanges.subscribe(() => {
      const paymentMethodId = this.posForm.get('paymentMethodId')?.value;
      if (paymentMethodId === PaymentMethod.CASH) {
        const invoiceTotal = this.calculateInvoiceTotal();
        this.posForm.get('paidAmount')?.setValue(invoiceTotal, { emitEvent: false });
      }
    });

    // Update paidAmount when delivery cost changes if payment method is CASH
    this.posForm.get('deliveryCost')?.valueChanges.subscribe(() => {
      const paymentMethodId = this.posForm.get('paymentMethodId')?.value;
      if (paymentMethodId === PaymentMethod.CASH) {
        const invoiceTotal = this.calculateInvoiceTotal();
        this.posForm.get('paidAmount')?.setValue(invoiceTotal, { emitEvent: false });
      }
    });

    // Prefill delivery address from the selected customer's saved address
    this.posForm.get('customerId')?.valueChanges.subscribe((customerId) => {
      const customer = this.getCustomerById(customerId);
      if (customer) {
        const savedAddress = customer.favoriteDeliveryAddress || customer.address;
        if (savedAddress) {
          this.posForm.get('deliveryAddress')?.setValue(savedAddress, { emitEvent: false });
        }
      }
    });
  }

  getCustomerById(customerId: number | null | undefined) {
    if (!customerId) return null;
    return this.customerService.customers().find(c => c.id === customerId) ?? null;
  }

  private calculateInvoiceTotal(): number {
    const orderItems = this.posForm.get('orderItems') as FormArray;
    const itemsTotal = orderItems.value.reduce((total: number, item: any) => {
      return total + (item.quantity * item.price);
    }, 0);
    const deliveryCost = this.posForm.get('customerType')?.value === CustomerType.ONLINE_CUSTOMER
      ? (this.posForm.get('deliveryCost')?.value ?? 0)
      : 0;
    return itemsTotal + deliveryCost;
  }






}
