package com.libraryproject.library_project.repositories;

import org.springframework.data.repository.CrudRepository;

import com.libraryproject.library_project.entities.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
    boolean existsByTitleAndAuthor(String title, String author);
}
