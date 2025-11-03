# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PETEL Backend is a Spring Boot 3.5.6 application for a pet-friendly hotel booking platform. It supports multiple user roles (users, sellers/merchants, admins) with features including real-time chat, notifications, payment processing, and media management.

**Key Technologies:**
- Java 17
- Spring Boot 3.5.6 (Web, Security, Data JPA, WebSocket)
- Oracle Database (FREEPDB1)
- JWT Authentication (JJWT 0.11.5)
- AWS S3 (media storage)
- Redis (caching)
- ecPay (payment gateway)

## Common Commands

### Build & Run
```bash
# Clean and compile
./mvnw clean compile

# Package as WAR
./mvnw clean package

# Run application (default port 8080)
./mvnw spring-boot:run

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Testing
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=AuthControllerTest

# Run specific test method
./mvnw test -Dtest=AuthControllerTest#testRegister

# Run tests with coverage
./mvnw test jacoco:report
```

### Development
```bash
# Skip tests during build
./mvnw clean package -DskipTests

# Check for dependency updates
./mvnw versions:display-dependency-updates

# Format code (if formatter plugin configured)
./mvnw formatter:format
```

## Architecture

### Layered Structure
```
Controller → Service (Interface + Impl) → Repository → Database
```

All layers follow a transaction code pattern (XXX001, XXX002, etc.) where:
- **AUTH0XX**: Authentication & authorization
- **BOOK0XX**: Booking & orders
- **HOTEL0XX**: Hotel/property browsing
- **CHAT0XX**: Chat functionality
- **MEDIA0XX**: Media management
- **MERCH0XX**: Merchant operations
- **ADMIN0XX**: Admin functions
- **NOTIFY0XX**: Notification system
- **REVIEW0XX**: Reviews
- **USER0XX**: User profile
- **IMG0XX**: Image operations

### Service Interface Pattern
Every service follows interface-implementation separation:
- Interface: `AUTH001Svc.java`
- Implementation: `AUTH001SvcImpl.java`

### Standard Request/Response Format
All API endpoints use wrapper DTOs:

**Request:**
```java
Req<AUTH001Tranrq> request
├─ ReqMwHeader mwHeader    // Transaction metadata
└─ AUTH001Tranrq tranrq    // Actual payload
```

**Response:**
```java
Res<AUTH001Tranrs> response
├─ ResMwHeader mwHeader    // Return code & description
└─ AUTH001Tranrs tranrs    // Actual payload
```

### Database Layer Strategy
Hybrid approach using both JPA and native SQL:

**JPA Repositories** (`src/main/java/com/example/petel/repository/`)
- Standard CRUD operations
- Simple queries with Spring Data JPA methods
- 26 repositories total

**Native SQL Templates** (`src/main/resources/sql/`)
- Complex queries with dynamic WHERE clauses
- 37 .sql files with parameter placeholders
- Executed via `SqlAction.java` + `SqlUtils.java`

Example SQL usage:
```java
String sql = sqlUtils.getDynamicQuerySQL("CHAT002_QUERY_BUYER.sql", paramMap);
List<Map<String, Object>> results = sqlAction.queryForList(sql, paramMap);
```

## Authentication & Authorization

### JWT Configuration
**Location:** `src/main/java/com/example/petel/model/jwt/JwtUtil.java`

Token structure:
- **Access Token**: 15 minutes (900,000ms)
- **Refresh Token**: 30 days (2,592,000,000ms)
- **Claims:** `sub` (accountId), `typ` (token type), `email`, `role`, `tv` (token_version)

### Security Flow
1. Client sends credentials to `/auth/login`
2. Server validates and returns access + refresh tokens
3. Client includes token in `Authorization: Bearer <token>` header or `access_token` cookie
4. `JwtAuthFilter` validates token before every protected request
5. `AccountPrincipal` injected via `@AuthenticationPrincipal` in controllers

### Token Version Logout Mechanism
- Each account has a `token_version` field in database
- On logout, server increments `token_version`
- All previously issued tokens become invalid (version mismatch)
- No need to maintain token blacklist

