package com.libraryproject.library_project.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;

import com.libraryproject.library_project.entities.Book;
import com.libraryproject.library_project.repositories.BookRepository;
import com.libraryproject.library_project.utils.ApiResponse;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book newBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        newBook = new Book();
        newBook.setTitle("Test Book");
        newBook.setAuthor("Test Author");
        newBook.setGenre("Fiction");
        newBook.setAvailability(true);
    }

    /*
     * Probar el comportamiento del método listAllBooks cuando se obtienen los
     * libros
     * correctamente.
     */
    @Test
    void testListAllBooks() {
        // Simulamos que bookRepository.findAll() devuelve una lista de libros
        when(bookRepository.findAll()).thenReturn(List.of(newBook));

        // Llamamos al método
        ResponseEntity<?> response = bookService.listAllBooks();

        // Verificamos la respuesta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("Los libros fueron consultados con éxito.", apiResponse.getMessage());
        assertNotNull(apiResponse.getData());
    }

    /*
     * Probar el comportamiento del método listAllBooks cuando ocurre un error al
     * intentar consultar los libros.
     */
    @Test
    void testListAllBooksError() {
        // Simulamos que bookRepository.findAll() lanza una excepción
        when(bookRepository.findAll()).thenThrow(new RuntimeException("Error"));

        // Llamamos al método
        ResponseEntity<?> response = bookService.listAllBooks();

        // Verificamos que el error sea manejado correctamente
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("Error al intentar consultar los libros.", apiResponse.getMessage());
    }

    /*
     * Probar el comportamiento del método addBook cuando se agrega un libro de
     * manera correcta.
     */
    @Test
    void testAddBook() {
        // Simulamos el comportamiento de bookRepository.save()
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        // Llamamos al método que estamos probando
        ResponseEntity<?> response = bookService.addBook(newBook);

        // Verificamos que la respuesta sea la esperada
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("El libro fue agregado con éxito.", apiResponse.getMessage());
    }

    /*
     * Probar el comportamiento del método addBook cuando se intenta agregar un
     * libro duplicado (simulando una excepción).
     */
    @Test
    void testAddBookWithDuplicate() {
        // Simulamos una excepción de duplicado en la base de datos
        when(bookRepository.save(any(Book.class))).thenThrow(new DataIntegrityViolationException("Duplicate"));

        // Llamamos al método
        ResponseEntity<?> response = bookService.addBook(newBook);

        // Verificamos que se maneja correctamente la excepción
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("El libro ya existe en la biblioteca.", apiResponse.getMessage());
    }

    /*
     * Probar el comportamiento del método updateBook cuando se actualiza un libro
     * correctamente.
     */
    @Test
    void testUpdateBook() {
        // Asignamos un id al libro para simular que ya existe en la base de datos
        newBook.setId(1L); // Asignamos un id ficticio para que el libro sea "existente"

        // Simulamos que el libro existe en la base de datos
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(newBook));

        // Simulamos el comportamiento de save() para que devuelva el libro actualizado
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        // Actualizamos los valores del libro
        newBook.setTitle("Updated Book");
        newBook.setAuthor("Updated Author");
        newBook.setGenre("Terror");
        newBook.setAvailability(true);

        // Llamamos al método
        ResponseEntity<?> response = bookService.updateBook(newBook);

        // Verificamos la respuesta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("El libro fue actualizado con éxito.", apiResponse.getMessage());
    }

    /*
     * Probar el comportamiento del método updateBook cuando se intenta actualizar
     * un
     * libro inexistente.
     */
    @Test
    void testUpdateBookNotFound() {
        // Simulamos que el libro no existe en la base de datos
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Llamamos al método
        ResponseEntity<?> response = bookService.updateBook(newBook);

        // Verificamos que el error sea manejado correctamente
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("El libro no existe.", apiResponse.getMessage());
    }

    /*
     * Probar el comportamiento del método deleteBook cuando se elimina un libro
     * correctamente.
     */
    @Test
    void testDeleteBook() {
        // Simulamos que el libro se elimina correctamente
        doNothing().when(bookRepository).deleteById(anyLong());

        // Llamamos al método
        ResponseEntity<?> response = bookService.deleteBook(1L);

        // Verificamos la respuesta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("El libro fue eliminado con éxito.", apiResponse.getMessage());
    }

    /*
     * Probar el comportamiento del método deleteBook cuando se intenta eliminar un
     * libro que no existe.
     */
    @Test
    void testDeleteBookNotFound() {
        // Simulamos que el libro no existe al intentar eliminarlo
        doThrow(new EmptyResultDataAccessException(1)).when(bookRepository).deleteById(anyLong());

        // Llamamos al método
        ResponseEntity<?> response = bookService.deleteBook(1L);

        // Verificamos que el error sea manejado correctamente
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("Error al eliminar, el libro no existe.", apiResponse.getMessage());
    }
}
