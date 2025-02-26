package com.libraryproject.library_project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio.")
    @Size(max = 50, message = "El título no puede tener más de 50 caracteres.")
    @Pattern(regexp = "^[\\p{L}0-9\\s.,'-áéíóúÁÉÍÓÚñÑ]+$", message = "El título no puede contener caracteres especiales.")
    @Column(nullable = false, length = 50)
    private String title;

    @NotBlank(message = "El autor es obligatorio.")
    @Size(max = 50, message = "El autor no puede tener más de 50 caracteres.")
    @Pattern(regexp = "^[\\p{L}\\s.,'-áéíóúÁÉÍÓÚñÑ]+$", message = "El nombre del autor no puede contener números.")
    @Column(nullable = false, length = 50)
    private String author;

    @Size(max = 50, message = "El género no puede tener más de 50 caracteres.")
    @Pattern(regexp = "^[\\p{L}\\s,.áéíóúÁÉÍÓÚñÑ]+$", message = "El género solo puede contener letras.")
    @Column(length = 50)
    private String genre;

    @NotNull(message = "La disponibilidad es obligatoria.")
    @Column(nullable = false)
    private Boolean availability = true;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
