# Thaimei Ecommerce

Thaimei Ecommerce is a full-stack online shopping application with a Spring Boot REST API backend and a React/Vite frontend.

## Applications

- **Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA, MySQL, JWT, and Stripe
- **Frontend:** React 19, Vite, React Router, and Stripe JS

## Features

### Customers

- Register and log in
- Browse products and view product details
- Add products to a cart
- Place orders and make Stripe payments
- View orders and profile information
- Request returns and refunds

### Sellers

- Register and log in as a seller
- Manage store information
- Add and manage product listings
- View seller orders
- Update order statuses

### Administrators

- Log in through the admin portal
- Manage users, products, stores, and orders
- Approve stores
- Review and approve refunds
- Register additional administrators

## Project Structure

```text
ThaimeiEcommerce/
├── frontend/                 # React/Vite client
│   ├── src/
│   │   ├── components/
│   │   ├── lib/
│   │   └── pages/
│   ├── package.json
│   └── README.md             # Frontend-specific hosting notes
├── src/main/java/            # Spring Boot backend source
│   └── com/thaimei/myapp/
│       ├── configurations/   # Application, Stripe, and seed configuration
│       ├── controller/       # REST API controllers
│       ├── dto/              # API request and response types
│       ├── enums/             # Domain roles and statuses
│       ├── error/             # Exceptions and REST error handling
│       ├── model/             # JPA entities
│       ├── repository/        # Spring Data repositories
│       ├── security/          # JWT and Spring Security configuration
│       └── service/           # Business logic
├── src/main/resources/
│   └── application.properties
├── pom.xml                   # Maven backend configuration
└── README.md
```

## Backend

The backend is a stateless Spring Boot REST API. It is divided into controllers, services, repositories, JPA models, DTOs, security components, configuration, and centralized error handling.

The API covers customer, seller, and administrator operations for authentication, products, stores, carts, orders, payments, refunds, profiles, and administration.

Spring Security uses JWT authentication. Administrator and seller endpoints require their respective roles, while login, registration, and the Stripe webhook are public routes.

## Configuration

The backend reads configuration from environment variables and an optional local `.env` file. Configure the following values before starting the application:

```env
JWT_SECRET=your_jwt_secret
STRIPE_PUBLISHABLE_KEY=your_stripe_publishable_key
STRIPE_SECRET_KEY=your_stripe_secret_key
STRIPE_WEBHOOK_SECRET=your_stripe_webhook_secret
ADMIN_SEED_USERNAME=admin
ADMIN_SEED_EMAIL=admin@example.com
ADMIN_SEED_PASSWORD=your_admin_password
DATASOURCE_URL=jdbc:mysql://localhost:3306/thaimei_db
DATASOURCE_USERNAME=root
DATASOURCE_PASSWORD=your_db_password
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://127.0.0.1:5173
```

Do not commit real credentials, JWT secrets, database passwords, or Stripe secret keys.

The configuration is defined in `src/main/resources/application.properties`. The application currently uses Hibernate's `ddl-auto=update` setting, which updates the database schema from the JPA entities at startup. Review this setting before production deployment.

## Running Locally

Make sure MySQL is running and the backend environment variables are configured.

### Start the backend

From the `ThaimeiEcommerce` directory:

```bash
mvn spring-boot:run
```

### Start the frontend

Open a second terminal and run:

```bash
cd frontend
npm install
npm run dev
```

The frontend normally runs at `http://localhost:5173`.

The frontend API base URL and backend CORS origins must be configured for compatible origins. See `frontend/README.md` for frontend production hosting and proxy options.

## Build and Test

### Backend

```bash
mvn clean package
mvn test
```

### Frontend

```bash
cd frontend
npm run build
```

## Important Backend Files

- `src/main/java/com/thaimei/myapp/MyappApplication.java`: Spring Boot entry point
- `src/main/java/com/thaimei/myapp/security/SecurityConfig.java`: authentication, authorization, CORS, and security filter setup
- `src/main/java/com/thaimei/myapp/security/JwtAuthenticationFilter.java`: JWT request authentication
- `src/main/java/com/thaimei/myapp/configurations/DataSeeder.java`: initial administrator seeding
- `src/main/java/com/thaimei/myapp/configurations/StripeConfig.java`: Stripe configuration
- `src/main/resources/application.properties`: environment-backed application settings

## Admin Account Seeding

When the application starts, `DataSeeder` creates an administrator if the configured admin email does not already exist. The seed credentials come from the `ADMIN_SEED_USERNAME`, `ADMIN_SEED_EMAIL`, and `ADMIN_SEED_PASSWORD` environment variables.

## License

No license file is currently included in this project.
