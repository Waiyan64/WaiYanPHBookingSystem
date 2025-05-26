# Class Booking System

API Documentation
Access the API documentation after starting the application:
http://localhost:8084/swagger-ui/index.html#/

Configuration
The application uses the following configuration:
<pre>
server:
  port: 8084

spring:
  application:
    name: RestService
  
  datasource:
    url: jdbc:mysql://localhost:3306/wyp_javadev_bookingsystem?useUnicode=true&createDatabaseIfNotExist=true
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      maximum-pool-size: 10
  
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect

  # JWT Authentication
  app:
    jwt:
      password: d2Fzd29uZGVyZmlsbG5vaXNlc3BsaXRjcm93ZHNob3R1bmRlcmFycm93dGhyb3VnaG8=
      expiration-time: 600000
</pre>


