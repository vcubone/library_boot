# library_boot
## Library website with SpringBoot (Java) Backend
Application to demonstrate various parts of a service oriented SpringBoot application.

### Technology Stack
Component           | Technology
---                 | ---
Frontend            | HTML
Backend (WEB & REST)| [SpringBoot] (Java)
Security            | Session Based (Spring Security)
DB                  | PostgreSQL
Persistence         | JPA (Using Spring Data)
Server Build Tools  | Maven(Java) or Gradle
Documentation tools | Swagger
Test Tools          | Junit

## Folder Structure
```bash
PROJECT_FOLDER
│  README.md
│  pom.xml           
│  Dockerfile
└──[src]      
│  └──[main]      
│     └──[java/ru/batorov/library]      
│     └──[resources]
│        │  application.properties.origin #contains pattern of springboot cofigurations
│        │  schema-postgresql.sql  # Contains DB Script to create tables that executes during the App Startup          
│        │  data-postgresql.sql    # Contains DB Script to Insert data that executes during the App Startup (after schema.sql)
│        └──[templates]    # keep all html,css etc, resources
│  └──[test]             #Contains tests
│
└──[target]              #Java build files, auto-created after running java build: mvn install
```
## Prerequisites
Ensure you have this installed before proceeding further
- Java 19
- Maven 2.7.13
- PostgreSQL 16.0
- Docker(Optional)

## About
This is an implementation of an library website with authentication and authorization.
The goal of the project is to 
- Highlight techniques of making a website using [SpringBoot]

### Features of the Project
* Backend
  * Session Based Security (using Spring security)
  * In Memory DB with H2 for tests 
  * Using JPA and JDBC template to talk to relational database
  * How to request for paginated data

* Frontend
  * Different view depending on the user's role

* Build
  * The ability to launch the applications image from dockerhub
 
### Build (SpringBoot Java)
* Start PostgreSQL Server
* Add variables
```bash
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
jwt_secret=
```
* Run the command
```
  If you want jar
mvn install

  If you want just run
mvn spring-boot:run
```
### Build Docker Image
* Download [docker image](https://hub.docker.com/repository/docker/vcubone/library/general)
* Create docker-compose.yml file (change username and password if you want)
```
version: "3"
services:
  site:
    image: vcubone/library:main
    ports:
      - "8080:8080"
    environment:
      - spring.datasource.url=jdbc:postgresql://postgresqldb:5432/bootdb
      - spring.datasource.username=postgres
      - spring.datasource.password=password
      - jwt_secret=secret
    networks:
      - testnetwork
    depends_on:
      postgresqldb:
        condition: service_healthy
 
  postgresqldb:
    image: postgres:16-alpine
    networks:
      - testnetwork
    environment:
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=password
      - POSTGRES_DB=bootdb
    healthcheck:
            test: ["CMD", "pg_isready" ,"-d", "bootdb"]
            interval: 5s
            timeout: 5s
            retries: 10

networks:
  testnetwork: 
```
* Run the command
```
docker-compose up
```
(admin username = ad; admin password = ad)
### Accessing Application
Cpmponent         | URL
---               | ---
Frontend          |  http://localhost:8080
Swagger           |  http://localhost:8080/swagger-ui/
### Screenshots
#### Swagger
![Dashboard](/screenshots/swagger.png?raw=true)
---
#### Login
![Dashboard](/screenshots/login.png?raw=true)
---
#### Register
![Dashboard](/screenshots/register.png?raw=true)
---
#### Home
![Dashboard](/screenshots/home/home_page_unregistered.png?raw=true)
#### Books
![Dashboard](/screenshots/books/books.png?raw=true)
---
#### Books/search
![Dashboard](/screenshots/books/search/books_search.png?raw=true)
---
#### Books/new
Admin only
![Dashboard](/screenshots/books/new/books_new_admin.png?raw=true)
---
#### Books/book_id
The page changes depending on the user's role
* Unregistered
![Dashboard](/screenshots/books/id/books_id_unregistered_free.png?raw=true)
![Dashboard](/screenshots/books/id/books_id_unregistered_taken.png?raw=true)
* User
![Dashboard](/screenshots/books/id/books_id_user_free.png?raw=true)
The user is the owner
![Dashboard](/screenshots/books/id/books_id_user_taken_by_him.png?raw=true)
The user is not the owner
![Dashboard](/screenshots/books/id/books_id_user_taken_by_not_him.png?raw=true)
* Admin
![Dashboard](/screenshots/books/id/books_id_admin_free.png?raw=true)
![Dashboard](/screenshots/books/id/books_id_admintaken.png?raw=true)
---
#### Books/book_id/edit
Admin
![Dashboard](/screenshots/books/id/edit/books_id_edit_admin.png?raw=true)
---
#### People
Admin
![Dashboard](/screenshots/people/people_admin.png?raw=true)
---
#### People/new
Admin
![Dashboard](/screenshots/people/new/people_new_admin.png?raw=true)
---
#### People/person_id
Admin
![Dashboard](/screenshots/people/id/people_id_admin.png?raw=true)
---
#### People/person_id/edit
Admin
![Dashboard](/screenshots/people/id/edit/people_id_edit_admin.png?raw=true)
---
#### Account/main
Logged in user
![Dashboard](/screenshots/account/account_main.png?raw=true)
---
