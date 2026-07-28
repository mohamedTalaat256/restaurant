/* 
in settings module inside java.com.mtalaat.resturent 

i want to create a new entity called Kitchen with the following attributes:
- id (primary key, auto-generated)
- name (string, required)
- ipAddress (string, optional)
- port (integer, optional)
- status (boolean, default true)
 


create a jpa repository and service and controller (add, create, update, delete, getAll, getById) and Dtos and mappers 
use the pre defined custome exceptions
use the pre defined response wrapper for all responses 
in each respons emessage : the message will be in format like "msg_Kitchen_created"



note: use PermissionChecker class in the controller


*/



/* 
in foodManagement module inside java.com.mtalaat.resturent 


i want to create a new entity called ItemFoodVarient will extends com.mtalaat.restaurant.entity.BaseEntity  with the following attributes:
    - id (primary key, auto-generated)
    - name (string, required)
    - price (double, required)
    - itemFood (many-to-one relationship with ItemFood)


i want to create a new entity called ItemFoodAddOns will extends com.mtalaat.restaurant.entity.BaseEntity  with the following attributes:
    - id (primary key, auto-generated)
    - name (string, required)
    - price (double, required)
    - status (boolean, default true)
 

i want to create a new entity called ItemFoodAddOnsAssociation will extends com.mtalaat.restaurant.entity.BaseEntity  with the following attributes:
    - id (primary key, auto-generated)
    - itemFood (many-to-one relationship with ItemFood)
    - itemFoodAddOns (many-to-one relationship with ItemFoodAddOns)    
     
 

create for each entity: jpa repository and service and controller (add, create, update, delete, getAll, getById) and Dtos and mappers 
use the pre defined custome exceptions
use the pre defined response wrapper for all responses 
in each respons emessage : the message will be in format like "msg_example_created"



note: note: use PermissionChecker class in each controller 





*/


/* 

please handel image upload in controllers and services (itemCategory, ItemFood, MenuType) in create and update
and use FileUtility class to save image and return the path
also create a new upload folder like users for saving these images inside the main uploads folder and use it in the FileUtility class when saving


use same code style in package com.mtalaat.restaurant.modules.auth.service.UserService; 
and com.mtalaat.restaurant.modules.auth.controller.UserController; 


insert into restaurant_db.menu_items
(page_url, title, module_id, is_page) values
('item-food-variant', 'item_food_variants', 5, b'1'),
('item-food-add-ons', 'item_food_add_ons', 5, b'1'),
('item-food-add-ons-association', 'item_food_add_ons_association', 5, b'1');


*/

/* application settings */

/* 
in settings module inside java.com.mtalaat.resturent 

i want to create a new entity called LanguageTranslation with the following attributes:
    - id (primary key, auto-generated)
    - language (many-to-one relationship with Language)
    - key (string, required)
    - value (string, required)

i want to create a new entity called Language with the following attributes:
    - id (primary key, auto-generated)
    - name (string, required)
    - isDefault (boolean, default false)

i want to create a new entity called Currency with the following attributes:
    - id (primary key, auto-generated)
    - code (string, required)
    - symbol (string, required)
    - name (string, required)
    - exchangeRateToUSD (double, required)

i want to create a new entity called ApplicationSetting with the following attributes:
    - id (primary key, auto-generated)
    - applicationTitle (string, required)
    - storeName (string, required)
    - address (string, optional)
    - phone (string, optional)
    - icon (string, optional) //will save the path of the icon image
    - logo (string, optional) //will save the path of the logo image
    - oppingtime (string, optional)
    - closingTime (string, optional)
    - discountType [enum: PERCENTAGE, FIXED_AMOUNT]
    - discountPercentage (double, optional)
    - serviceChargeType [enum: PERCENTAGE, FIXED_AMOUNT]
    - taxPercentage (double, optional)
    - taxNumber (string, optional)
    - currency (many-to-one relationship with Currency) 
    - language (many-to-one relationship with Language)
    - dateFormat (string, optional)
    - timezone (string, optional)
    - applicationDirection [enum: LTR, RTL]
    - poweredByText (string, optional)
    - foorterText (string, optional)

 


create a jpa repository and service and controller (add, create, update, delete, getAll, getById) and Dtos and mappers 
use the pre defined custome exceptions in src.main.java.com.mtalaat.restaurant.exceptions
use the pre defined response wrapper for all responses in src.main.java.com.mtalaat.restaurant.payload.ApiResponse
in each respons emessage : the message will be in format like "msg_ApplicationSetting_created"

ApplicationSettingController should handle image upload for icon and logo using FileUtility class and save the images in a new folder called "settings" inside the main uploads folder.

note: use PermissionChecker class in the controller and add the menu id in application.properties # Menu IDs for permission checks

insert into restaurant_db.menu_items
(page_url, title, module_id, is_page) values
('languages', 'languages', 5, b'1'),
('language-translation', 'language_translation', 5, b'1'),
('currencies', 'currencies', 5, b'1'),
('application-settings', 'application_settings', 5, b'1');



*/


