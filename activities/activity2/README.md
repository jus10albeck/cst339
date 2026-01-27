# Activity 2 - Spring Model View Controller (MVC)
CST-339: Java III  
Justin Albecker  
1/25/2026

## Part 1: Creating Models, Views, and Controllers Using Spring MVC

### Screenshots
- Screenshot of the first MVC test screen

    ![Screenshot 1](screenshots/screenshot1.png)

- Screenshot of the second MVC test screen

    ![Screenshot 2](screenshots/screenshot2.png)
   
- Screenshot of the third MVC test screen

    ![Screenshot 3](screenshots/screenshot3.png)

- Screenshot of the fourth MVC test screen

    ![Screenshot 4](screenshots/screenshot4.png)

- Screenshot of the fifth MVC test screen

    ![Screenshot 5](screenshots/screenshot5.png)

- Screenshot of the sixth MVC test screen

    ![Screenshot 6a](screenshots/screenshot6a.png)

    ![Screenshot 6b](screenshots/screenshot6b.png)

- Screenshot of the JAR file test

     ![Test JAR](screenshots/jarFileTest.png)

## Part 2: Creating Forms with Data Validation Using Spring MVC

### Screenshots

- Screenshot of the Login Form with no data validation

    ![Login Form](screenshots/screenshot7.png)

- Screenshot of the table

    ![Table](screenshots/screenshot8.png)

- Screenshot of the data validation errors

    ![Data validation errors](screenshots/screenshot9.png)

## Part 3: Creating Layouts Using Thymeleaf

### Screenshots

- Screenshot of the new Login page

    ![New Login screen](screenshots/screenshot10.png)

- Screenshot of the new Orders page

    ![Data validation errors](screenshots/screenshot11.png)

## Discussion Posts

1. Spring MVC supports the Model–View–Controller (MVC) design pattern by enforcing a clear separation of responsibilities across application layers (Web MVC framework, 2026). Incoming HTTP requests are handled by the DispatcherServlet, which acts as a front controller and routes requests to the appropriate @Controller methods based on handler mappings (Web MVC framework, 2026). Controllers process user input, interact with the model to prepare application data, and return a logical view name instead of rendering output directly (Web MVC framework, 2026). The model holds the application data, while the view (often implemented with Thymeleaf) uses that data to generate the user interface without containing business logic (Web MVC framework, 2026). This structure improves maintainability, scalability, and testability by isolating concerns.  
References:  
Spring (2026). Web MVC framework. Retrieved on 26 January 2026 from https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/mvc.html

    ![Diagram of the MVC Design Pattern](screenshots/Diagram.png)

2. 

# Conclusion
This activity strengthened my understanding of the Spring MVC framework by implementing controllers, models, and views to handle multiple request types and dynamic content. It also demonstrated how to build interactive web applications using Thymeleaf forms, model binding, and JSR-303 data validation to ensure proper user input handling. Finally, the use of Thymeleaf layouts reinforced best practices for creating reusable and consistent page designs, preparing me for building more structured and maintainable Spring Boot applications.