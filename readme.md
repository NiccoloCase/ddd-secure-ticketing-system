# Digital Ticketing Validation System to Prevent Scalping

**Authors**: Caselli Niccolo, Cavichia Lautaro, Tinacci Lapo  
**Course**: Software Engineering (B003372) - Bachelor's in Computer Engineering  
**Professor**: Enrico Vicario, Department of Information Engineering  
**Submission Date**: January 31, 2025


---

For a comprehensive overview of the system’s design, engineering, and implementation details, please refer to our [Report.pdf](https://github.com/NiccoloCase/ddd-secure-ticketing-system/blob/main/report.pdf)

---

## 📖 Introduction
This project addresses ticket scalping by redesigning the ticket verification process. Instead of guests presenting a QR code, staff generate a unique verification QR code for guests to scan. This ensures tickets cannot be shared or resold before arrival. Built using **Domain-Driven Design (DDD)** principles, the system features JWT authentication, DTO validation, PostgreSQL persistence and well structured test suite.

---

## 🎯 Key Features

- **Anti-Scalping Mechanism**: Staff-generated QR codes prevent ticket sharing.
- **Decoupled Architecture**: Built using Domain-Driven Design architecture with one controller per action and centralized dependency injection.
- **Secure Authentication**: JWT-based login and signup for robust security.

- **Event Management**: Create, update, and delete events with configurable ticket quotas and pricing.
- **Role-Based Access**:
    - **Admins**: Create/manage events and assign staff.
    - **Staff**: Generate verification QR codes and validate tickets.
    - **Guests**: Browse events, purchase tickets, and validate via QR scan.
- **Testing**: 106 tests achieving 92.5% class coverage.
---


## 🏛️ Architectural Philosophy

Our system is built with a clear separation of concerns and a focus on Domain-Driven Design (DDD) principles, ensuring that each layer of the application has a well-defined responsibility. Here’s how the architecture comes together:

### 1. **Separation of Concerns: Controllers & Business Logic**
- **One Controller Per Action**:  
  Each user action is handled by its own dedicated controller. By isolating responsibilities, we ensure that the code remains modular and easier to test or update without affecting unrelated parts of the system.

- **Business Logic Layer**:  
  Controllers delegate complex operations to a dedicated business logic layer.

### 2. **Dependency Injection & Modular Design**
- **Centralized Dependency Injection**:  
  All services, including controllers, DAOs, and other core components, are assembled and injected via a centralized dependency injection mechanism. This design choice minimizes tight coupling between components and makes it straightforward to substitute parts of the system (for example, swapping a real DAO with a mock during testing).

### 3. **Data Persistence with DAO Pattern**
- **DAO-Driven Persistence**:  
  The system uses the DAO pattern to abstract database interactions. Each domain entity—such as Event, User, or Ticket—is managed by its corresponding DAO. This pattern ensures that all database operations are encapsulated in a single layer, keeping the business logic unaware of the underlying PostgreSQL implementation.

### 4. Domain Model & Model-Driven Design

Our system follows a **Domain-Driven Design (DDD)** approach, where the domain model accurately reflects real-world event management processes.


---



## 🛠 System Design

### Tools
- **Design**: StarUML (UML), Draw.io (ER diagrams), Figma (mockups).
- **Testing**: JUnit, Mockito, JaCoCo for coverage.

---

## 💻 Technologies
- **Language**: Java 17
- **Dependencies**:
    - **Security**: JJWT (0.12.2), jbcrypt (0.4).
    - **Database**: PostgreSQL JDBC (42.5.0).
    - **Validation**: Jakarta Validation (3.1.0), Hibernate Validator (8.0.0).
    - **Testing**: JUnit Jupiter (5.11.4), Mockito (4.2.0).

---




## 🚀 Installation & Setup

1. Clone the repository:
   ```sh
   git clone https://github.com/NiccoloCase/ddd-secure-ticketing-system.git
   cd ddd-secure-ticketing-system
   ```
2. Create a `.env` file in the root directory:
   ```sh
   JWT_SECRET=<your_secret_key>
   DB_URL=<your_database_url>
   ```
3. Install dependencies and compile:
   ```sh
   mvn clean install
   ```
4. Run unit and integration tests:
    ```sh
    mvn test
    ```



## Notes on AI Assistance

This project leveraged AI-powered tools such as GitHub Copilot and LLMs (ChatGPT, Claude) for code generation, UML verification, and design refinement while maintaining critical oversight on all outputs.

## License

This project is licensed under the MIT License.
