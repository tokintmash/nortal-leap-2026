package com.nortal.library.persistence.jpa;

import com.nortal.library.core.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaBookRepository extends JpaRepository<Book, String> {
  public long countByLoanedTo(String loanedTo);

  @Modifying
  @Query(value = "DELETE FROM book_reservations WHERE member_id = :memberId", nativeQuery = true)
  void removeFromAllQueues(@Param("memberId") String memberId);
}
