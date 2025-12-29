package com.nortal.library.core.port;

import com.nortal.library.core.domain.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
  Optional<Book> findById(String id);

  List<Book> findAll();

  long countByLoanedTo(String memberId);

  Book save(Book book);

  void delete(Book book);

  boolean existsById(String id);
}
