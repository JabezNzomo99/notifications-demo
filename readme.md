# Libraries and Tech Specs

To demonstrate Ktor's full capability in building scalable web applications the following Ktor features and libraries were
leveraged on;

## Ktor Features
- [Routing](https://ktor.io/docs/routing-in-ktor.html): Handles incoming requests and provides a simple DSL to capture route mappings.
- [Authentication](https://ktor.io/docs/authentication.html): Handles API authentication.
- [Call Logging](https://ktor.io/docs/logging.html): Logs application events.
- [Status pages](https://ktor.io/docs/status-pages.html): Handles exceptions and errors that occur within a request lifecycle and returns an appropriate response.
- [Content Negotiation](https://ktor.io/docs/serialization.html#built_in_converters): Negotiating media types and serializing/deserializing JSON content.

## External Libraries

### Database
- [Exposed](https://github.com/JetBrains/Exposed): ORM library developed by JetBrains, allowing for model definitions to be written in SQL format, while proving a simple interface and clean DSL.
- [HikariCP](https://github.com/brettwooldridge/HikariCP): Database connection and connection pooling.
- [Flyway](https://flywaydb.org/): Handles database migrations
- [Caffeine](https://github.com/ben-manes/caffeine): High performance in-memory cache based on Guava.

### Testing
- [Jacoco](https://github.com/jacoco/jacoco): Mature test coverage and reporting.
- [Mockk](https://mockk.io/): Easier mocks, stubs definitions using DSL allowing for easier testing.
- [Ktor-tests](https://ktor.io/docs/testing.html): Provides Ktor test engine to allow for high fidelity end to end integration tests.
- [Test Containers](https://www.testcontainers.org/): Provides reusable database instances for testing
- [Kotest](https://kotest.io/): Testing framework for rich assertions.

### Logging
- [Logback](http://logback.qos.ch/): Mature logging library.

### Utility
- [Koin](https://insert-koin.io/) - Dependency Injection.
- [Gradle with kotlin DSL](https://gradle.org/) - a widely adopted build tool, allowing us to write build scripts and code in Kotlin.
- [Valiktor](https://github.com/valiktor/valiktor) -  type-safe, powerful and extensible fluent DSL to validate objects in Kotlin.


# Getting started

### Configure Environment Variables

- Create a .env file, with the format:
- Create a project on firebase or use an existing one to retrieve the [service-account.json](https://firebase.google.com/docs/admin/setup#set-up-project-and-service-account) file. 

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=notifications
export DB_USER=admin
export DB_PASSWORD=admin
export SERVICE_ACCOUNT_FILE_PATH=path_to_your_firebase_sa.json
```

### Start Docker
- Ensure sure that [docker](https://docs.docker.com/get-docker/) and [docker compose](https://docs.docker.com/compose/install/) are installed on your machine. 
- CD to `/docker` and run the following command to initialize the database 
 ```bash
cd docker && docker-compose up
```

### Start the application server
- Run the following command to start the ktor sever
 ```bash
source .env && ./gradlew run
```
- Open a web browser and go to http://0.0.0.0:8080/ and you should see "I'm running!" in the page🚀


### Run the tests
 ```bash
source .env && ./gradlew clean tests
```
