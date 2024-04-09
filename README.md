# Java Spring Boot Multitenant App

This repository contains a Java Spring Boot application that demonstrates how to implement multitenancy in a Spring Boot application.

## Features
- Multitenancy implementation using separate databases for each tenant
- RESTful API endpoints for managing tenants

## Configuration
The `application.yml` file contains the following configuration values:
- `database.url`: The URL for the database connection
- `database.username`: The username for the database
- `database.password`: The password for the database
- `accesskey`: AWS AccessKey for S3 bucket access
- `secretkey`: AWS SecretKey for S3 bucket access
- `bootstrap-servers` : Kafka IP address with port
