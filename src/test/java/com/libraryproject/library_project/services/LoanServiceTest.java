package com.libraryproject.library_project.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.libraryproject.library_project.dto.LoanDTO;
import com.libraryproject.library_project.entities.Book;
import com.libraryproject.library_project.entities.Loan;
import com.libraryproject.library_project.entities.User;
import com.libraryproject.library_project.repositories.BookRepository;
import com.libraryproject.library_project.repositories.LoanRepository;
import com.libraryproject.library_project.repositories.UserRepository;
import com.libraryproject.library_project.utils.ApiResponse;

public class LoanServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    private LoanDTO loanDTO;
    private Book book;
    private User user;
    private Loan loan;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup Book
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setGenre("Fiction");
        book.setAvailability(true);

        // Setup User
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPhone("1234567890");
        user.setAddress("Test Address");

        // Setup LoanDTO
        loanDTO = new LoanDTO();
        loanDTO.setBookId(book.getId());
        loanDTO.setUserId(user.getId());
        loanDTO.setUserName(user.getName());
        loanDTO.setUserEmail(user.getEmail());
        loanDTO.setUserPhone(user.getPhone());
        loanDTO.setUserAddress(user.getAddress());

        // Setup Loan
        loan = new Loan();
        loan.setId(1L);
        loan.setUserId(user.getId());
        loan.setBookId(book.getId());
        loan.setLoanDate(LocalDate.parse("2025-02-25"));
        loan.setReturnDate(LocalDate.parse("2025-03-01"));
    }

    /*
     * Probar el comportamiento del método createLoan cuando el libro está
     * disponible y el usuario ya existe.
     * Se espera que se cree correctamente el préstamo.
     */
    @Test
    void testCreateLoan() {
        // Simulamos que el libro está disponible
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        // Simulamos que el usuario ya existe
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Simulamos guardar el préstamo
        when(loanRepository.save(any())).thenReturn(new Loan());

        // Llamamos al método createLoan
        ResponseEntity<?> response = loanService.createLoan(loanDTO);

        // Verificamos que la respuesta sea la esperada
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /*
     * Probar el comportamiento del método createLoan cuando el libro no está
     * disponible.
     * Se espera que el sistema devuelva un mensaje de error indicando
     * que el libro no está disponible.
     */
    @Test
    void testCreateLoanBookNotAvailable() {
        // Simulamos que el libro no está disponible
        book.setAvailability(false);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        // Llamamos al método createLoan
        ResponseEntity<?> response = loanService.createLoan(loanDTO);

        // Verificamos que la respuesta sea la esperada para libro no disponible
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El libro no esta disponible", ((ApiResponse<?>) response.getBody()).getMessage());
    }

    /*
     * Probar el comportamiento del método createLoan cuando el usuario no existe.
     * Se espera que el sistema cree un nuevo usuario y registre el préstamo.
     */
    @Test
    void testCreateLoanUserNotFound() {
        // Simulamos que el usuario no existe
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Simulamos que se guarda un nuevo usuario
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Simulamos guardar el préstamo
        when(loanRepository.save(any())).thenReturn(new Loan());

        // Llamamos al método createLoan
        ResponseEntity<?> response = loanService.createLoan(loanDTO);

        // Verificamos que la respuesta sea la esperada
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /*
     * Probar el comportamiento del método listLoans cuando se encuentran préstamos.
     * Se espera que se retorne una lista de préstamos con los detalles de usuario y
     * libro.
     */
    @Test
    void testListLoans() {
        // Simulamos que se encuentran préstamos
        when(loanRepository.findAll()).thenReturn(Arrays.asList(loan));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        // Llamamos al método listLoans
        ResponseEntity<?> response = loanService.listLoans();

        // Verificamos que la respuesta sea la esperada
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /*
     * Probar el comportamiento del método listLoans cuando no hay préstamos.
     * Se espera que el sistema retorne una respuesta con estado NO_CONTENT, un
     * mensaje adecuado y un campo data con null.
     */
    @Test
    void testListLoansNoLoans() {
        // Simulamos que no hay préstamos
        when(loanRepository.findAll()).thenReturn(Arrays.asList());

        // Llamamos al método listLoans
        ResponseEntity<?> response = loanService.listLoans();

        // Verificamos que la respuesta sea la esperada cuando no hay préstamos
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verificamos que el cuerpo de la respuesta no sea null (debe ser un
        // ApiResponse)
        assertNotNull(response.getBody());

        // Obtenemos el ApiResponse del cuerpo de la respuesta
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();

        // Verificamos que el mensaje sea el esperado
        assertEquals("No hay préstamos disponibles.", apiResponse.getMessage());

        // Verificamos que el campo data sea null
        assertNull(apiResponse.getData());

        // Verificamos que el timestamp esté presente
        assertNotNull(apiResponse.getTimestamp());
    }

}
