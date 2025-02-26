package com.libraryproject.library_project.controllers;

import com.libraryproject.library_project.entities.Book;
import com.libraryproject.library_project.repositories.BookRepository;
import com.libraryproject.library_project.services.BookService;
import com.libraryproject.library_project.utils.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    private Book book;

    @BeforeEach
    void setUp() {
        // Setup MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        // Setup Book
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setGenre("Fiction");
    }

    @Test
    void testAddBook() throws Exception {
        // Simulamos la respuesta del servicio con ResponseEntity<ApiResponse<Book>>
        ApiResponse<Book> apiResponse = new ApiResponse<>("Libro agregado", book);
        ResponseEntity<ApiResponse<Book>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

        // Simulamos que el servicio devuelve un ResponseEntity<ApiResponse<Book>>
        when(bookService.addBook(any(Book.class))).thenReturn(responseEntity);

        // Realizamos la petición POST
        mockMvc.perform(post("/api/book/addBook")
                .contentType("application/json")
                .content("{\"title\":\"Test Book\", \"author\":\"Test Author\", \"genre\":\"Fiction\"}"))
                .andExpect(status().isCreated()) // Validamos el código de estado
                .andExpect(jsonPath("$.message").value("Libro agregado")) // Validamos el mensaje
                .andExpect(jsonPath("$.data.title").value("Test Book")) // Validamos el título
                .andExpect(jsonPath("$.data.author").value("Test Author")); // Validamos el autor

        // Verificamos que se haya llamado al servicio
        verify(bookService, times(1)).addBook(any(Book.class));
    }

    @Test
    void testDeleteBook() throws Exception {
        // Simulamos la respuesta del servicio con ResponseEntity<ApiResponse<String>>
        ApiResponse<String> apiResponse = new ApiResponse<>("Libro eliminado", null);
        ResponseEntity<ApiResponse<String>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        // Simulamos que el servicio devuelve un ResponseEntity<ApiResponse<String>>
        when(bookService.deleteBook(1L)).thenReturn(responseEntity);

        // Realizamos la petición DELETE
        mockMvc.perform(delete("/api/book/deleteBook/1"))
                .andExpect(status().isOk()) // Validamos el código de estado
                .andExpect(jsonPath("$.message").value("Libro eliminado")) // Validamos el mensaje
                .andExpect(jsonPath("$.data").isEmpty()); // Validamos que no hay datos adicionales

        // Verificamos que se haya llamado al servicio
        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    public void testListAllBooks() throws Exception {
        // Arrange: Create a mock list of books
        Book book1 = new Book();
        book1.setTitle("Title 1");
        book1.setAuthor("Author 1");
        book1.setGenre("Genre 1");

        Iterable<Book> mockBooks = Arrays.asList(book1);

        // Simulamos la respuesta del servicio con
        // ResponseEntity<ApiResponse<Iterable<Book>>>
        ApiResponse<Iterable<Book>> apiResponse = new ApiResponse<>("Los libros fueron consultados con éxito.",
                mockBooks);
        ResponseEntity<ApiResponse<Iterable<Book>>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        // Simulamos que el servicio devuelve un
        // ResponseEntity<ApiResponse<Iterable<Book>>>
        when(bookService.listAllBooks()).thenReturn(responseEntity);

        // Realizamos la petición GET
        mockMvc.perform(get("/api/book/listAllBooks"))
                .andExpect(status().isOk()) // Validamos el código de estado
                .andExpect(jsonPath("$.message").value("Los libros fueron consultados con éxito.")) // Validamos el
                                                                                                    // mensaje
                .andExpect(jsonPath("$.data[0].title").value("Title 1")) // Validamos el primer libro
                .andExpect(jsonPath("$.data[0].author").value("Author 1")); // Validamos el autor del primer libro

        // Verificamos que se haya llamado al servicio
        verify(bookService, times(1)).listAllBooks();
    }

    @Test
    void testUpdateBook() throws Exception {
        // Arrange: Crear un libro mock con setters
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Original Book");
        existingBook.setAuthor("Original Author");
        existingBook.setGenre("Fiction");
        existingBook.setAvailability(true);

        // Crear el libro actualizado (esto simula el resultado que debería estar en la
        // base de datos después de la actualización)
        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setTitle("Updated Book");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setGenre("NonFiction");
        updatedBook.setAvailability(true);

        // Crear la respuesta simulada para la actualización
        ApiResponse<Book> apiResponse = new ApiResponse<>("Libro actualizado", updatedBook);
        ResponseEntity<ApiResponse<Book>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        // Simulamos que el servicio devuelve el libro existente cuando se busca por id
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));

        // Simulamos que el servicio guarda el libro actualizado
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        // Simulamos que el servicio actualiza el libro
        when(bookService.updateBook(any(Book.class))).thenReturn(responseEntity);

        // Realizamos la petición PUT
        mockMvc.perform(put("/api/book/updateBook")
                .contentType("application/json")
                .content(
                        "{\"id\":1, \"title\":\"Updated Book\", \"author\":\"Updated Author\", \"genre\":\"NonFiction\", \"availability\":true}"))
                .andExpect(status().isOk()) // Validamos el código de estado
                .andExpect(jsonPath("$.message").value("Libro actualizado")) // Validamos el mensaje
                .andExpect(jsonPath("$.data.title").value("Updated Book")) // Validamos el nuevo título
                .andExpect(jsonPath("$.data.author").value("Updated Author")) // Validamos el nuevo autor
                .andExpect(jsonPath("$.data.genre").value("NonFiction")) // Validamos el nuevo género
                .andExpect(jsonPath("$.data.availability").value(true)); // Validamos la disponibilidad

    }

}
