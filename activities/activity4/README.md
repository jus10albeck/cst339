# Activity 4 - Spring Data Java Database Connectivity (JDBC)
CST-339: Programming in Java III  
Justin Albecker  
2/15/2026

---
## Part 1: Creating Data Services Using Spring JDBC
- Screenshot of the Orders page
![Orders Page](screenshots/screenshot1.png)

## Part 2: Creating Data Services Using Spring Data JDBC
- Screenshot of the Orders page
![Orders Page](screenshots/screenshot2.png)

## Part 3: Creating Data Services Using Spring Data JDBC Native Queries
- Screenshot of the Orders page
![Orders Page](screenshots/screenshot3.png)

## Research Questions
1. Spring Data JDBC provides a simpler, more direct way to interact with relational databases by executing SQL statements without the heavy abstraction layers seen in ORM-based frameworks. It emphasizes predictability, direct SQL mapping, and minimal overhead, making it suitable for applications that need performance and clarity (Introduction to Spring Data JDBC, 2025). In contrast, Java JDBC requires developers to manually manage connections, write SQL queries, handle result sets, and work directly with the JDBC API’s core interfaces such as Connection, Statement, PreparedStatement, and ResultSet (JDBC (Java Database Connectivity), 2026). While JDBC offers precise control and database independence through its standard API and driver-based architecture, it also leads to more boilerplate code and manual error handling (JDBC (Java Database Connectivity), 2026). Spring Data JDBC reduces this boilerplate by providing repository abstractions and straightforward entity mapping, enabling cleaner and more maintainable code while still relying on SQL directly (Introduction to Spring Data JDBC, 2025).
References:
Introduction to Spring Data JDBC (2025). Geeks for Geeks. Retrieved on February 11, 2026 from https://www.geeksforgeeks.org/advance-java/introduction-to-spring-data-jdbc/

JDBC (Java Database Connectivity) (2026). Retrieved on February 11, 2026 from https://www.geeksforgeeks.org/java/introduction-to-jdbc/

2. Spring Data JDBC supports transaction management by relying on Spring’s general transaction framework. Spring enables transaction handling through @EnableTransactionManagement, which manages commit/rollback behavior and applies isolation levels at the database level (Paraschiv, 2024). Transactional boundaries can be set at the class or method layer using @Transactional, letting developers control isolation, propagation, read-only hints, and rollback rules (Paraschiv, 2024). These mechanisms ensure atomicity, consistency, isolation, and durability. In Spring, transaction behavior ultimately depends on the underlying JDBC and database engine, while Spring provies the orchestration layer (Paraschiv, 2024).

References:
Paraschiv, E (2024). Baeldung. Transactions with Spring and JPA. https://www.baeldung.com/transaction-configuration-with-jpa-and-spring

## Conclusion
This activity guided the development of data services using three approaches: Spring JDBC with SQL-based DAO patterns, Spring Data JDBC with repository-based abstraction, and Spring Data JDBC enhanced with native SQL queries. Through these steps, you learned how to configure MySQL connectivity, map entities, implement CRUD operations, and integrate services into the business layer to display data in a Spring Boot application. Overall, the assignment reinforced best practices in data persistence, separation of concerns, and transitioning between low-level and high-level data access techniques.