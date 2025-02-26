# Library Management System - Microservicio de Libros

Este proyecto es un microservicio para la gestión de libros en una biblioteca. Proporciona operaciones CRUD y manejo de préstamos. Está desarrollado con **Spring Boot** y utiliza **MySQL** como base de datos.

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

Ejecutar el siguiente script en MySQL para crear la base de datos y su estructura inicial:

```sql
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    genre VARCHAR(50),
    availability BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

```

### 3. Configuración de propiedades

El microservicio utiliza **application.properties** para configurar la conexión a la base de datos y otros parámetros. A continuación, se muestra un ejemplo de la configuración:

```properties
spring.application.name=library-project
server.port=8081

# Configuración de la base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=root
spring.datasource.password=admin
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración de Hibernate
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

## Implementación del Controlador

Ejemplo de código del controlador para la gestión de libros:

```java
@Tag(name = "Books", description = "Gestión de libros en la biblioteca")
@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Lista todos los libros")
    @GetMapping("/listAllBooks")
    public ResponseEntity<?> listAllBooks() {
        ResponseEntity<?> response = bookService.listAllBooks();
        return response;
    }

    @Operation(summary = "Agregar un nuevo libro")
    @PostMapping("/addBook")
    public ResponseEntity<?> addBook(@Valid @RequestBody Book newBook) {
        ResponseEntity<?> response = bookService.addBook(newBook);
        return response;
    }

    @Operation(summary = "Actualizar la información de un libro")
    @PutMapping("/updateBook")
    public ResponseEntity<?> updateBook(@Valid @RequestBody Book updateBook) {
        ResponseEntity<?> response = bookService.updateBook(updateBook);
        return response;
    }

    @Operation(summary = "Eliminar un libro")
    @DeleteMapping("/deleteBook/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        ResponseEntity<?> response = bookService.deleteBook(bookId);
        return response;
    }
}
```

## Ejecución del Microservicio

Para ejecutar el microservicio localmente:

```sh
mvn spring-boot:run
```

O compilar y ejecutar el `jar`:

```sh
mvn clean package
java -jar target/library-project.jar
```

## Documentación con Swagger

Swagger se encuentra habilitado y puede ser accedido desde:

```
http://localhost:8081/swagger-ui.html
```

## Contacto y Contribución

Si desea contribuir a este proyecto, puede clonar el repositorio y realizar un pull request con sus cambios. Para consultas, contactar al administrador del proyecto.
