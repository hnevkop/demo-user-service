# Project Description

## Project Status

![Project Build Status](https://github.com/hnevkop/demo-user-service/actions/workflows/main.yml/badge.svg)

## Project Overview

This application is a basic project management system that establishes a many-to-many relationship between Users and Projects. Users can be assigned to multiple projects, and each project can have many users associated with it.

## Technologies Used

- **Programming Language**: Java SDK version 21
- **Framework**: Spring Boot 3
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Authorization**: JWT

## Functionalities

1. **User Creation**: The application allows for the creation of new users. Once created, these users can be assigned to various projects.

2. **Project Creation**: New projects can also be created within the system. Similar to users, once a project has been created, users can then be assigned to it.

3. **Assigning Users to a Project**: This feature gives you the ability to assign any user to any project that needs their involvement.

4. **Deleting a User**: If a user is no longer necessary, the application provides functionality to remove that user from the system. However, it is important to note that this would also result in the user being removed from all related projects.

## Architecture

The application uses the Jakarta EE architecture with Spring MVC to handle web requests and return appropriate responses. Additionally, it uses Lombok for reducing boilerplate code, ensuring the codebase remains clean and readable.

With `Spring Data JPA`, the application standardizes the process of interacting with the database, abstracting most of the complexity of database access, and letting developers focus more on business logic rather than database intricacies.

The application uses JWT (JSON Web Tokens) for secure authorization and transmitting information between parties as JSON objects. This information can be verified and trusted because it is digitally signed.

Overall, this application serves as a simple yet functional interface for managing users and projects in a many-to-many relationship, backed by reliable technologies for solidity and dependability. 


## Java Version

This application is built using **Java JDK 21**, which is the long-term support (LTS) version as of the time of
writing (May 2024). You can verify your Java version by running the following command in your terminal:

```shell
shell java -version
```

If you do not have Java installed or have a different version, you can download JDK 21 from
the [official Oracle website](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).

## Maven

[Maven](https://maven.apache.org/) is a build and dependency management tool for Java applications. It is used in this
application to manage the project's build, report and documentation from a central piece of information, letting you
understand and control your project's build logic.

You can check if Maven is installed by running:

```shell
shell mvn -version
```

Maven is configured through the `pom.xml` file in the project root. In this file, you can specify dependencies, build
plugins, and other project information.

## Spring Boot

This project uses **Spring Boot 3+** . Spring Boot makes it easy to create stand-alone, production-grade Spring-based
applications that you can just run. We take an opinionated view of the Spring platform and third-party libraries so you
can get started with minimum fuss.

## OpenAPI 3.0

OpenAPI is a public description format for API services. This application uses OpenAPI 3.0 to generate and expose API
documentation and UI so that the API can be easily understood and accessed. Once the application is running, you can
access the OpenAPI UI by navigating to `http://localhost:8080/swagger-ui/index.html` in your web browser.

## Authentication

This application protects certain routes that require authentication. Credentials are stored in application.properties

1. To authenticate, send a POST request to the `/login` endpoint. This will return a JWT token in the response body.
2. For any subsequent requests to protected routes, include this token as a Bearer token in the `Authorization` header.

NOTE: users that are created are not used in JWT token. Check Out `JwtAuthenticationFilter`

## Database Configuration

This application uses a MySQL database. The configuration for the database can be found and modified in
the `application.properties` file.

## Docker

This application also supports Docker, allowing it to be packaged into a container for easier deployment and
scalability. This is particularly useful when deploying the application to a cloud service.

## Running The Application

The main entry point of the application is `UserServiceApplication.java`. Running this file will launch the application.
If you're using an IDE like IntelliJ IDEA or Eclipse, you can simply do a right-click on the file and choose "Run".

---

Author: [Premysl Hnevkovsky](premysl.hnevkovsky@gmail.com)

