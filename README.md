# Library

## Description

REST service for library management using Java Spring Boot, JPA, Spring Validator, H2/Postgres database, JUnit and Mockito.

## Build

To build this project you need to execute following command:
```
./mvnw clean package
```
After this, an executable jar file will be created in ``/target`` folder.

## Launch

### Database

In this project, I used PostgreSQL with Liquibase to control changes. A PostgreSQL user used to connect to the database must have **privileges to perform DDL and DML operations**. 
Liquibase will handle all preparations, like table and constraint creation. If the project updates, Liquibase will perform all necessary database changes.

### Environmental variables

This project requires environmental variables to be successfully launched. Required variables with examples:
* Database Connection
```
DB_HOST=localhost:5432
DB_NAME=library
DB_USER=spring
DB_PASS=spring_pass
```
* Library settings
```
BORROW_LIMIT=10
```
### Launch command

To launch this application you need to execute the following command:
```
java -jar target/library-*version*.jar
```
