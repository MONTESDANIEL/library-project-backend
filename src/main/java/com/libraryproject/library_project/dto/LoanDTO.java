package com.libraryproject.library_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LoanDTO {

    @NotBlank(message = "El nombre del usuario es obligatorio")
    private String userName;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no es válido")
    private String userEmail;

    @NotNull(message = "La cedula del usuario es obligatoria")
    private Long userId;

    @NotBlank(message = "El teléfono del usuario es obligatorio")
    private String userPhone;

    @NotBlank(message = "La dirección del usuario es obligatoria")
    private String userAddress;

    @NotNull(message = "El ID del libro es obligatorio")
    private Long bookId;

    @NotNull(message = "La fecha de préstamo es obligatoria")
    private LocalDate loanDate;

    @NotNull(message = "La fecha de devolución es obligatoria")
    private LocalDate returnDate;
}
