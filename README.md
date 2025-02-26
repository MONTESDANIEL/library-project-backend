# Library Management System - Microservicio de Libros

Este proyecto es un microservicio para la gestión de libros en una biblioteca. Proporciona operaciones CRUD para libros y manejo de préstamos. Está desarrollado con **Spring Boot** y utiliza **MySQL** como base de datos.

## Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **MySQL**
- **Swagger (OpenAPI)**

## Configuración e instalación

### 1. Configuración del entorno

Antes de ejecutar el microservicio, asegúrese de tener instalados los siguientes requisitos:

- **Java 17 o superior**
- **Maven 3.8+**
- **MySQL 8+**

### 2. Creación de la base de datos

Ejecute el siguiente script en MySQL para crear la base de datos y su estructura inicial:

```sql
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- TABLA: usuarios
-- Contiene la información de los usuarios que solicitan préstamos.
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    phone VARCHAR(15),
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- TABLA: libros
-- Contiene los libros disponibles en la biblioteca.
CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    genre VARCHAR(50),
    availability BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- TABLA: prestamos
-- Registra los préstamos de los libros, enlazando usuarios y libros.
CREATE TABLE loans (
    id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    user_id INT NOT NULL,
    loan_date DATE NOT NULL,
    return_date DATE NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 3. Configuración de propiedades

El microservicio utiliza el archivo application.properties para configurar la conexión a la base de datos y otros parámetros. A continuación se muestra un ejemplo de la configuración:

properties
Copiar
Editar
spring.application.name=library-project
server.port=8081

## Configuración de la base de datos

```sh
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=root
spring.datasource.password=admin
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
```

## Endpoints del Microservicio

El microservicio expone los siguientes endpoints:

| Método | Endpoint                        | Descripción                          |
| ------ | ------------------------------- | ------------------------------------ |
| GET    | `/api/book/listAllBooks`        | Lista todos los libros               |
| POST   | `/api/book/addBook`             | Agrega un nuevo libro                |
| PUT    | `/api/book/updateBook`          | Actualiza la información de un libro |
| DELETE | `/api/book/deleteBook/{bookId}` | Elimina un libro por ID              |

## Ejecución del Microservicio

Para ejecutar el microservicio localmente, puedes usar el siguiente comando:

```sh
mvn spring-boot:run
```

O bien, compilar y ejecutar el jar generado:

```sh
mvn clean package
java -jar target/library-project.jar
```

## Documentación con Swagger

Swagger está habilitado y puede ser accedido desde la siguiente URL:

http://localhost:8081/swagger-ui/index.html
