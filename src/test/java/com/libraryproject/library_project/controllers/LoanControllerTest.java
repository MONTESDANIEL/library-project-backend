package com.libraryproject.library_project.controllers;

import com.libraryproject.library_project.dto.LoanDTO;
import com.libraryproject.library_project.entities.Book;
import com.libraryproject.library_project.services.LoanService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    @BeforeEach
    void setUp() {
        // Setup MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
    }

    @Test
    void testCreateLoan() throws Exception {
        // Arrange: Crear un objeto LoanDTO (simulando datos de un préstamo)
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setBookId(1L);
        loanDTO.setUserId(1L);

        // Crear la respuesta esperada para el test (ApiResponse<Book>)
        ApiResponse<Book> apiResponse = new ApiResponse<>("Préstamo creado con éxito", null);
        ResponseEntity<ApiResponse<Book>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

        // Simulamos la respuesta del servicio para crear el préstamo
        when(loanService.createLoan(any(LoanDTO.class))).thenReturn(responseEntity);

        // Realizamos la petición POST para crear un préstamo
        mockMvc.perform(post("/api/loan/createLoan")
                .contentType("application/json")
                .content("{\"bookId\":1, \"userId\":1, \"loanDate\":\"2025-02-26\", \"dueDate\":\"2025-03-26\"}"))
                .andExpect(status().isCreated()) // Verifica que el código de estado es 201
                .andExpect(jsonPath("$.message").value("Préstamo creado con éxito")); // Verifica el mensaje

        // Verificamos que el servicio fue llamado una vez
        verify(loanService, times(1)).createLoan(any(LoanDTO.class));
    }

    @Test
    void testListLoans() throws Exception {
        // Simulamos la respuesta del servicio para listar los préstamos
        List<LoanDTO> loanDTOList = new ArrayList<>();
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setLoanId(1L);
        loanDTO.setBookId(1L);
        loanDTO.setUserId(1L);
        loanDTOList.add(loanDTO);

        ApiResponse<List<LoanDTO>> apiResponse = new ApiResponse<>("Lista de préstamos", loanDTOList);
        ResponseEntity<ApiResponse<List<LoanDTO>>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        // Simulamos la respuesta del servicio para listar los préstamos
        when(loanService.listLoans()).thenReturn(responseEntity);

        // Realizamos la petición GET para listar los préstamos
        mockMvc.perform(get("/api/loan/listLoans"))
                .andDo(print()) // Esto imprime la respuesta completa en la consola para depuración
                .andExpect(status().isOk()) // Verifica que el código de estado es 200
                .andExpect(jsonPath("$.message").value("Lista de préstamos")) // Verifica el mensaje
                .andExpect(jsonPath("$.data[0].loanId").value(1)) // Verifica el id del préstamo
                .andExpect(jsonPath("$.data[0].bookId").value(1)); // Verifica el id del libro

        // Verificamos que el servicio fue llamado una vez
        verify(loanService, times(1)).listLoans();
    }
}
