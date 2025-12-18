# Digital Wallet & Split Bill System — Backend

A microservices-based backend for a digital wallet and split-bill application. It supports user authentication, wallet top-ups, payments, split-bill settlement, transaction history, and notifications. Services are designed to run independently, communicate via REST and event-driven messaging (Kafka optional), and are discoverable using Eureka.

Badges
- Build: (add CI badge)
- License: (add license badge)
- Coverage: (add coverage badge)

Table of Contents
- About
- Architecture & Services
- Key Features
- Tech Stack
- Getting Started
    - Prerequisites
    - Running Locally (Maven)
    - Running with Docker Compose
    - Environment Variables
- API Overview & Examples
- Data Model & Events
- Testing
- Development Tips
- Future Enhancements
- Contributing
- License
- Contact

About
This repository contains the backend microservices for a Digital Wallet and Split Bill System. Each service focuses on a single responsibility (Auth, User, Wallet, Payment, Transaction, Notification, SplitBill), enabling independent development, deployment, and scaling.

Architecture & Services
High-level flow:
- Client -> API Gateway -> appropriate microservice
- Services discover each other via Eureka
- Services emit domain events (via Kafka or an internal event bus) consumed by Notification and Transaction services

Core services:
1. API Gateway
    - Single entry point and routing
    - JWT validation, rate limiting, centralized logging & error handling

2. Service Registry (Eureka)
    - Service discovery and health checks
    - Keeps track of available instances for load balancing

3. Auth Service
    - Signup, login, password hashing, JWT generation and refresh tokens

4. User Service
    - User profiles, avatars, contact information, preferences

5. Wallet Service
    - Create wallets, top-ups, balance checks, debits, event emission on changes

6. Payment Service
    - Payment initiation, settlement of split bills, wallet-to-wallet transfers,
      emits events on payment results

7. Transaction Service
    - Stores every financial event (top-ups, payments, refunds), provides history and auditing

8. Notification Service
    - Stores and serves notifications; consumes events to notify users (email/push can be integrated)

9. Split Bill Service
    - Group creation, expense addition, split calculation (equal, percentage, custom),
      computes who owes whom and integrates with Payment Service for settlements

Key Features
- Microservices with clear separation of concerns
- JWT-based authentication
- Wallet lifecycle: top-up, payment, settlement
- Split-bill with multiple splitting strategies (equal, percentage, custom)
- Transaction ledger for audits and user statements
- Event-driven notifications (Kafka optional)
- Dockerized services for local development

Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Cloud Gateway
- Spring Cloud Eureka
- MySQL (or compatible RDBMS)
- Kafka (optional)
- Docker & Docker Compose
- Maven

Getting Started

Prerequisites
- Java 17 (JDK)
- Maven 3.6+
- Docker & Docker Compose (optional but recommended for multi-service local runs)
- MySQL (or use Dockerized MySQL)
- (Optional) Kafka (if you want to enable async events)

Run services locally (individual)
1. Start Eureka:

   cd eureka-server
   mvn spring-boot:run

2. Start API Gateway:

   cd api-gateway
   mvn spring-boot:run

3. Start the other services (examples):

   cd auth-service
   mvn spring-boot:run

   cd ../user-service
   mvn spring-boot:run

   cd ../wallet-service
   mvn spring-boot:run

Repeat for payment-service, transaction-service, notification-service, splitbill-service.

Run with Docker Compose (recommended for local end-to-end)
- Add a docker-compose.yml at repository root that starts:
    - eureka server
    - api gateway
    - mysql
    - kafka (optional)
    - each microservice (or use images you build locally)
- Example (high-level):

  docker-compose up -d

(If you'd like, I can create a sample docker-compose.yml for this repo.)
Environment Variables (examples)
Each service supports configuration via environment variables or application.yml:
- DATABASE_URL / spring.datasource.url
- DATABASE_USER / spring.datasource.username
- DATABASE_PASSWORD / spring.datasource.password
- EUREKA_CLIENT_SERVICEURL_DEFAULT_ZONE (Eureka server URL)
- JWT_SECRET (Auth Service)
- KAFKA_BOOTSTRAP_SERVERS (if using Kafka)
- SERVER_PORT

API Overview & Examples
Auth Service:
- POST /auth/signup
    - Body: { "email": "...", "password": "...", "name": "..." }
- POST /auth/login
    - Body: { "email": "...", "password": "..." }
    - Response: { "accessToken": "...", "refreshToken": "..." }

User Service:
- GET /user/{id}
- PUT /user/{id} (update profile)

Wallet Service:
- GET /wallet/{userId}
- POST /wallet/add
    - Body: { "userId": "...", "amount": 100.00, "source": "CARD" }

Payment Service:
- POST /payment/transfer
    - Body: { "fromUserId": "...", "toUserId": "...", "amount": 10.0 }
- POST /payment/settle
    - Body: { "groupId": "...", "payerId": "...", "amount": 20.0 }

Transaction Service:
- GET /transactions/user/{id}

Notification Service:
- GET /notifications/{userId}
- PATCH /notifications/read/{id}

Split Bill Service:
- POST /group/create
    - Body: { "name": "Trip", "members": ["user1","user2"] }
- POST /group/{id}/add-member
- POST /expense/add
    - Body: { "groupId": "...", "payerId": "...", "amount": 120, "splitType": "EQUAL|PERCENTAGE|CUSTOM", "splits": [...] }
- GET /group/{id}/balances

Sample curl
- Login:

  curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"password"}'

- Add money to wallet:

  curl -X POST http://localhost:8080/wallet/add \
  -H "Content-Type: application/json" \
  -d '{"userId":"user1","amount":50.0}'

Data Model & Events (summary)
- Wallet events: WALLET_TOPUP, WALLET_DEBIT
- Payment events: PAYMENT_INITIATED, PAYMENT_SUCCESS, PAYMENT_FAILED
- Notification events: NOTIFICATION_CREATED
- Transaction records include: id, amount, fromUser, toUser, status, method, timestamp

Testing
- Unit tests:

  mvn test

- Integration tests:
    - Configure testcontainers or a test DB and run with:

      mvn -DskipITs=false verify

Development Tips
- Use Eureka dashboard (http://localhost:8761) to verify service registration
- To debug inter-service calls, inspect API Gateway logs and service instance logs
- Centralize common DTOs and events in a shared module (if not present) to reduce duplication
- Add OpenAPI/Swagger to each service for easier API exploration

Future Enhancements
- Email & SMS alerts integration
- Push notifications (Firebase/Apple Push)
- OCR-based bill scanning to import expenses
- AI-driven budgeting and expense categorization
- Multi-currency support & exchange rates
- Webhook support for external integrations

Contributing
Contributions are welcome — please open issues or PRs. Suggested workflow:
- Fork the repository
- Create a feature branch: git checkout -b feat/some-feature
- Implement changes and add tests
- Open a PR describing the change

License
- (Add license header — e.g., MIT) — include LICENSE file in repo

Contact
- Maintainer: shivamdhakad14
- For questions or help, open an issue or contact the maintainer via GitHub.

Changelog
- v0.1 — Initial microservices layout and documentation

---

Notes:
- This README has been reorganized for clarity, added quickstart steps, examples, environment guidance, and a development checklist.
- If you'd like, I can:
    - create a docker-compose.yml for local end-to-end runs,
    - add OpenAPI docs and sample Postman collection,
    - or open a PR with this updated README directly to your repository.