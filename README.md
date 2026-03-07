# Ocean View Resort - Online Reservation System

A comprehensive web-based room reservation management system for Ocean View Resort, a popular beachside hotel in Galle, Sri Lanka.

## 📋 Project Overview

Ocean View Resort is an enterprise-grade Java EE application designed to streamline hotel room reservations, guest management, and billing operations. The system replaces manual booking processes with an automated, user-friendly interface that prevents booking conflicts and improves operational efficiency.

**Project Type**: Academic Assessment  
**Duration**: November 2025 - March 2026  
**Organization**: Cardiff Metropolitan University (UK) in partnership with ICBT Campus, Nugegoda  
**Author**: D. Narada

## 🎯 Key Features

### Core Functionality
- **User Authentication** - Secure login system with username and password validation
- **Guest Management** - Register new guests with comprehensive personal details
- **Reservation Management** - Create, view, update, and cancel reservations
- **Billing System** - Automated bill calculation with multiple billing strategies
- **Notifications** - Real-time reservation confirmations and reminders
- **Help System** - Built-in guidelines for new staff members
- **Reports** - Comprehensive reservation and billing reports

### Advanced Features
- **Design Patterns Implementation**
  - Strategy Pattern (Validation & Billing)
  - Observer Pattern (Notifications)
  - Singleton Pattern (Session Management)
  - Facade Pattern (Reservation Service)
  - Factory Pattern (Data Access Objects)

- **Multiple Billing Strategies**
  - Standard billing with tax calculations
  - Premium room pricing
  - Length-of-stay discounts (5%-15%)
  - Dynamic strategy selection by room type

- **Comprehensive Validation**
  - Guest information validation
  - Reservation date and duration validation
  - Server-side input validation with regex patterns
  - Custom validation rules engine

## 💻 Technology Stack

### Development Environment
- **IDE**: NetBeans 17
- **JDK**: JDK-17 (Java 17)
- **Build Tool**: Apache Maven 3.x
- **Application Server**: GlassFish Server 7.0
- **Database**: MySQL 8.0 / MySQL Workbench 8.0

### Framework & Libraries
- **Jakarta EE 10** - Enterprise Java Platform
- **Jakarta REST** - RESTful Web Services
- **Jakarta Persistence (JPA)** - Object-Relational Mapping
- **Jakarta Faces (JSF)** - Web Framework
- **JUnit 5** - Unit Testing Framework
- **MySQL Connector/J 8.4** - Database Driver
- **HikariCP 5.0.1** - Connection Pooling

## 📁 Project Structure

```
OceanViewResort/
├── src/
│   ├── main/
│   │   ├── java/com/orrs/
│   │   │   ├── billing/              # Billing strategies and engine
│   │   │   ├── config/               # Application configuration
│   │   │   ├── dao/                  # Data Access Objects
│   │   │   ├── domain/               # Entity classes (Guest, Room, Bill, etc.)
│   │   │   ├── help/                 # Help system implementation
│   │   │   ├── manager/              # Session and reservation management
│   │   │   ├── notification/         # Notification services (Email, SMS)
│   │   │   ├── resources/            # REST endpoints
│   │   │   ├── staff/                # Staff management
│   │   │   └── validator/            # Validation strategies and engine
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   │       └── persistence.xml   # JPA configuration
│   │   └── webapp/
│   │       ├── index.html            # Main UI
│   │       └── WEB-INF/
│   │           ├── beans.xml         # CDI beans configuration
│   │           ├── web.xml           # Deployment descriptor
│   │           └── glassfish-web.xml # GlassFish specific config
│   └── test/
│       └── java/com/orrs/            # Test suites for all modules
├── pom.xml                            # Maven configuration
├── nb-configuration.xml              # NetBeans configuration
└── README.md                          # This file
```

## 🚀 Getting Started

### Prerequisites

