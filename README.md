# Project Name: QuickStartSpringBoot

## Overview
[Provide a brief overview of the project, including its purpose and key features.]

## Installation
1. Clone the repository from GitHub: `git clone https://github.com/faizakram/QuickStartSpringBoot.git`
2. Navigate to the project directory: `cd QuickStartSpringBoot`
3. Build the project: `./mvnw clean install`

## Getting Started
1. Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse).
2. Configure the application properties in `application.properties` or `application.yml` as per your environment.
3. Run the application:
4. Access the application at `http://localhost:8080` in your web browser.
5. open postman use this curl command
```curl
curl --location 'http://localhost:8080/free-marker' \
--header 'Content-Type: application/json' \
--header 'Cookie: SESSION=vwonazS21tqbsgQHWdO6Q8lsDD8EF3kQDIzznZNepAKLPkLYi9pOlkywIeYiF9lo' \
--data '{
  "name":"Faiz Akram",
  "message":"Hello World"  
}'
```
6. The output of this request is as string
```html
<!DOCTYPE html>
<html>
<body>
<p>Testing the template name :- Faiz Akram</p>
<p>And the messages are:- Hello World</p>
</body>
</html>s
```
## Project Structure
- **src/main/java**: Contains Java source code.
- `com.app`: Main package containing application code.
- **src/main/resources**: Contains application properties, static files, and templates.
- `application.yml`: Configuration properties for the application.
- `static/`: Directory for static resources like CSS, JavaScript, etc.
- `templates/`: Directory for HTML templates.
- **src/test**: Contains test source code.
- **pom.xml**: Maven project configuration file.

## Dependencies
- Spring Boot Starter Web: `spring-boot-starter-web`
- Spring Boot Starter Mail: `spring-boot-starter-mail`
- Spring Boot Starter Test: `spring-boot-starter-test`
- spring Boot Starter Freemarker: `spring-boot-starter-freemarker`
- [Add any additional dependencies here]

# Adding Email Services with FreeMarker Templates to Your Spring Boot Project
## API Documentation
# Adding Email Service to Spring Boot Project

This guide will walk you through the steps to addFreeMarker templates to your Spring Boot project. You'll learn how to integrate FreeMarker templates using Spring's.
## 1. Add Dependency
Ensure that you have the necessary dependency in your `pom.xml` file to enable email functionality:

## 1. Add Dependency
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```
If you're using Maven, add the following dependencies to your `pom.xml` file:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
```