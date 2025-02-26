package com.libraryproject.library_project.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.libraryproject.library_project.entities.Book;
import com.libraryproject.library_project.repositories.BookRepository;
import com.libraryproject.library_project.utils.ApiResponse;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public ResponseEntity<ApiResponse<Iterable<Book>>> listAllBooks() {
        try {
            Iterable<Book> books = bookRepository.findAll();
            return createApiResponse(HttpStatus.OK, "Los libros fueron consultados con éxito.", books);
        } catch (Exception e) {
            e.printStackTrace();
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error al intentar consultar los libros.", null);
        }
    }

    public ResponseEntity<ApiResponse<Book>> addBook(Book newBook) {
        try {

            newBook.setId(null);
            newBook.setTitle(capitalize(newBook.getTitle()));
            newBook.setAuthor(capitalize(newBook.getAuthor()));
            newBook.setGenre(capitalize(newBook.getGenre()));

            bookRepository.save(newBook);
            return createApiResponse(HttpStatus.OK, "El libro fue agregado con éxito.", newBook);

        } catch (DataIntegrityViolationException e) {
            logger.warn("Intento de agregar un libro duplicado: {} - {}", newBook.getTitle(), newBook.getAuthor());
            return createApiResponse(HttpStatus.BAD_REQUEST, "El libro ya existe en la biblioteca.", null);
        } catch (Exception e) {
            logger.error("Error al agregar el libro", e);
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error al intentar agregar el libro.", null);
        }
    }

    public ResponseEntity<ApiResponse<Book>> updateBook(Book updateBook) {
        try {
            Book book = bookRepository.findById(updateBook.getId()).get();

            book.setTitle(updateBook.getTitle());
            book.setAuthor(updateBook.getAuthor());
            book.setGenre(updateBook.getGenre());
            book.setAvailability(updateBook.getAvailability());

            bookRepository.save(book);

            return createApiResponse(HttpStatus.OK, "El libro fue actualizado con éxito.", book);

        } catch (NoSuchElementException e) {
            logger.warn("Intento de actualizar un libro inexistente: {}", updateBook.getId());
            return createApiResponse(HttpStatus.BAD_REQUEST, "El libro no existe.", null);
        } catch (Exception e) {
            logger.error("Error al actualizar el libro: {}", updateBook.getId(), e);
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error al intentar actualizar el libro.", null);
        }
    }

    public ResponseEntity<ApiResponse<String>> deleteBook(Long bookId) {
        try {
            bookRepository.deleteById(bookId);
            return createApiResponse(HttpStatus.OK, "El libro fue eliminado con éxito.", null);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Intento de eliminar un libro inexistente: {}", bookId);
            return createApiResponse(HttpStatus.BAD_REQUEST, "Error al eliminar, el libro no existe.", null);
        } catch (Exception e) {
            logger.error("Error al eliminar el libro: {}", bookId, e);
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error al intentar eliminar el libro.", null);
        }
    }

    // Formato para los datos que se guardan en la base
    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    // Metodo para crear una respuesta con formato generalizado
    private <T> ResponseEntity<ApiResponse<T>> createApiResponse(HttpStatus status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>(message, data);
        return ResponseEntity.status(status).body(response);
    }
}
