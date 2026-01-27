# CST339 - Activity 1 - Introduction to Spring Boot
Justin Albecker

1/25/2026

## Part 1: Tools Installation and Validation

### Screenshots

- Screenshot of the proof of Spring Boot installation

     ![Spring Tool Install Proof](screenshots/SpringBootInstall.png)

- Screenshot of the console output

    ![Spring Console Output](screenshots/SpringHelloWorld.png)

- Screenshot of the error page

    ![Error page](screenshots/localhostScreenshot1.png)

- Screenshot of the Hello World index.html page

    ![Hello World Page](screenshots/HelloWorldPage.png)

## Part 2: Learning Maven

### Screenshots

- Screenshot of the Maven Build configurations

    ![Maven Configurations](screenshots/RunConfigurations.png)

- Screenshot of the Maven Console, with the "BUILD SUCCESS" message.
    ![Build Success](screenshots/SuccessfulBuild.png)

- Screenshots of the JAR file execution.
    ![Build Success](screenshots/SuccessfulBuild2.png)

    ![localhost:8080](screenshots/HelloWorldPage2.png)

## Research Questions

1. Spring Boot and the traditional Spring Framework both support building dynamic web applications, but they differ significantly in development approach and complexity. The Spring Framework is highly flexible and modular, giving developers greater control over configuration, dependency management, and application setup (A Comparison Between Spring and Spring Boot, 2024). However, building dynamic web applications with Spring often requires extensive XML or Java-based configuration, manual dependency selection, and external application servers, which can slow initial development. In contrast, Spring Boot is built on top of Spring and is designed to simplify and accelerate development (A Comparison Between Spring and Spring Boot, 2024). It uses opinionated defaults, auto-configuration, and “starter” dependencies to reduce boilerplate code and setup time (A Comparison Between Spring and Spring Boot, 2024). This makes Spring Boot especially well-suited for rapid development of dynamic, production-ready web applications.  
References:  
A Comparison Between Spring and Spring Boot (2024). Baeldung. Retrieved on 21 January, 2026 from https://www.baeldung.com/spring-vs-spring-boot

2. Gradle is a modern build and dependency management tool that differs from Maven mainly in flexibility, performance, and configuration style. While Maven relies on XML-based pom.xml files with a fixed lifecycle and strict conventions, Gradle uses a Groovy- or Kotlin-based DSL that allows highly customizable build logic written as code (Obregon, 2025). This makes Gradle more concise and expressive, especially for complex projects. Gradle also improves performance through incremental builds, task caching, and parallel execution, which can significantly reduce build times compared to Maven (Obregon, 2025). In contrast, Maven emphasizes simplicity and consistency, making it easier for beginners and ensuring standardized project structures (Obregon, 2025). Overall, Gradle is favored for advanced customization and speed, while Maven is often chosen for stability and convention-driven builds.  
References:  
Obregon, A (2025). Medium. Gradle vs Maven for Java Developers.

https://medium.com/@AlexanderObregon/gradle-vs-maven-for-java-developers-145f4e5d1603

## Conclusion

This activity provided hands-on experience setting up a Java development environment using Spring Tool Suite and creating a basic Spring Boot “Hello World” application to validate the setup. It also introduced Maven as a build and dependency management tool, reinforcing how projects are structured, built, and packaged into executable JAR files. Overall, the assignment established a strong foundation in Spring Boot and Maven that will be essential for developing and managing more complex Java web applications later in this class.
   