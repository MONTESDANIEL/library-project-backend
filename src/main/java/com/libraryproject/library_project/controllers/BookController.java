package com.libraryproject.library_project.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libraryproject.library_project.entities.Book;
import com.libraryproject.library_project.services.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
