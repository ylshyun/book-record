package com.boot;

import com.boot.dto.BookDTO;
import com.boot.dto.ReviewDTO;
import com.boot.entity.Book;
import com.boot.entity.Member;
import com.boot.entity.Review;
import com.boot.repository.BookRepository;
import com.boot.repository.MemberRepository;
import com.boot.repository.ReviewRepository;
import com.boot.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    ReviewRepository reviewRepository;

    @InjectMocks
    BookService bookService;

    @Test
    void searchBookByIsbn() {   // isbn으로 책 검색
        // given
        Book book = Book.builder()
                .bookTitle("책 제목")
                .bookIsbn("123456789")
                .bookImageURL("bookImage")
                .bookAuthor("작가")
                .bookDescription("123456")
                .bookDiscount(123456)
                .bookPublisher("출판사")
                .build();

        when(bookRepository.findByBookIsbn(book.getBookIsbn())).thenReturn(Optional.of(book));

        // when
        BookDTO bookDTO = bookService.searchBookByIsbn(book.getBookIsbn());

        // then
        assertThat(bookDTO).isNotNull();
        assertThat(bookDTO.getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(bookDTO.getBookIsbn()).isEqualTo(book.getBookIsbn());

        verify(bookRepository, times(1)).findByBookIsbn(book.getBookIsbn());
    }

    @Test
    void searchBookByIsbnNoExist(){ // 존재하지 않는 isbn으로 책 검색

        // given
        String bookIsbn = "999999";
        when(bookRepository.findByBookIsbn(bookIsbn)).thenReturn(Optional.empty());

        // when
        BookDTO bookDTO = bookService.searchBookByIsbn(bookIsbn);

        // then
        assertThat(bookDTO).isNull();
        verify(bookRepository, times(1)).findByBookIsbn(bookIsbn);
    }

    @Test
    void findReviewsByBookIsbn() {  // 해당 isbn으로 작성된 리뷰 검색
        // given
        String isbn = "123456789";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modifiedDate"));

        Book testBook = Book.builder()
                .bookIsbn(isbn)
                .bookTitle("테스트 책")
                .bookAuthor("테스트 작가")
                .bookPublisher("테스트 출판사")
                .bookDescription("15000")
                .bookImageURL("test_image.jpg")
                .build();

        Member testMember = Member.builder()
                .memberEmail("test@gmail.com")
                .memberPassword("password123")
                .memberName("테스터")
                .build();

        List<Review> reviews = IntStream.range(1, 50)
                .mapToObj(i -> Review.builder()
                        .reviewTitle("리뷰 제목 " + i)
                        .reviewContent("리뷰 내용 " + i)
                        .book(testBook)
                        .member(testMember)
                        .build())
                .toList();

        IntStream.range(0, reviews.size()).forEach(i -> {
            ReflectionTestUtils.setField(reviews.get(i), "createDate", LocalDateTime.now().minusDays(50 - i));
            ReflectionTestUtils.setField(reviews.get(i), "modifiedDate", LocalDateTime.now().minusDays(50 - i));
        });

        List<Review> sortedReviews = reviews.stream()
                .sorted(Comparator.comparing(Review::getModifiedDate).reversed()) // 최신순 정렬
                .toList();

        Page<Review> reviewPage = new PageImpl<>(sortedReviews, pageable, sortedReviews.size());

        // when
        when(reviewRepository.findByBook_bookIsbn(isbn, pageable)).thenReturn(reviewPage);
        Page<ReviewDTO> result = bookService.findReviewsByBookIsbn(isbn, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(49);
        assertThat(result.getContent().get(0).getReviewTitle()).isEqualTo("리뷰 제목 49");
        verify(reviewRepository, times(1)).findByBook_bookIsbn(isbn, pageable);
    }
}
