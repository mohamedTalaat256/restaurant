/*


Supplier that has the following attributes:
    - id (primary key, auto-generated)
    - name (string, required)
    - email (string, optional)
    - phone (string, optional)
    - address (string, optional)
    - status (boolean, default true)

Ingredient that has the following attributes:
    - id (primary key, auto-generated)
    - name (string, required)
    - uom relationship with UnitOfMeasurement
    - stockQuantity (double, required)
    - minStockQuantity (double, required)
    - status (boolean, default true)

Purchase that has the following attributes:
    - id (primary key, auto-generated)
    - invoiceNumber (string, required)
    - paymentMethod (many-to-one relationship with PaymentMethod)
    - supplier (many-to-one relationship with Supplier)
    - purchaseDate (date, required)
    - expiryDate (date, optional)
    - totalAmount (double, required)
    - paidAmount (double, required)
    - note (string, optional)

PurchaseItem that has the following attributes:
    - id (primary key, auto-generated)
    - purchase (many-to-one relationship with Purchase)
    - ingredient (many-to-one relationship with Ingredient)
    - quantity (double, required)
    - price (double, required)


    if i have created the following entities and controllers in backend in springboot
    now to want to create the frontend components in src/app/modules/admin/purchases folder

    use code base style and pattern from users component and user.service.ts in src/app/modules/admin/user-and-permissons folder

    add routes in admin-routing.module.ts
    add en translations in en.json file in public/i18n folder
    use i18n naming convention for translation keys (e.g. label_payment_method_settings, msg_payment_method_updated_successfully, etc.)
    use showIfCanCreate, showIfCanEdit, showIfCanDelete as found in users component


    */


/*
    in order folder in src/app/modules/admin

    i want to create a new component

CashCounter with the following attributes:
    - id (primary key, auto-generated)
    - number (int, required)

CashRegister with the following attributes:
    - id (primary key, auto-generated)
    - user (many-to-one relationship with User)
    - cashCounter (many-to-one relationship with CashCounter)
    - openingBalance (double, required)
    - closingBalance (double, optional)
    - openingTime (datetime, required)
    - closingTime (datetime, optional)
    - openingNote (string, optional)
    - closingNote (string, optional)
    - status (boolean, default true)


in CashCounter use same style and pattern of tables and components as in the kitchens component in src/app/modules/admin/kitchens folder

in CashRegister:
make 2 components
  CashRegister list
  CashRegister dialog


  in CashRegister service make a function to check for existing open cash register for the current user and if exists return it otherwise create a new one and return it

  api/cash/registers

   @GetMapping("/my-open")
    public ResponseEntity<ApiResponse> getMyOpenCashRegister() {
        User currentUser = permissionChecker.getCurrentUser();
        Optional<CashRegisterDto> openRegister = cashRegisterService.getOpenCashRegister(currentUser);
        HttpStatus status = HttpStatus.OK;
        if (openRegister.isPresent()) {
            return ResponseEntity.status(status)
                    .body(ApiResponse.success("msg_cash_register_fetched", openRegister.get(), status.value()));
        }
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_no_open_cash_register", null, status.value()));
    }

 and make a function to open and close

     @PostMapping("/open")
    public ResponseEntity<ApiResponse> openCashRegister(@Valid @RequestBody OpenCashRegisterDto dto) {
        permissionChecker.checkCreate(menuId);
        User currentUser = permissionChecker.getCurrentUser();
        CashRegisterDto opened = cashRegisterService.openCashRegister(dto, currentUser);
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_cash_register_opened", opened, status.value()));
    }

    @PostMapping("/close")
    public ResponseEntity<ApiResponse> closeCashRegister(@Valid @RequestBody CloseCashRegisterDto dto) {
        permissionChecker.checkEdit(menuId);
        User currentUser = permissionChecker.getCurrentUser();
        CashRegisterDto closed = cashRegisterService.closeCashRegister(dto, currentUser);
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponse.success("msg_cash_register_closed", closed, status.value()));
    }

    @Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenCashRegisterDto {

    @NotNull(message = "Cash counter is required")
    private Long cashCounterId;

    @NotNull(message = "Opening balance is required")
    private Double openingBalance;

    private String openingNote;
}

public class CloseCashRegisterDto {

    @NotNull(message = "Closing balance is required")
    private Double closingBalance;

    private String closingNote;
}

i provided the backend code for cash register open and close apis, now you need to implement the frontend components and services for cash register in src/app/modules/admin/orders folder



*/
