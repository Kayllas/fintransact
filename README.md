# FinTransact - Financial System Portfolio Project

FinTransact is a simplified banking system designed to demonstrate full-stack development skills using Java (Spring Boot), Angular, Docker, and Messaging (RabbitMQ).

## 🚀 Tech Stack

- **Backend**: Java 17, Spring Boot 3.x (Web, Data JPA, Security, AMQP)
- **Frontend**: Angular 14, PrimeNG
- **Database**: PostgreSQL
- **Messaging**: RabbitMQ
- **Infrastructure**: Docker, Docker Compose

## 🏗 Architecture

The system follows a modular architecture:
1.  **Core API**: Manages Users, Accounts, and Transactions. Exposes a REST API.
2.  **Notification Service**: Consumes transaction events from RabbitMQ to simulate async notifications.
3.  **Frontend**: Single Page Application (SPA) for user interaction.

## 🛠 Setup & Run

### Prerequisites
- Docker & Docker Compose

### Steps
1.  Clone the repository.
2.  Navigate to the project root.
3.  Run the application using Docker Compose:
    ```bash
    docker-compose up --build
    ```
4.  Access the application:
    - **Frontend**: [http://localhost:4200](http://localhost:4200)
    - **Core API**: [http://localhost:8080](http://localhost:8080)
    - **RabbitMQ Dashboard**: [http://localhost:15672](http://localhost:15672) (User: `user`, Pass: `password`)

## 🧪 Features to Test
1.  **Register**: Create a new user account.
2.  **Login**: Access the dashboard.
3.  **Transfer**: Send money to another user (use their Account Number).
4.  **History**: View transaction history.
5.  **Async Notifications**: Check `notification-service` logs to see processed events.

## 📂 Project Structure
- `backend/core-api`: Main banking logic.
- `backend/notification-service`: Async event consumer.
- `frontend`: Angular UI.
