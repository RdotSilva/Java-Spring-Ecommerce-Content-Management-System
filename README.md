# E-Commerce Content Management System (CMS)

Full stack web application used to manage the creation and modification of an E-Commerce platform.

Users can add products with an image, description and price. Users can also easily change the order of pages,
categories, and products.

*Currently a work in progress*


### Prerequisites

You must create an application.properties file with your database/hibernate configuration options.

1. Create a new file: \src\main\resources\application.properties

```
# Database configurations
spring.datasource.url=YOUR_DATABASE_URL
spring.datasource.username=USERNAME
spring.datasource.password=PASSWORD

# Hibernate configurations
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialet=YOUR_DIALECT
```

## Installation

1. Install Maven dependencies.

```
mvn install
```

## Running the server

1. Start SpringBoot server.

```
mvn spring-boot:run
```

## Built With

- Java
- Maven
- Spring
- Thymeleaf
- Hibernate
- Spring Security
- MySQL
- IntelliJ IDEA


## Screenshots

![Coming Soon](https://upload.wikimedia.org/wikipedia/commons/8/80/Comingsoon.png "Coming Soon")
