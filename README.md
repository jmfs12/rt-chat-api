# RT-Chat API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

This project is an API built in Java using the Spring Boot framework. It serves as the backend for the [RT-Chat Frontend](https://github.com/jmfs12/rt-chat-web) project, providing real-time chat functionalities.

## Core Technologies:

- **Framework**: Java 17, Spring Boot 3
- **Database**: PostgreSQL
- **Authentication**: Spring Security & JSON Web Tokens (JWT)
- **Real-time Communication**: WebSocket (STOMP)
- **API Documentation**: Swagger/OpenAPI
- **Unit Tests**: JUnit & Mockito
- **Integration Tests**: Spring Boot Test

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the application](#running-the-application)
- [Authentication Flow](#authentication-flow)
- [API Endpoints Overview](#api-endpoints-overview)
- [WebSocket Communication](#websocket-communication)
- [Database Schema](#database-schema)

## Prerequisites

Before starting, ensure you have the following tools installed on your system:  

- Java 17 or higher
- Apache Maven 3.6 or later.
- PostgreSQL server running locally or accessible remotely.

## Installation

1. Clone the repository:

```bash
git clone https://github.com/jmfs12/rt-chat-api.git
cd rt-chat-api
```

2. Install dependencies with Maven

```bash
mvn clean install
```

## Configuration

Application settings are managed in src/main/resources/application.properties. Before running the application, you must configure the database connection and JWT settings.
1. Database configuration
    - Set up your PostgreSQL database and update the following properties in [application.properties](src/main/resources/application.properties):
    
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    ```
2. JWT Secret Configuration
    - Set a secure secret key for JWT in the application properties:
    
    ```properties
    # JWT Configuration
    # Use a strong base64 encoded secret key for production environments.
    jwt.secret.key=YOUR_ULTRA_SECRET_KEY_FOR_JWT_SIGNING
    jwt.expiration.ms=86400000 # Token expiration time in milliseconds (e.g., 24 hours)
    ```

## Running the application
Once configured, start the application using the Spring Boot Maven plugin:

```bash
mvn spring-boot:run
```
The Api will be accessible at `http://localhost:8080`.

### Api Documentation (Swagger):
API documentation is available at `http://localhost:8080/swagger-ui.html` 
once the application is running.

## Authentication Flow
Authentication is handled using JWT. Access to protected endpoints requires a 
valid token provided in the Authorization header.

1. **Registration**: Users can register by sending a POST request to `/api/auth/register` 
with their details.
2. **Login**: The user authenticates using credentials via the POST `/api/auth/login` endpoint.
3. **Token Generation**: Upon successful authentication, a JWT is generated and returned to the client.
4. **Token Usage**: The client includes the JWT in the Authorization header for subsequent requests
to protected endpoints.

## API Endpoints Overview
- **Authentication**:
    - `POST /api/auth/register`: Register a new user.
    - `POST /api/auth/login`: Authenticate and receive a JWT.
- **User Management**:
    - `GET /api/users`: Retrieve a list of users (protected).
    - `GET /api/users/me`: Get current user details by ID (protected).
- **Message Management**:
    - `GET /api/messages`: Get all messages between two users.

## WebSocket Communication
Real-time messaging is implemented using WebSocket with STOMP protocol.
- **WebSocket Endpoint**: `ws://localhost:8080/ws`
- **User-Specific Notifications (Private Messages/Updates)**: Clients subscribe to their private queue to receive direct messages and notifications.
   - Subscribe: /user/queue/message

## Database Schema
The database schema is managed by Spring Data JPA based on the entity classes defined in the source code. The primary entities include:
- **User**: Represents application users with fields for username, password, email, and roles.
- **Message**: Represents chat messages with fields for sender, receiver, content, and timestamp

## Contributing
Contributions are welcome! Please fork the repository and create a pull request with your changes.
