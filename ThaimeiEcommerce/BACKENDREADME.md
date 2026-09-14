# Thaimei Ecommerce Backend

Spring Boot backend for the Thaimei e-commerce application. It includes customer, seller, and admin flows, JWT-based authentication, MySQL persistence, and Stripe payment integration.

## Tech Stack

- Java 21
- Spring Boot 3.5.6
- Spring Security
- Spring Data JPA
- MySQL 8
- JWT (jjwt)
- Stripe Java SDK
- Maven
- Docker + Docker Compose

## Project Structure

- src/main/java/com/thaimei/myapp/
  - controller/
  - service/
  - repository/
  - model/
  - dto/
  - security/
  - config/
  - enums/
  - error/
- src/main/resources/application.properties

## Features

### Customer Features
- Sign up and login
- Product listing and product detail viewing
- Search products by keyword/category
- Add items to cart
- Checkout and place orders
- View order history
- View and update profile
- Request refunds
- Payment retrieval and webhook processing

### Seller Features
- Seller registration and login
- Create and manage stores
- Add products
- View products and orders
- Manage seller-specific operations

### Admin Features
- Admin login
- Manage customers and sellers
- Review admin orders
- Handle product approvals / admin operations
- Refund management

## Authentication and Authorization

This backend uses Spring Security with JWT authentication.

- Public routes allow signup/login endpoints.
- Customer routes are protected for authenticated customer users.
- Seller routes require SELLER role.
- Admin routes require ADMIN role.
- Stripe webhook endpoint is public for payment callbacks.

## Environment Variables

Create a .env file in the project root with the following values:

```env
JWT_SECRET=your_jwt_secret
STRIPE_PUBLISHABLE_KEY=your_publishable_key
STRIPE_SECRET_KEY=your_secret_key
STRIPE_WEBHOOK_SECRET=your_webhook_secret
ADMIN_SEED_USERNAME=admin
ADMIN_SEED_EMAIL=admin@example.com
ADMIN_SEED_PASSWORD=admin_password
DATASOURCE_URL=jdbc:mysql://localhost:3306/thaimei?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DATASOURCE_USERNAME=thaimei
DATASOURCE_PASSWORD=your_db_password
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://127.0.0.1:5173
```

## Database Configuration

The app uses MySQL with JPA and Hibernate.

```properties
spring.datasource.url=${DATASOURCE_URL}
spring.datasource.username=${DATASOURCE_USERNAME}
spring.datasource.password=${DATASOURCE_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

## Run Locally

### Option 1: Local Maven

```bash
./mvnw clean install
./mvnw spring-boot:run
```

### Option 2: Docker Compose

```bash
docker compose up --build
```

This starts:
- MySQL database on port 3306
- Backend on port 8080

## Backend Ports

- App: http://localhost:8080
- MySQL: localhost:3306

## Base API Routes

### Customer API
- /customers/signup
- /customers/login
- /customers/profile-info
- /customers/profile
- /customers/productlist
- /customers/details/{id}
- /customers/products/search
- /customers/search/store
- /Cart/getItems
- /Cart/AddItems
- /customers/buyNowCheckout
- /customers/CartCheckout
- /customers/GetOrder
- /customer/requestRefund
- /customer/getRefunds
- /payment/customer/{paymentId}
- /payment/webhook

### Seller API
- /sellers/registration
- /sellers/sellerLogin
- /sellers/addBusiness
- /sellers/getStoresForSeller
- /sellers/addProducts
- /sellers/getProducts
- /sellers/orders
- /sellers/store/{storeId}

### Admin API
- /admin/api/adminlogin
- /admin/api/customers/sellers
- /admin/api/searchSellers
- /admin/api/getAllStoresBySeller/{sellerId}
- /admin/api/seller/{userId}
- /admin/api/adminOrders
- /admin/api/sellerOrdersForAdmin/{sellerId}
- /admin/refund/pullrefund

## Security Notes

- JWT tokens are used for sessionless auth.
- Passwords are encoded using BCrypt.
- CORS is configured for the frontend local development origin.
- Stripe webhook signatures are verified before processing events.

## Stripe

The backend handles payment events such as:
- payment_intent.succeeded
- payment_intent.payment_failed
- charge.refunded

These are verified via webhook signatures and persisted through the payment service layer.

## Useful Commands

```bash
./mvnw test
./mvnw clean package
./mvnw spring-boot:run
```

## Troubleshooting

- If database connection fails, verify MySQL is running and .env values are correct.
- If JWT auth fails, confirm JWT_SECRET is set and valid.
- If frontend cannot call backend, check CORS_ALLOWED_ORIGINS and backend security config.
- If Stripe webhook fails, verify STRIPE_WEBHOOK_SECRET and request signature.

## Notes

This backend is designed to support the React/Vite frontend in the frontend/ folder and is intended to run alongside it in local development.

## License

This project is currently configured as a local application project without a formal external license declaration.
