package com.libraryproject.library_project.repositories;

import org.springframework.data.repository.CrudRepository;

import com.libraryproject.library_project.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}