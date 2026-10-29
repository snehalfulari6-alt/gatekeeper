# GateKeeper

A custom, backend-focused **API gateway and API management platform** built with Spring Boot. GateKeeper shows how gateway responsibilities work internally: routing, policy enforcement and reverse proxying, with routes stored as configuration instead of hard-coded.

> This is an educational project with a defined scope, not a replacement for a production gateway such as Spring Cloud Gateway, Kong or NGINX.

## What it does

GateKeeper separates live traffic from configuration:

| Service | Plane | Responsibility |
|---|---|---|
| `api-management-service` | Control plane | Stores organizations (tenants), management users, routes and policies in MySQL; issues JWTs; versions route configuration; audits admin actions |
| `smart-gateway-service` | Data plane | Matches routes (held in memory), validates JWTs locally, enforces role checks, rate limits and idempotency, rewrites paths/headers, forwards requests with timeouts |
| `analytics-service` | Analytics | Consumes access events from Kafka, de-duplicates them and stores them in MongoDB for request statistics |

Small Catalog and Order demo services sit behind the gateway to demonstrate forwarding and policies.

### Key design ideas

- **Multi-tenant**: an organization is the tenant, and a route is identified by `organization_id + route_id`.
- **Management users are not tenant customers**: GateKeeper authenticates only its own admins (`PLATFORM_ADMIN`, `TENANT_ADMIN`, `GATEWAY_SERVICE`). End-user authentication stays with the tenant application.
- **No per-request calls to Management**: the gateway keeps routes in memory and validates JWTs locally.
- **Redis** holds shared short-lived state (rate-limit counters, POST idempotency).
- **Kafka** carries access events and route-change notifications.
- **Versioning vs auditing**: every route change creates a configuration version; control-plane actions are recorded separately in audit records.

### Architecture

```
Admin / Postman --> API Management --> MySQL
                         |
                  (Kafka: config events)
                         v
API Client --> Smart Gateway --> Catalog / Order services
                  |      \
                Redis     (Kafka: access events)
                                 v
                          Analytics --> MongoDB
```

## Tech stack

- Java, Spring Boot (Spring Security, AOP)
- MySQL (API Management data)
- MongoDB (Analytics data)
- Redis (rate limiting, idempotency)
- Apache Kafka (events)
- JWT for management authentication
- Maven

## Project structure

```
api-management-service/
smart-gateway-service/
analytics-service/
```

## How to run

### Prerequisites

- JDK (version used by the project's `pom.xml`)
- Maven (or the included Maven wrapper)
- MySQL, MongoDB, Redis and Kafka running locally

> The first milestone only needs the three Spring Boot apps to start and answer a health check. Redis, Kafka, MySQL and MongoDB are needed as the later features are added.

### Steps

1. Clone the repository
   ```bash
   git clone <your-repo-url>
   cd gatekeeper
   ```
2. Configure each service's `src/main/resources/application.properties` (or `.yml`): database credentials, Redis/Kafka addresses, JWT secret and a distinct `server.port` per service.
3. Start each service in its own terminal
   ```bash
   cd api-management-service && ./mvnw spring-boot:run
   cd smart-gateway-service  && ./mvnw spring-boot:run
   cd analytics-service      && ./mvnw spring-boot:run
   ```
4. Verify with the health check
   ```bash
   curl http://localhost:<port>/health
   ```

## API endpoints

### API Management Service (public)

| Method | Path | Description |
|---|---|---|
| GET | `/health` | Check that the service is running |
| POST | `/api/auth/login` | Authenticate a management user and receive a JWT |

There is intentionally **no** `/api/auth/register`. Organizations and their admins are provisioned by a `PLATFORM_ADMIN` through the controlled management flow.

### Planned

Route CRUD, organization provisioning, and a read-only endpoint that returns active routes for the gateway are planned for upcoming milestones.

## Roles

| Role | Can do |
|---|---|
| `PLATFORM_ADMIN` | Create, activate, suspend and soft-delete organizations; view platform health |
| `TENANT_ADMIN` | Manage routes and policies for their own organization only |
| `GATEWAY_SERVICE` | Read active configuration; cannot modify it |

## Out of scope

Service discovery and load balancing, circuit breakers and retries, response caching, body transformation, WebSocket/gRPC/GraphQL proxying, TLS termination, and billing/API-key products.

## Status

Current milestone: architecture design, independent startup of the three services, and a basic health check.
