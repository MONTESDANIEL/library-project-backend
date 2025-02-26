package com.libraryproject.library_project.repositories;

import org.springframework.data.repository.CrudRepository;

import com.libraryproject.library_project.entities.Loan;

public interface LoanRepository extends CrudRepository<Loan, Long> {

}
