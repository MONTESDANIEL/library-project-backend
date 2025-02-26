package com.libraryproject.library_project.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libraryproject.library_project.dto.LoanDTO;
import com.libraryproject.library_project.services.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Loan", description = "Gestión de libros en la biblioteca")
@RestController
@RequestMapping("/api/Loan")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @Operation(summary = "Registrar un nuevo prestamo")
    @PostMapping("/createLoan")
    public ResponseEntity<?> createLoan(@RequestBody LoanDTO loan) {
        ResponseEntity<?> response = loanService.createLoan(loan);
        return response;
    }

    @Operation(summary = "Listar todos los prestamos")
    @PostMapping("/listLoans")
    public ResponseEntity<?> listLoans() {
        ResponseEntity<?> response = loanService.listLoans();
        return response;
    }

}