1. **NetBeans 17**
   - Download from [Apache NetBeans](https://netbeans.apache.org/)
   - Ensure it includes JDK-17 or configure it separately

2. **JDK-17**
   ```bash
   # Verify Java installation
   java -version
   # Output should show Java 17.x.x
   ```

3. **MySQL 8.0**
   - Download from [MySQL Community](https://dev.mysql.com/downloads/mysql/)
   - Ensure MySQL Server and MySQL Workbench 8.0 are installed

4. **GlassFish Server 7.0**
   - Download from [GlassFish Download](https://glassfish.org/download)
   - Version 7.0 or later

5. **Maven 3.x**
   - Usually bundled with NetBeans
   - Verify: `mvn -version`

### Installation Steps

#### 1. Open Project in NetBeans

```bash
# Open NetBeans and select:
# File → Open Project → Navigate to OceanViewResort folder
```

#### 2. Configure GlassFish Server in NetBeans

1. Go to **Tools → Servers**
2. Click **Add Server**
3. Select **GlassFish Server 7.0**
4. Set the installation directory to your GlassFish 7.0 location
5. Click **Next** and **Finish**

#### 3. Create Database

Open MySQL Workbench and execute the database setup script:

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS ocean_view_resort;
USE ocean_view_resort;

-- Create tables (see persistence.xml and domain classes for schema)
-- Tables: guests, reservations, rooms, room_types, bills, staff, etc.
```

Alternatively, the application can auto-generate tables using JPA when first deployed (if configured in persistence.xml).

#### 4. Configure Database Connection

1. Edit `src/main/resources/META-INF/persistence.xml`
2. Update database connection details:
   ```xml
   <property name="javax.persistence.jdbc.url" 
             value="jdbc:mysql://localhost:3306/ocean_view_resort"/>
   <property name="javax.persistence.jdbc.user" value="root"/>
   <property name="javax.persistence.jdbc.password" value="your_password"/>
   ```

#### 5. Build and Deploy

```bash
# Clean and build the project
mvn clean build

# Or in NetBeans:
# Right-click project → Clean and Build (Shift+F11)

# Deploy to GlassFish:
# Right-click project → Deploy (F11)
```

## 🧪 Testing

### Run Unit Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=BillTest

# In NetBeans: Right-click project → Test
```

### Test Coverage

The project includes comprehensive test suites:

- **Domain Tests**: `BillTest`, `GuestTest`, `ReservationTest`, `RoomTypeTest`
- **Utilities Tests**: `HelpSystemTest`
- **Manager Tests**: `AuthenticationManagerTest`, `ReservationManagerTest`
- **Staff Tests**: `StaffTest`

Test reports are generated in: `target/surefire-reports/`

## 🏗️ Architecture

### Design Patterns Implemented

#### 1. **Strategy Pattern** (Billing & Validation)
- `BillingStrategy` interface with multiple implementations
- `ValidationStrategy` interface for pluggable validation rules
- Enables runtime selection of billing and validation approaches

#### 2. **Observer Pattern** (Notifications)
- `ReservationObserver` interface
- `EmailNotifier` and `SMSNotifier` implementations
- `NotificationService` acts as the subject
- Automatic notifications on reservation events

#### 3. **Singleton Pattern** (Session Management)
- `SessionManager` ensures single instance per JVM
- Thread-safe implementation with eager initialization
- Session timeout: 30 minutes (configurable)

#### 4. **Facade Pattern** (Reservation Service)
- `ReservationService` provides unified interface
- Coordinates: DAOs, BillingEngine, ValidationEngine, NotificationService
- Simplifies complex reservation operations

#### 5. **Factory Pattern** (Data Access)
- DAO factory for object creation
- Consistent data access abstraction

### Layer Architecture

```
┌─────────────────────────────────────┐
│   Presentation Layer (JSF, REST)    │
├─────────────────────────────────────┤
│   Business Logic Layer              │
│  (Services, Managers, Engines)      │
├─────────────────────────────────────┤
│   Validation & Notification Layer   │
├─────────────────────────────────────┤
│   Persistence Layer (JPA/DAO)       │
├─────────────────────────────────────┤
│   Database Layer (MySQL)            │
└─────────────────────────────────────┘
```

## 📊 Database Schema

### Main Entities

- **Guest** - Guest personal information
- **Reservation** - Booking details with guest and room references
- **Room** - Individual room information
- **RoomType** - Room categories and daily rates
- **Bill** - Billing records with calculated amounts
- **Staff** - Staff credentials for system access
- **Notification** - Notification history

## 🔧 Configuration Files

### persistence.xml
Located at: `src/main/resources/META-INF/persistence.xml`

Configures JPA provider, database connection, and entity scanning.

### pom.xml
Maven configuration specifying:
- Jakarta EE 10 dependencies
- MySQL JDBC driver
- HikariCP connection pool
- JUnit 5 for testing
- Maven plugins for compilation and testing

### GlassFish Configuration
- `glassfish-web.xml` - GlassFish-specific deployment configuration
- `beans.xml` - CDI (Contexts and Dependency Injection) configuration
- `web.xml` - Standard Java EE deployment descriptor

## 🔐 Security Considerations

- User authentication required for system access
- Session-based security with timeout mechanism
- Server-side input validation (prevent injection attacks)
- Database connection pooling for secure resource management
- Role-based access control for staff members

## 📝 Usage Examples

### Creating a Reservation

1. Login with staff credentials
2. Navigate to "Add New Reservation"
3. Enter guest details (name, address, contact)
4. Select room type and check-in/check-out dates
5. System validates and confirms the reservation
6. Automated notification sent to guest

### Generating a Bill

1. Retrieve reservation
2. Bill calculation occurs automatically based on:
   - Room type (Standard/Premium)
   - Length of stay
   - Applicable discounts
   - Tax calculations (10%)
3. Bill can be printed or exported

## 🐛 Troubleshooting

### Common Issues

**Issue**: Database connection failed
- **Solution**: Verify MySQL is running and credentials in persistence.xml are correct

**Issue**: GlassFish deployment fails
- **Solution**: Ensure GlassFish 7.0 is configured in NetBeans and compatible with JDK-17

**Issue**: Tests fail
- **Solution**: Ensure database is accessible and test data is properly initialized

**Issue**: Port 8080 already in use
- **Solution**: Change GlassFish HTTP port in `domain.xml` or stop conflicting service

## 👥 Team & Contribution

- **Lead Developer**: D. Narada
- **Institution**: Cardiff Metropolitan University
- **Partner**: ICBT Campus, Nugegoda, Sri Lanka

## 📚 Documentation

Additional documentation available in the project:

- [UML Diagrams](../base/plane.md) - Design diagrams and use cases
- [Database Design](../database/) - Database layer documentation
- [Git Workflow](../base/howTo/git-branching.md) - Version control guidelines
- [Task Documentation](../core/) - Detailed task specifications

## 📄 License

This project is developed as part of an academic assessment at Cardiff Metropolitan University.

## 🔄 Version History

**Version 1.0** (Current)
- Complete implementation of core reservation system
- Design patterns integrated
- Comprehensive testing suite
- Full documentation

---

For questions or issues, refer to the help section within the application or contact the development team.

**Last Updated**: March 2026  
**GlassFish**: 7.0  
**MySQL**: 8.0  
**JDK**: 17  
**NetBeans**: 17
