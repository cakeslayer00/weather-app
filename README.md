# weather-app

This pet-project provides an ability to look for weather in a certain location. Sole purpose is to learn Spring Framework, create app without sugary Spring Boot, but with Spring MVC where one set up everything by himself (e.g Beans). Application based on MVC pattern and lacks extended features as providing forecasts.

# Table of content

[Technologies](#used-technologies)

[How to install](#how-to-install-and-run)

[Credits](#credits)

# Used technologies

- Spring Framework
- Thymeleaf
- Hibernate
- Flyway
- PostgreSQL
- JUnit 5 & Mockito
- BCrypt for encoding passwords

# How to install and run

1. Simply clone repository using `git clone https://github.com/cakeslayer00/weather-app`
2. Enter directory `cd weather-app`
3. Execute docker command `docker-compose up --build`, assuming you have docker installed
4. Application will be available on `localhost:8080`

  For the development environment recommended to set up application.properties with right configuration, and build and run app on tomcat

# Credits

This project implemented based on requirements provided by Sergey Zhukov's roadmap.
Here's link:
https://zhukovsd.github.io/java-backend-learning-course/projects/currency-exchange/