### Protected Endpoints
All endpoints except:
- `/auth/login`, `/auth/register`, `/auth/refresh`, `/auth/forgot`, `/auth/reset`, `/auth/verify`
- `/ws*` (WebSocket endpoints have their own JWT validation via `WebSocketAuthInterceptor`)

## Real-Time Features

### WebSocket Chat
**Configuration:** `src/main/java/com/example/petel/configuration/WebSocketConfig.java`
**Controller:** `src/main/java/com/example/petel/controller/ChatWsController.java`

**Connection:**
```
ws://localhost:8080/ws
wss://localhost:8080/ws-native
```

**Authentication:**
- Include JWT in STOMP CONNECT frame headers
- `WebSocketAuthInterceptor` validates token before connection

**Message Mapping:**
- Send message: `/app/rooms/{threadId}/send`
- Mark read: `/app/rooms/{threadId}/read`
- Subscribe to room: `/topic/rooms/{threadId}`
- User notifications: `/user/queue/notifications`

### Server-Sent Events (SSE) Notifications
**Component:** `src/main/java/com/example/petel/component/NotificationHub.java`
**Controller:** `src/main/java/com/example/petel/controller/NotificationController.java`

**Subscription:**
```
GET /notifications/subscribe
```

**Notification Flow:**
1. Client opens SSE connection (`NotificationHub.register(accountId)`)
2. Service triggers notification (`NotificationHub.sendTo(accountId, eventName, eventId, payload)`)
3. Server pushes event to all SSE connections for that user
4. Connection auto-cleanup on timeout/disconnect

**Event Types:** SYSTEM, ORDER, PAYMENT, NOTIFICATION

## Media Management

### S3 Upload Process
**Configuration:** `src/main/java/com/example/petel/configuration/AwsConfig.java`
**Controller:** `src/main/java/com/example/petel/controller/S3Controller.java`

**Two-step upload:**
1. Request pre-signed URL: `POST /s3/signature`
   - Returns signed URL + required headers
   - Browser uploads directly to S3 (no backend involvement)
2. Confirm upload: `POST /media/upload` (MEDIA001)
   - Records metadata in database (`MediaEntity`, `MediaS3Entity`)

**Configuration (application.properties):**
```properties
cloud.aws.credentials.access-key=YOUR_AWS_ACCESS_KEY
cloud.aws.credentials.secret-key=YOUR_AWS_SECRET_KEY
cloud.aws.region.static=us-east-1
s3.bucket.name=YOUR_S3_BUCKET_NAME
petel.s3.public-base=https://YOUR_BUCKET.s3.us-east-1.amazonaws.com/
```

## Payment Integration

### ecPay Configuration
**Service:** `src/main/java/com/example/petel/service/impl/BOOK005SvcImpl.java`

**Payment Methods:**
- On-site payment (BOOK005)
- Credit card (BOOK006 - hosted payment page)

**Webhooks:**
- Credit card callback: `/bookings/credit/notify` (BOOK009)
- Authorization callback: `/bookings/authorize/notify`

**Configuration (application.properties):**
```properties
ecpay.merchantId=3002607
ecpay.hashKey=YOUR_HASH_KEY
ecpay.hashIv=YOUR_HASH_IV
ecpay.authorize.returnUrl=https://your-domain/bookings/authorize/notify
ecpay.credit.returnUrl=https://your-domain/bookings/credit/notify
ecpay.credit.clientBackUrl=http://localhost:4200/book/finish
```

**Note:** For local development, use ngrok to expose webhooks:
```bash
ngrok http 8080
# Update returnUrl in application.properties with ngrok URL
```

## Database

### Connection Configuration
**Location:** `src/main/resources/application.properties`

```properties
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/FREEPDB1
spring.datasource.username=system
spring.datasource.password=YOUR_PASSWORD
```

### Entity Relationships
Key entities located in `src/main/java/com/example/petel/entity/`:

