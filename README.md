# ValoExplore Backend

A Spring Boot RESTful API serving the backend operations for ValoExplore. This application handles user authentication via Auth0, manages Valorant account statistics, cosmetics inventories, and match histories using Spring Data JPA, and integrates with external APIs to seed initial game asset data. The main application entry point is located at [`src/main/java/com/valoexplore/backend/BackendApplication.java`](https://www.google.com/search?q=src/main/java/com/valoexplore/backend/BackendApplication.java).

## Setup and Local Development

This project uses Java 21 and includes the Maven Wrapper, so a local Maven installation is not required.

To run the application locally:

1. Clone the repository and navigate to the project root.

2. Create an `application.properties` file in `src/main/resources/` (see the template below).

3. Execute the application using the Maven wrapper:

```
# On macOS/Linux
./mvnw spring-boot:run

# On Windows
mvnw.cmd spring-boot:run

```

The application fetches external asset data on startup. Ensure you have an active internet connection to allow the seeding process to complete.

### Template `application.properties`

The code requires configuration for the in-memory database and the OAuth2 resource server to function properly. Place the following in `src/main/resources/application.properties` and replace the Auth0 placeholder values with your specific tenant details.

```
# Server Configuration
server.port=8080

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:valodb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# JPA / Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Enable H2 Console for local debugging
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Auth0 / OAuth2 Resource Server Configuration
spring.security.oauth2.resourceserver.jwt.issuer-uri=https://YOUR_AUTH0_DOMAIN/
spring.security.oauth2.resourceserver.jwt.audiences=YOUR_API_IDENTIFIER

```

## Auth0 Configuration

The backend relies on Auth0 for identity management and expects specific custom claims in the JWT access token.

1. **Create an API in Auth0**: Navigate to the Auth0 Dashboard > Applications > APIs and create a new API. The "Identifier" you choose here will be your `spring.security.oauth2.resourceserver.jwt.audiences` value in the properties file.

2. **Configure RBAC**: In your Auth0 API settings, enable "Enable RBAC" and "Add Permissions in the Access Token".

3. **Add Custom Claims via Actions**: The backend application specifically looks for two custom claims mapped to a local namespace to sync user data and roles: `https://valoexplore.local/email` and `https://valoexplore.local/roles`.

Navigate to Actions > Library in the Auth0 dashboard, create a new custom action for the **Login / Post Login** flow, and add the following code:

```
exports.onExecutePostLogin = async (event, api) => {
  const namespace = '[https://valoexplore.local](https://valoexplore.local)';

  if (event.user.email) {
    api.accessToken.setCustomClaim(`${namespace}/email`, event.user.email);
  }

  if (event.authorization) {
    api.accessToken.setCustomClaim(`${namespace}/roles`, event.authorization.roles);
  }
};

```

Deploy the action and add it to your Login flow to ensure the backend receives the required user information.

## Interesting Techniques

* **JWT Authorization**: Implements stateless authentication by validating [JSON Web Tokens (JWT)](https://www.google.com/search?q=https://developer.mozilla.org/en-US/docs/Web/Security/JSON_Web_Token) via Spring Security's OAuth2 Resource Server. Custom claims are parsed to map authorities.

* **Cross-Origin Resource Sharing (CORS)**: Configured programmatically to securely manage [Cross-Origin Resource Sharing](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) and allow specific requests from the frontend client.

* **Entity Auditing**: Uses Spring Data JPA's `@EnableJpaAuditing` to automatically populate creation and modification timestamps on base entities using lifecycle events.

* **Automated Data Seeding**: Utilizes a Spring `CommandLineRunner` hook to fetch and ingest asset data from an external REST API upon application startup, ensuring the database is populated without manual intervention.

* **Complex Data Relationships**: Makes extensive use of JPA `@ElementCollection`, `@ManyToMany`, and `@Embedded` annotations to map complex nested game data to relational database tables.

## Technologies and Libraries

This project uses standard Spring Boot starters alongside several specific tools to streamline development:

* [Spring Boot OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html): Provides the infrastructure to protect endpoints and validate OAuth 2.0 access tokens.

* [H2 Database](https://www.h2database.com/): An in-memory relational database configured for runtime to facilitate rapid development and testing without external provisioning.

* [Lombok](https://projectlombok.org/): Reduces boilerplate Java code by auto-generating getters, setters, and constructors via annotation processing.

## Project Structure

```
.
├── .mvn/
│   └── wrapper/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── valoexplore/
│   │               └── backend/
│   │                   ├── config/
│   │                   ├── controller/
│   │                   ├── model/
│   │                   ├── payload/
│   │                   ├── repository/
│   │                   ├── security/
│   │                   └── service/
│   └── test/
│       └── java/
│           └── com/
│               └── valoexplore/
│                   └── backend/
├── .gitattributes
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml

```

* [`config/`](https://www.google.com/search?q=src/main/java/com/valoexplore/backend/config/): Contains application-wide configuration classes, including the database initialization routines.

* [`controller/`](https://www.google.com/search?q=src/main/java/com/valoexplore/backend/controller/): Defines the REST API endpoints, handling incoming HTTP requests and routing them to the appropriate services.

* [`model/`](https://www.google.com/search?q=src/main/java/com/valoexplore/backend/model/): Houses the JPA entities that define the database schema and domain relationships.

* [`payload/`](https://www.google.com/search?q=src/main/java/com/valoexplore/backend/payload/): Contains Data Transfer Objects (DTOs) constructed to shape API responses and incoming requests.

* [`repository/`](https://www.google.com/search?q=src/main/java/com/valoexplore/backend/repository/): Contains Spring Data JPA interfaces for abstracting database access and executing custom queries.

* [`security/`](https://www.google.com/search?q=src/main/java/com/valoexplore/backend/security/): Manages Spring Security configurations, defining CORS policies and JWT validation rules.

* [`service/`](https://www.google.com/search?q=src/main/java/com/valoexplore/backend/service/): Contains the core business logic, external API integration handling, and data mapping operations.
