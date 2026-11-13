🚀 GateKeeper – Custom API Gateway

**A Smart API Gateway built with Java, Spring Boot and Microservices.**

## 📌 About the Project

GateKeeper is a custom API Gateway that acts as a bridge between clients and multiple backend services.

It provides a single entry point to manage API requests, secure communication, control traffic, and monitor API activities.

## 🎯 Why GateKeeper?

In microservices architecture, managing multiple APIs can be difficult.

GateKeeper helps by providing:
- Centralized API management.
- Secure authentication using JWT.
- Dynamic request routing.
- Rate limiting to control API traffic.
- Idempotency to prevent duplicate operations.
- API monitoring and analytics.

## 🏗️ Architecture

GateKeeper consists of three microservices:

1. **Smart Gateway Service** – Handles incoming requests, security, and routing.
2. **API Management Service** – Manages APIs, users, routes, and security policies.
3. **Analytics Service** – Tracks API requests, failures, and performance.

## 🛠️ Tech Stack

- **Backend:** Java, Spring Boot
- **Frontend:** React.js
- **Database:** MySQL, MongoDB
- **Caching:** Redis
- **Messaging:** Apache Kafka
- **Security:** Spring Security, JWT
- **Tools:** Maven, Git, GitHub, Docker

## 🔄 How It Works

1. Client sends an API request.
2. Smart Gateway receives and validates the request.
3. Gateway checks security and configured policies.
4. Request is forwarded to the correct backend service.
5. Response is returned to the client.
6. Analytics Service collects request information.

## 🌟 Key Highlights

- Custom API Gateway implementation.
- Microservices-based architecture.
- Centralized security and API management.
- Redis-based rate limiting.
- Kafka-based event processing.
- API analytics and monitoring.

## 🎯 Project Goal

To build an industry-oriented backend project and gain practical experience in Spring Boot, Microservices, API Gateway design, and distributed systems.


