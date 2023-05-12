# Web-приложение для изучения RestFul API архитектуры

[![Java CI with Maven](https://github.com/svoh86/job4j_url_shortcut/actions/workflows/maven.yml/badge.svg)](https://github.com/svoh86/job4j_url_shortcut/actions/workflows/maven.yml)

+ [О проекте](#О-проекте)
+ [Технологии](#Технологии)
+ [Требования к окружению](#Требования-к-окружению)
+ [Запуск проекта](#Запуск-проекта)
+ [Взаимодействие с приложением](#Взаимодействие-с-приложением)
+ [Контакты](#Контакты)

## О проекте

RESTFul сервис для обеспечения безопасности пользователей путем замены URL на сгенерированные уникальные коды.
URL передается в сервис. Сервис сохраняет его и выдает ему уникальный код, который служит новым адресом.
При регистрации каждому сайту выдается пара пароль и логин.
Авторизация в сервисе через JWT.
Зарегистрированные сайты могут сохранять URL и получать статистику по ним.
Переадресация доступна неавторизованным пользователям.

## Технологии

+ **Maven 3.8**
+ **Spring Boot 2.7.11**, **Spring Data JPA**, **Spring Security**
+ **Lombok 1.18.26** 
+ **PostgreSQL 14**
+ **Тестирование:** **Liquibase 4.20.0**, **H2 2.1.214**, **Mockito 4.5.1**
+ **Java 17**
+ **Checkstyle 3.2.0**

## Требования к окружению
+ **Java 17**
+ **Maven 3.8**
+ **PostgreSQL 14**

## Запуск проекта
Перед запуском проекта необходимо настроить подключение к БД в соответствии с параметрами,
указанными в src/main/resources/application.properties, или заменить на свои параметры.

Варианты запуска приложения:
1. Упаковать проект в jar архив (job4j_url_shortcut/target/job4j_url_shortcut-1.0.jar):
``` 
mvn package
``` 
Запустить приложение:
```
java -jar job4j_url_shortcut-1.0.jar 
```
2. Запустить приложение:
```
mvn spring-boot:run
```
Для отправки запросов используйте Postman или аналог

## Взаимодействие с приложением
Сервисом могут пользоваться только зарегистрированные сайты. Каждому сайту выдается уникальные логин и пароль.
Для регистрации нужно отправить запрос:
```
POST /registration
```
![alt text](https://github.com/svoh86/job4j_url_shortcut/blob/master/img/registration.png)
В ответе получаем логин и пароль.
В случае, если сайт уже зарегистрирован получаем ответ:
![alt text](https://github.com/svoh86/job4j_url_shortcut/blob/master/img/failRegistration.png)

Далее необходимо пройти авторизацию, отправив запрос с login и password. 
```
POST /login
```
В заголовке ответа получим токен:
![alt text](https://github.com/svoh86/job4j_url_shortcut/blob/master/img/login.png)
Этот токен необходимо вставлять в заголовок запросов, требующих авторизации.

Поле того, как сайт зарегистрирован и авторизован, можно отправлять url-адреса и получать преобразованные ссылки.
```
POST /convert
```
В ответе получаем короткую ссылку(код):
![alt text](https://github.com/svoh86/job4j_url_shortcut/blob/master/img/convert.png)

Переадресация выполняется без авторизации. Сайт отправляет ссылку с кодом.
```
GET /redirect/УНИКАЛЬНЫЙ_КОД
```
В ответе заголовок REDIRECT со ссылкой на сайт и статусом 302:
![alt text](https://github.com/svoh86/job4j_url_shortcut/blob/master/img/redirect.png)

Для получения статистики необходимо авторизоваться и выполнить запрос:
```
GET /statistic
```
В ответе ссылки и количество переходов по ним:
![alt text](https://github.com/svoh86/job4j_url_shortcut/blob/master/img/statistic.png)

## Контакты

Свистунов Михаил Сергеевич

[![Telegram](https://img.shields.io/badge/Telegram-blue?logo=telegram)](https://t.me/svoh86)

Email: sms-86@mail.ru
