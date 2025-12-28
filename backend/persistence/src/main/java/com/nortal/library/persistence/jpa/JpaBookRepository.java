package com.nortal.library.persistence.jpa;

import com.nortal.library.core.domain.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBookRepository extends JpaRepository<Book, String> {
    public List<Book> findByLoanedTo(String loanedTo);
}
