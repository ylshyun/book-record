package com.boot.service;

import com.boot.dto.BookDTO;
import com.boot.dto.ReviewDTO;
import com.boot.entity.Book;
import com.boot.entity.Review;
import com.boot.repository.BookRepository;
import com.boot.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final NaverApi naverApi;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    // 키워드로 책 검색
    public Page<BookDTO> searchBooks(String keyword, Pageable pageable) throws Exception {
        // 1. 책 제목 또는 작가로 DB에서 책 검색
        Page<Book> books = bookRepository.findByBookTitleContainingOrBookAuthorContaining(keyword, keyword, pageable);

        // 2. DB에 검색 결과가 없으면, 네이버 API에서 책 정보를 가져와서 db에 저장
        if (books.isEmpty()) {
            List<BookDTO> bookDtoList = naverApi.searchBooksByKeyword(keyword);
            // 네이버 API에서 가져온 책 정보를 저장
            saveBooksIfNotExist(bookDtoList);

            // 검색 결과를 DTO로 변환하여 반환
            return bookRepository.findByBookTitleContainingOrBookAuthorContaining(keyword, keyword, pageable)
                    .map(BookDTO::fromEntity);
        }

        // 3. Page<Book> -> Page<BookDTO> (Entity -> DTO 변환)
        return books.map(BookDTO::fromEntity);
    }

    @Transactional
    // 네이버 API에서 가져온 책 정보를 db에 저장
    public void saveBooksIfNotExist(List<BookDTO> bookDtoList) {
        // ISBN 목록 추출
        List<String> isbns = bookDtoList.stream().map(BookDTO::getBookIsbn).collect(Collectors.toList());
        // DB에서 해당 ISBN이 있는 책들 조회
        List<Book> existingBooks = bookRepository.findAllByBookIsbnIn(isbns);
        // 이미 존재하는 ISBN 목록 생성
        List<String> existingIsbns = existingBooks.stream().map(Book::getBookIsbn).toList();
        // 존재하지 않는 ISBN에 해당하는 책들만 필터링 후 저장
        List<BookDTO> newBooks = bookDtoList.stream()
                .filter(bookDTO -> !existingIsbns.contains(bookDTO.getBookIsbn()))
                .toList();
        // 새로 추가할 책들을 DB에 저장
        for (BookDTO bookDTO : newBooks) {
            Book newBook = bookDTO.toEntity();
            bookRepository.save(newBook);
        }
    }

    @Transactional(readOnly = true)
    // db에서 isbn으로 책 검색
    public BookDTO searchBookByIsbn(String isbn) {
        return bookRepository.findByBookIsbn(isbn)
                .map(BookDTO::fromEntity)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    // db에서 isbn으로 작성된 리뷰 검색
    public Page<ReviewDTO> findReviewsByBookIsbn(String isbn, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByBook_bookIsbn(isbn, pageable);
        return (reviews != null) ? reviews.map(ReviewDTO::fromEntity) : Page.empty();
    }
}