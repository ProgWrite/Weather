# 🌤️ Weather Application
- Spring MVC web application for viewing weather in various locations.
- The application is available [here](http://5.188.29.53:8080/)

## 🚀 Features

### 👥 User Management
-  Registration
-  Authorization
-  Logout

### 📍 Location Management
- Search
- Add to list
- View location list with weather information
- Delete

## Technologies
- Java 17
- Spring MVC
- Tomcat 10
- PostgreSQL
- Thymeleaf
- Flyway
- Maven
- Hibernate


## 📋 Requirements for the application to work
- JDK 17+
- Apache Tomcat 10+
- PostgreSQL 12+
- Maven 3.6+


## 🚀  Installation Guide
1. Clone the [repository](https://github.com/ProgWrite/Weather.git).
2. Set up PostgreSQL database. Create database and user with appropriate permissions.
3. Register and get your [API KEY](https://openweathermap.org/).
4. Create setenv.bat file in your Tomcat's bin folder (\apache-tomcat-10.1.43\bin) with the following content:
```bat
@echo off
set DB_URL=jdbc:postgresql://localhost:5432/your_db_name
set DB_USERNAME=your_username
set DB_PASSWORD=your_password
set OPEN_WEATHER_API_KEY=your_api_key_here
```
5. Build the application (mvn clean package).
6. Start Tomcat server.
7. Open in browser:  http://localhost:8080/your_adress.

## 📚 Additional Information
This project was completed as part of the [Java Backend Learning Roadmap](https://zhukovsd.github.io/java-backend-learning-course/projects/weather-viewer/)
