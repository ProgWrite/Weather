# 🌤️ Weather Application
Spring MVC веб-приложение для просмотра погоды в различных локациях.

## 🚀 Возможности

### 👥 Работа с пользователями
-  Регистрация
-  Авторизация
-  Логаут

### 📍 Работа с локациями
- Поиск
- Добавление в список
- Просмотр списка локаций с погодой
- Удаление

## 🛠 Технологии
- Java 17
- Spring MVC
- Tomcat 10
- PostgreSQL
- Thymeleaf
- Flyway
- Maven
- Hibernate


## 📋 Требования для работы приложения
- JDK 17+
- Apache Tomcat 10+
- PostgreSQL 12+
- Maven 3.6+


## 🚀 Инструкция по запуску приложения
1. Клонируйте [репозиторий](https://github.com/ProgWrite/Weather.git)
2. Настройте базу данных PostgreSQL.
3. Зарегестрируйтесь и получите Ваш [API KEY](https://openweathermap.org/)
4. Настройте переменные окружения, для этого необходимо создать в папке bin где расположен ваш Tomcat (\apache-tomcat-10.1.43\bin) файл с следующим содержимым:
@echo off
set DB_URL=jdbc:postgresql://localhost:5432/your_db_name
set DB_USERNAME=your_username
set DB_PASSWORD=your_password
set OPEN_WEATHER_API_KEY=your_api_key_here
5. Соберите приложение (mvn clean package)
6. Запустите Tomcat
7. Откройте браузер http://localhost:8080/your_adress


Проект выполнен в рамках прохождения [роадмапа](https://zhukovsd.github.io/java-backend-learning-course/projects/weather-viewer/)
