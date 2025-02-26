package com.libraryproject.library_project.utils;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {

    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

}
