# Banking Microservice API

A Java Spring Boot REST API that simulates basic banking operations, including creating accounts, depositing money, withdrawing money, and transferring money between accounts.

This project demonstrates my ability to develop backend services, connect applications to relational databases, and implement business logic using Java.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java | Main programming language |
| Spring Boot | Backend application framework |
| Spring Data JPA | Database access and persistence |
| Hibernate | Maps Java objects to database tables |
| PostgreSQL | Stores banking accounts and transactions |
| Maven | Builds the application and manages dependencies |
| REST API | Allows other applications to communicate with the backend |
| Git & GitHub | Source control and project hosting |

## What the Application Does

The banking application supports four main operations:

**1. Create a Bank Account**

Creates a checking or savings account with a unique account number and an initial balance of $0.00.

**2. Deposit Money**

Allows money to be added to an account and updates the balance in PostgreSQL.

**3. Withdraw Money**

Allows money to be withdrawn from an account. The application checks whether enough money is available before processing the withdrawal.

**4. Transfer Money**

Moves money between two accounts. The application updates both account balances and records the transfer.

The application also supports retrieving account information and balances.

## How the Application Works

The backend is divided into four main layers:

### 1. Controller Layer

**File:** `AccountController.java`

The controller receives HTTP requests from clients, such as a React frontend.

For example, when someone requests account information, the controller receives the request and passes it to the appropriate backend logic.

### 2. Service Layer

**File:** `AccountService.java`

The service contains the banking business rules.

It handles operations such as:

- Creating accounts
- Processing deposits
- Processing withdrawals
- Checking available balances
- Transferring money between accounts
- Recording transactions

For example, before withdrawing money, the service verifies that the account has sufficient funds.

### 3. Repository Layer

**Files:** `AccountRepository.java` and `TransactionRepository.java`

Repositories communicate with PostgreSQL through Spring Data JPA.

They allow the application to retrieve, save, and update database records without writing SQL for every operation.

### 4. Entity Layer

**Files:** `Account.java` and `Transaction.java`

Entities represent the data stored in the database.

**Account** stores information such as the account number, account type, balance, and status.

**Transaction** stores information about banking activities, including transaction type, amount, associated account, and date.

## Application Architecture

```text
React Frontend / API Client
           |
           v
    REST Controller
           |
           v
     Account Service
     (Business Logic)
           |
           v
       Repository
           |
           v
     PostgreSQL Database
```

Each layer has a specific responsibility, making the application easier to understand, test, and maintain.

## REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/accounts/{id}` | Retrieve account information |
| POST | `/api/accounts` | Create a bank account |
| POST | `/api/accounts/{id}/deposits` | Deposit money |
| POST | `/api/accounts/{id}/withdrawals` | Withdraw money |

The application also implements account-to-account transfer functionality.

## Example: Deposit Money

A client sends this request:

```http
POST /api/accounts/1/deposits
Content-Type: application/json
```

Example request body:

```json
{
  "requestId": "0bba6ed8-5d5d-4f5b-bfa5-d8cc8420e9c2",
  "amount": 100.00
}
```

The backend:

1. Receives the request.
2. Finds the account in PostgreSQL.
3. Validates the transaction.
4. Adds $100.00 to the account balance.
5. Records the deposit.
6. Returns the updated account information.

## Database Design

The application uses two primary database tables.

**Accounts**

Stores bank account information:

- Account ID
- Account number
- Account type
- Balance
- Account status
- Creation date

**Transactions**

Stores banking transaction information:

- Transaction ID
- Account ID
- Transaction type
- Amount
- Creation date

Each transaction is associated with an account.

## Data Integrity and Error Handling

The application includes several features that help protect account data:

- **Database transactions:** Spring's `@Transactional` annotation helps keep related database operations together.
- **Pessimistic locking:** Helps prevent simultaneous operations from incorrectly modifying an account balance.
- **Insufficient funds validation:** Prevents withdrawals that exceed the available balance.
- **Transaction history:** Records completed banking operations.
- **Request IDs:** Provides identifiers that can be used to recognize repeated transaction requests.

## Running the Application Locally

### Requirements

- Java 21
- PostgreSQL
- Maven Wrapper (included)

### 1. Configure PostgreSQL

Create a PostgreSQL database named:

```text
banking_db
```

Configure a database user with access to the database.

### 2. Configure the Application

The application uses `application.properties` for database configuration.

```properties
spring.application.name=banking-service

spring.datasource.url=jdbc:postgresql://localhost:5432/banking_db
spring.datasource.username=${DB_USERNAME:banking_user}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
```

Set the `DB_PASSWORD` environment variable to your local PostgreSQL password before starting the application.

Do not commit database passwords or other credentials to GitHub.

### 3. Start the Backend

```bash
./mvnw spring-boot:run
```

The API runs locally at:

```text
http://localhost:8080
```

### 4. Test an Account

```bash
curl http://localhost:8080/api/accounts/1
```

The API returns account information in JSON format.

## Project Highlights

Through this project, I demonstrated experience with:

- Developing RESTful APIs using Java and Spring Boot.
- Implementing business logic in a service-oriented architecture.
- Integrating PostgreSQL with Spring Data JPA.
- Managing financial values using Java's `BigDecimal`.
- Implementing database transactions and locking.
- Designing and maintaining relational database entities.
- Testing APIs using cURL.
- Using Git and GitHub for version control.
- Connecting a Java backend to a separate React frontend.

## Future Improvements

Planned enhancements include:

- User authentication and authorization
- Secure account access
- Expanded transaction history APIs
- Automated unit and integration tests
- Docker containerization
- CI/CD pipelines
- Cloud deployment and monitoring

## Project Purpose

I developed this project to strengthen my Java backend development skills and demonstrate my ability to build a database-driven REST API.

The project also serves as the backend for a React and TypeScript banking dashboard.

**Note:** This is an educational banking simulation using test data, not a production banking platform.