```
AccountsEntity (auth credentials)
  └─→ UsersEntity (user profile)
  └─→ SellersEntity (merchant profile)

PropertyEntity (hotels)
  ├─→ RoomsEntity (room types)
  ├─→ PropertyImageEntity
  └─→ PropertyFacilitiesEntity

OrdersEntity (bookings)
  ├─→ OrderItemsEntity (line items)
  └─→ TransactionsEntity (payment records)

ChatThreadEntity (chat rooms)
  └─→ ChatMessagesEntity (messages)

NotificationsEntity
  └─→ NotificationEventsEntity
```

### ID Generation
All table IDs use `IdUtil.generateTableId(tableName, repository)`:
- Format: First letter of table name + 5-digit number (e.g., `A00001`, `O12345`)
- Auto-increments based on max ID in database

## Exception Handling

### Custom Exceptions
Located in `src/main/java/com/example/petel/exception/`:
- `DataNotFoundException`
- `InvalidInputException`
- `InsertFailException`
- `UpdateFailException`
- `DeleteFailException`
- `JwtProcessingException`
- `UnauthorizedException`
- `PaymentFailedException`
- `InvalidPaymentMethodException`
- `RefundFailException`

### Global Handler
**Location:** `src/main/java/com/example/petel/controller/advice/WebExceptionHandler.java`

Automatically maps exceptions to standardized error responses:
```json
{
  "MWHEADER": {
    "returnCode": "E001",
    "returnDesc": "Error description"
  },
  "TRANRS": null
}
```

## Code Conventions

### Service Implementation
When creating new services:
1. Create interface in `service/` (e.g., `BOOK014Svc.java`)
2. Create implementation in `service/impl/` (e.g., `BOOK014SvcImpl.java`)
3. Use `@Service` and `@RequiredArgsConstructor` (Lombok)
4. Follow transaction code naming (XXX001, XXX002, etc.)

### DTO Creation
1. Create request DTO: `BOOK014Tranrq.java`
2. Create response DTO: `BOOK014Tranrs.java`
3. Wrap in `Req<T>` and `Res<T>` in controller methods
4. Use Jakarta validation annotations (`@NotNull`, `@NotBlank`, etc.)

### Controller Pattern
```java
@RestController
@RequestMapping("/api-path")
@RequiredArgsConstructor
public class XxxController extends BaseController {

    private final XXX001Svc xxx001Svc;

    @PostMapping("/endpoint")
    public Res<XXX001Tranrs> method(
        @AuthenticationPrincipal AccountPrincipal auth,
        @RequestBody @Valid Req<XXX001Tranrq> req
    ) throws CustomException {
        return xxx001Svc.process(req);
    }
}
```

### Accessing Current User
Inject `AccountPrincipal` to get authenticated user info:
```java
public Res<XXX001Tranrs> method(@AuthenticationPrincipal AccountPrincipal auth) {
    String accountId = auth.getAccountId();      // User ID
    String role = auth.getRole();                // "user", "seller", or "admin"
    Integer tokenVersion = auth.getTokenVersion(); // For logout validation
}
```

## API Documentation

### Swagger/OpenAPI
Access interactive API docs at:
```
http://localhost:8080/swagger-ui.html
```

Configured via `springdoc-openapi-starter-webmvc-ui` dependency.

## Important Notes

### Security Considerations
- JWT secret stored in `application.properties` (should use environment variables in production)
- Database credentials in properties file (use secrets management in production)
- CORS currently limited to `http://localhost:4200` (see `SecurityConfig.java`)

### Lombok Usage
This project uses Lombok extensively:
- `@Data`, `@Getter`, `@Setter` for POJOs
- `@RequiredArgsConstructor` for dependency injection
- `@Builder` for complex object construction
- Ensure Lombok plugin is installed in your IDE

### Native SQL Files
When modifying SQL templates in `src/main/resources/sql/`:
- Use named parameters: `:paramName`
- Support dynamic clauses: `:QUERY_FIELD`, `:ORDER_BY`
- Oracle pagination: `OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY`
- No need to recompile after changes (loaded at runtime)

### Role-Based Access
Currently implemented at service level, not controller level:
- Services check `auth.getRole()` and execute different logic
- Example: `CHAT002Svc` has separate queries for buyers vs. sellers
- When adding features, consider role-specific behavior in service layer
