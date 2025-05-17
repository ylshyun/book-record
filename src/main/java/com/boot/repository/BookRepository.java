package com.boot.repository;

import com.boot.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    //Optional을 사용하여 null 체크 후 예외 처리
    Optional<Book> findByBookIsbn(String isbn);
    // ISBN 리스트로 책을 찾는 메서드
    List<Book> findAllByBookIsbnIn(List<String> isbns);
    Page<Book> findByBookTitleContainingOrBookAuthorContaining(String bookTitle, String author, Pageable pageable);
}
