# DemoLibrary

**Spring Boot** REST API for a small library management system — books, authors, users and rentals — with **Auth0**-based authentication, JPA/Hibernate persistence and BDD acceptance tests written in Gherkin. Built as interview-prep training to exercise a realistic backend stack end-to-end.

## Learning goal

Practise **Spring Boot** in a production-like shape: layered architecture (Controller → Service → Repository), DTOs vs entities, JPA entities and repositories, global exception handling, correlation-id logging, externalized security configuration, and BDD-style acceptance tests.

## Tech stack

- **Language / Framework:** Java 21, Spring Boot 3
- **Persistence:** Spring Data JPA / Hibernate
- **Auth:** Auth0 (JWT-based OAuth2 resource server)
- **Build:** Maven
- **Testing:** JUnit + Cucumber (Gherkin features)
- **Packaging:** WAR

## Project structure

```
src/main/java/net/myself/DemoLibrary/
├── DemoLibraryApplication.java
├── Controller/           # AuthenticationController, BookController, AuthorController, RentalController, UserController
├── Data/
│   ├── Entities/         # JPA entities (Book, Author, BookRental, DeletedBook, ...)
│   ├── NTO/              # Network Transfer Objects (request/response DTOs)
│   └── Repository/       # Spring Data repositories
├── Model/                # domain models
└── Infrastructure/
    ├── Configuration/    # Security, Jackson, Web, Test-visibility config
    ├── CorrelationIdInterceptor.java
    └── GlobalControllerExceptionHandler.java
```

## Build & run

```bash
./mvnw spring-boot:run
```

or package the WAR:

```bash
./mvnw package
```

## Companion repository

- React frontend: [demolibraryfe](https://github.com/Valer09/demolibraryfe)
