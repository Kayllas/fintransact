# FinTransact - Financial System Portfolio Project

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen?logo=springboot)
![Angular](https://img.shields.io/badge/Angular-14-red?logo=angular)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.12-orange?logo=rabbitmq)

**[🇧🇷 Português](#português) | [🇺🇸 English](#english)**

---

## 🇧🇷 Português

### 📖 Sobre o Projeto

FinTransact é um sistema bancário simplificado desenvolvido para demonstrar habilidades de desenvolvimento full-stack utilizando Java (Spring Boot), Angular, Docker e Mensageria (RabbitMQ). Este projeto faz parte do meu portfólio profissional.

### 🚀 Tecnologias Utilizadas

- **Backend**: Java 17, Spring Boot 3.2 (Web, Data JPA, Security, AMQP)
- **Frontend**: Angular 14, PrimeNG
- **Banco de Dados**: PostgreSQL
- **Mensageria**: RabbitMQ
- **Infraestrutura**: Docker, Docker Compose
- **Segurança**: JWT (JSON Web Tokens)

### 🏗 Arquitetura

O sistema segue uma arquitetura modular:

1. **Core API**: Gerencia Usuários, Contas e Transações. Expõe uma API REST segura com JWT.
2. **Notification Service**: Consome eventos de transação do RabbitMQ para simular notificações assíncronas.
3. **Frontend**: Single Page Application (SPA) para interação do usuário.

```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐
│   Angular   │─────▶│   Core API   │─────▶│ PostgreSQL  │
│  Frontend   │      │ (Spring Boot)│      │  Database   │
└─────────────┘      └──────┬───────┘      └─────────────┘
                            │
                            ▼
                     ┌─────────────┐
                     │  RabbitMQ   │
                     └──────┬──────┘
                            │
                            ▼
                  ┌──────────────────┐
                  │  Notification    │
                  │    Service       │
                  └──────────────────┘
```

### 🛠 Configuração e Execução

#### Pré-requisitos
- Docker & Docker Compose instalados

#### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/Kayllas/fintransact.git
   cd fintransact
   ```

2. Execute a aplicação usando Docker Compose:
   ```bash
   docker-compose up --build
   ```

3. Acesse a aplicação:
   - **Frontend**: [http://localhost:4200](http://localhost:4200)
   - **Core API**: [http://localhost:8080](http://localhost:8080)
   - **RabbitMQ Dashboard**: [http://localhost:15672](http://localhost:15672)
     - Usuário: `user`
     - Senha: `password`

### 🧪 Funcionalidades para Testar

1. **Registro**: Crie uma nova conta de usuário
2. **Login**: Acesse o dashboard com suas credenciais
3. **Transferência**: Envie dinheiro para outro usuário (use o número da conta)
4. **Histórico**: Visualize o histórico de transações
5. **Notificações Assíncronas**: Verifique os logs do `notification-service` para ver os eventos processados

### 📂 Estrutura do Projeto

```
fintransact/
├── backend/
│   ├── core-api/              # API principal com lógica bancária
│   └── notification-service/  # Consumidor de eventos assíncronos
├── frontend/                  # Interface Angular
└── docker-compose.yml         # Orquestração de containers
```

### 🔐 Segurança

- Autenticação JWT
- Senhas criptografadas com BCrypt
- Proteção de endpoints com Spring Security
- CORS configurado

### 📝 Licença

Este projeto é de código aberto e está disponível para fins educacionais e de portfólio.

---

## 🇺🇸 English

### 📖 About the Project

FinTransact is a simplified banking system designed to demonstrate full-stack development skills using Java (Spring Boot), Angular, Docker, and Messaging (RabbitMQ). This project is part of my professional portfolio.

### 🚀 Tech Stack

- **Backend**: Java 17, Spring Boot 3.2 (Web, Data JPA, Security, AMQP)
- **Frontend**: Angular 14, PrimeNG
- **Database**: PostgreSQL
- **Messaging**: RabbitMQ
- **Infrastructure**: Docker, Docker Compose
- **Security**: JWT (JSON Web Tokens)

### 🏗 Architecture

The system follows a modular architecture:

1. **Core API**: Manages Users, Accounts, and Transactions. Exposes a secure REST API with JWT.
2. **Notification Service**: Consumes transaction events from RabbitMQ to simulate async notifications.
3. **Frontend**: Single Page Application (SPA) for user interaction.

```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐
│   Angular   │─────▶│   Core API   │─────▶│ PostgreSQL  │
│  Frontend   │      │ (Spring Boot)│      │  Database   │
└─────────────┘      └──────┬───────┘      └─────────────┘
                            │
                            ▼
                     ┌─────────────┐
                     │  RabbitMQ   │
                     └──────┬──────┘
                            │
                            ▼
                  ┌──────────────────┐
                  │  Notification    │
                  │    Service       │
                  └──────────────────┘
```

### 🛠 Setup & Run

#### Prerequisites
- Docker & Docker Compose installed

#### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/Kayllas/fintransact.git
   cd fintransact
   ```

2. Run the application using Docker Compose:
   ```bash
   docker-compose up --build
   ```

3. Access the application:
   - **Frontend**: [http://localhost:4200](http://localhost:4200)
   - **Core API**: [http://localhost:8080](http://localhost:8080)
   - **RabbitMQ Dashboard**: [http://localhost:15672](http://localhost:15672)
     - User: `user`
     - Password: `password`

### 🧪 Features to Test

1. **Register**: Create a new user account
2. **Login**: Access the dashboard with your credentials
3. **Transfer**: Send money to another user (use their account number)
4. **History**: View transaction history
5. **Async Notifications**: Check `notification-service` logs to see processed events

### 📂 Project Structure

```
fintransact/
├── backend/
│   ├── core-api/              # Main API with banking logic
│   └── notification-service/  # Async event consumer
├── frontend/                  # Angular interface
└── docker-compose.yml         # Container orchestration
```

### 🔐 Security

- JWT Authentication
- BCrypt encrypted passwords
- Spring Security endpoint protection
- CORS configured

### 📝 License

This project is open source and available for educational and portfolio purposes.

---

**Developed by Kayque Castro** | [GitHub](https://github.com/Kayllas)
