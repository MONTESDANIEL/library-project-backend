package com.libraryproject.library_project.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.libraryproject.library_project.dto.LoanDTO;
import com.libraryproject.library_project.entities.Book;
import com.libraryproject.library_project.entities.Loan;
import com.libraryproject.library_project.entities.User;
import com.libraryproject.library_project.repositories.BookRepository;
import com.libraryproject.library_project.repositories.LoanRepository;
import com.libraryproject.library_project.repositories.UserRepository;
import com.libraryproject.library_project.utils.ApiResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    @Transactional
    public ResponseEntity<ApiResponse<Book>> createLoan(LoanDTO loanDTO) {
        try {
            // Buscar libro
            Book book = bookRepository.findById(loanDTO.getBookId()).get();

            // Verificar disponibilidad del libro
            if (!book.getAvailability()) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "El libro no esta disponible", null);
            }

            // Buscar usuario o crearlo si no existe
            User user = userRepository.findById(loanDTO.getUserId()).orElse(null);

            if (user == null) {
                user = new User();
                user.setName(loanDTO.getUserName());
                user.setId(loanDTO.getUserId());
                user.setEmail(loanDTO.getUserEmail());
                user.setPhone(loanDTO.getUserPhone());
                user.setAddress(loanDTO.getUserAddress());
                user = userRepository.save(user);
            }

            // Crear préstamo
            Loan loan = new Loan();
            loan.setUserId(user.getId());
            loan.setBookId(book.getId());
            loan.setLoanDate(loanDTO.getLoanDate());
            loan.setReturnDate(loanDTO.getReturnDate());

            // Marcar el libro como no disponible
            book.setAvailability(false);

            // Guardar préstamo y actualizar libro
            loanRepository.save(loan);
            bookRepository.save(book);

            return createApiResponse(HttpStatus.OK, "Préstamo registrado exitosamente", null);
        } catch (NoSuchElementException e) {
            return createApiResponse(HttpStatus.BAD_REQUEST, "El libro no existe", null);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    // Cambiar el tipo de retorno a ResponseEntity<ApiResponse<List<LoanDTO>>>
    public ResponseEntity<ApiResponse<List<LoanDTO>>> listLoans() {
        try {
            List<Loan> loanList = StreamSupport.stream(loanRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());

            // Verificamos si la lista de préstamos está vacía
            if (loanList.isEmpty()) {
                return createApiResponse(HttpStatus.NO_CONTENT, "No hay préstamos disponibles.", null);
            }

            List<LoanDTO> loans = new ArrayList<>();

            // Si hay préstamos, los procesamos
            for (Loan loan : loanList) {
                User user = userRepository.findById(loan.getUserId()).orElse(null);
                Book book = bookRepository.findById(loan.getBookId()).orElse(null);

                if (user != null && book != null) {
                    LoanDTO loanDTO = new LoanDTO();
                    loanDTO.setLoanId(loan.getId());
                    loanDTO.setUserName(user.getName());
                    loanDTO.setUserEmail(user.getEmail());
                    loanDTO.setUserId(user.getId());
                    loanDTO.setUserPhone(user.getPhone());
                    loanDTO.setUserAddress(user.getAddress());
                    loanDTO.setBookId(book.getId());
                    loanDTO.setLoanDate(loan.getLoanDate());
                    loanDTO.setReturnDate(loan.getReturnDate());
                    loans.add(loanDTO);
                }
            }

            return createApiResponse(HttpStatus.OK, "Los préstamos fueron consultados con éxito.", loans);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error al intentar consultar los préstamos.",
                    null);
        }
    }

    // Metodo para crear una respuesta con formato generalizado
    private <T> ResponseEntity<ApiResponse<T>> createApiResponse(HttpStatus status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>(message, data);
        return ResponseEntity.status(status).body(response);
    }

}