/* 
    in settings module inside java.com.mtalaat.resturent

i want to create a new entity called PaymentMethod with the following attributes:
    - id (primary key, auto-generated)
    - name (string, required)
    - description (string, optional)
    - status (boolean, default true)

i want to create a new entity called CustomerType (have only 4 rows [Walk_IN_CUSTOMER, ONLINE_CUSTOMER, THIRD_PARTY_CUSTOMER, TAKEAWAY_CUSTOMER]) with the following attributes:
    - type (primary key, [Walk_IN_CUSTOMER, ONLINE_CUSTOMER, THIRD_PARTY_CUSTOMER, TAKEAWAY_CUSTOMER])
    - description (string, optional)
    - ordering (integer, required)
    - status (boolean, default true)

i want to create a new entity called Customer with the following attributes:
    - id (primary key, auto-generated)
    - name (string, required)
    - email (string, required)
    - phone (string, required)
    - address (string, optional)
    - favoriteDeliveryAddress (string, optional)
    - password (string, optional)
    - customerType (many-to-one relationship with CustomerType)
    - status (boolean, default true)

i want to create a new entity called ThirdPartyCustomer with the following attributes:
    - id (primary key, auto-generated)
    - name (string, required)
    - address (string, optional)
    - phone (string, optional)
    - email (string, optional)
    - commionPercentage (double, required)


create a jpa repository and service and controller (create, update, delete, getAll, getById) and Dtos and mappers 
use the pre defined custome exceptions in src.main.java.com.mtalaat.restaurant.exceptions
use the pre defined response wrapper for all responses in src.main.java.com.mtalaat.restaurant.payload.ApiResponse
in each respons emessage : the message will be in format like "msg_payment_method_created"

note: use PermissionChecker class in the controller and add the menu id in application.properties # Menu IDs for permission checks
use: restaurant-patterns-analysis.md and content.txt in root folder for more details about the design patterns and code style to use in this module


insert into restaurant_db.menu_items
(page_url, title, module_id, is_page) values
('payment-methods', 'payment_methods', 3, b'1'),
('customer-types', 'customer_types', 3, b'1'),
('customers', 'customers', 3, b'1'),
('third-party-customers', 'third_party_customers', 3, b'1');

*/




/* 
    in settings module inside java.com.mtalaat.resturent

i want to create a new entity called Supplier that will extends BaseEntity with the following attributes:
    - id (primary key, auto-generated)
    - name (string, required)
    - email (string, optional)
    - phone (string, optional)
    - address (string, optional)
    - status (boolean, default true)

i want to create a new entity called Ingredient with the following attributes:
    - id (primary key, auto-generated)
    - name (string, required)
    - uom relationship with UnitOfMeasurement
    - stockQuantity (double, required)
    - minStockQuantity (double, required)
    - status (boolean, default true)

i want to create a new entity called Purchase that will extends BaseEntity with the following attributes:
    - id (primary key, auto-generated)
    - invoiceNumber (string, required)
    - paymentMethod (many-to-one relationship with PaymentMethod)
    - supplier (many-to-one relationship with Supplier)
    - purchaseDate (date, required)
    - expiryDate (date, optional)
    - totalAmount (double, required)
    - paidAmount (double, required)
    - note (string, optional)

i want to create a new entity called PurchaseItem that will extends BaseEntity with the following attributes:
    - id (primary key, auto-generated)
    - purchase (many-to-one relationship with Purchase)
    - ingredient (many-to-one relationship with Ingredient)
    - quantity (double, required)
    - price (double, required)


create a jpa repository and service and controller (create, update, delete, getAll, getById) and Dtos and mappers 
use the pre defined custome exceptions in src.main.java.com.mtalaat.restaurant.exceptions
use the pre defined response wrapper for all responses in src.main.java.com.mtalaat.restaurant.payload.ApiResponse
in each respons emessage : the message will be in format like "msg_purchase_item_created"

note: use PermissionChecker class in the controller and add the menu id in application.properties # Menu IDs for permission checks
use: restaurant-patterns-analysis.md and content.txt in root folder for more details about the design patterns and code style to use in this module


insert into restaurant_db.menu_items
(page_url, title, module_id, is_page) values

('suppliers', 'suppliers', 6, b'1'),
('ingredients', 'ingredients', 6, b'1'),
('purchases', 'purchases', 6, b'1');

*/





/* 
    in order module inside java.com.mtalaat.resturent

i want to create a new entity called CashCounter with the following attributes:
    - id (primary key, auto-generated)
    - number (int, required)

i want to create a new entity called CashRegister with the following attributes:
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

create a jpa repository and service and controller (create, update, delete, getAll, getById) and Dtos and mappers 
use the pre defined custome exceptions in src.main.java.com.mtalaat.restaurant.exceptions
use the pre defined response wrapper for all responses in src.main.java.com.mtalaat.restaurant.payload.ApiResponse
in each respons emessage : the message will be in format like "msg_text_created"

note: use PermissionChecker class in the controller and add the menu id in application.properties # Menu IDs for permission checks
use: restaurant-patterns-analysis.md and content.txt in root folder for more details about the design patterns and code style to use in this module


in CashRegisterController 
1- maka a function that check if there is an open cash 
register for th authenticated user and return its details if found or return a message if not found

2- make a function to open a cash register for the authenticated user,
 but before opening check if there is already an open cash register for this user and return a message if found,
if not found then open the cash register and return its details
the dto of the function should only contain cashCounterId, openingBalance and openingNote


3- make a function to close a cash register for the authenticated user,
but before closing check if there is an open cash register for this user and return a message if not found,
if found then close the cash register and return its details
the dto of the function should only contain closingBalance and closingNote
 

*/


/*  */