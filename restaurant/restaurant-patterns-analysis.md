# Restaurant Project - Code Patterns Analysis

## Architecture Overview
- **Spring Boot 3.x** with **Spring Security 7.x**
- **MySQL** database with JPA/Hibernate
- Modular structure: `/modules/<feature>/` (auth, settings, foodManagement, account)
- Standard layering: Controller → Service → Repository with DTOs and Mappers

## Entity Patterns

### BaseEntity (MappedSuperclass)
- Abstract base class used by audit-tracked entities
- Fields: `id`, `createdAt`, `updatedAt`, `deletedAt`, `createdBy`, `updatedBy`, `deletedBy`
- Uses `@EntityListeners(AuditingEntityListener.class)` for auto-auditing
- Uses `@CreatedBy`, `@LastModifiedBy`, `@CreatedDate`, `@LastModifiedDate` annotations
- Has `@PreRemove` hook that soft-deletes records

### Settings Module Entities
- **Floor**: Simple entity (id, name) - does NOT extend BaseEntity
- **Kitchen**: id, name, ipAddress, port, status (Boolean, default true)
- **RestaurantTable**: id, name, capacity, icon, status, floorId (FK to Floor)
- **UnitOfMeasurement**: id, name, shortName, status

Note: Settings entities use simple entities without BaseEntity inheritance.

## DTO Patterns
- Use `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- Include validation annotations: `@NotBlank`, `@NotNull` with messages
- Mirror entity structure but include related entity IDs/names
- Example: TableDto includes `floorId` (FK), `floorName` (for display)

## Mapper Patterns (@Component)
- Simple static mapping between entities and DTOs
- Methods: `toDto(Entity)` → `Dto`, `toEntity(Dto, ...dependencies)` → `Entity`
- TableMapper: `toEntity(TableDto, Floor)` requires related entity
- Handle null safety and default values (e.g., status defaults to true)

## Service Patterns (@Service, @RequiredArgsConstructor)
- Methods: `create(Dto)`, `getById(Long)`, `getAll()`, `update(Long, Dto)`, `delete(Long)`
- All throw `ResourceNotFoundException` for missing entities
- Use dependency injection via constructor for Repo + Mapper
- Validate related entities exist before using them (e.g., Floor lookup in TableService)

## Controller Patterns (@RestController, @RequiredArgsConstructor)
- Use `@RequestMapping("/api/settings/<resource>")` routing
- All methods return `ResponseEntity<ApiResponse>`
- Standard CRUD endpoints: POST (create), GET /{id}, GET, PUT /{id}, DELETE /{id}
- Use `permissionChecker.check*()` methods for authorization
- Use `@Value` to inject menuId from app.properties
- Return appropriate HTTP statuses (CREATED for POST, OK for GET/PUT/DELETE)

## Permission System (PermissionChecker)
- Injected in controllers via `@RequiredArgsConstructor`
- Methods: `checkPermission(menuId, action)`, `checkRead/Create/Edit/Delete(menuId)`
- Admin role (ROLE_ADMIN) has full access
- Others need `MENU_<id>_<ACTION>` authority (e.g., MENU_1_READ)
- Menu IDs are configured in application.properties: floor-id=1, table-id=2, kitchen-id=3, uom-id not found

## ApiResponse Pattern
- Static methods: `success(message, data, code)`, `error(message, data, code)`
- Fields: status (boolean), message, data (Object), code (int - HTTP status)
- Returns `ResponseEntity<ApiResponse>` with appropriate HTTP status

## Exception Handling
- Custom exceptions extend RuntimeException: BadRequestException, ForbiddenException, ResourceNotFoundException, UnauthorizedException, InternalServerErrorException
- GlobalExceptionHandler catches each and returns ApiResponse with status code
- Handles MethodArgumentNotValidException for bean validation errors

## FileUtility Pattern
- @Component with configuration value: `app.files.base-dir=uploads`
- Saves MultipartFile with UUID filename, preserves extension
- Validates: no null/empty, no path traversal (..), safe location
- Returns path like `/files/food/<uuid>.avif`

## Application Configuration
```properties
spring.application.name=restaurant
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/restaurant_db
spring.jpa.hibernate.ddl-auto=update
app.jwt.secret=<base64-encoded>
app.jwt.access-token-expiration-ms=86400000
app.cors.allowed-origin=http://localhost:4200
app.files.base-dir=uploads
app.menu.<resource>-id=<numeric-id>
```

## Key Patterns to Follow
1. Entities don't always extend BaseEntity
2. DTOs always have validation annotations
3. Mappers are @Component (Spring-managed)
4. Services handle business logic + exception throwing
5. Controllers use permission checks + consistent response format
6. All DTOs use Lombok annotations
7. Services depend on mapper + repository
8. Controllers depend on service + permissionChecker
