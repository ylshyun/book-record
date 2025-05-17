package com.boot;

import com.boot.dto.BookDTO;
import com.boot.dto.MemberDTO;
import com.boot.dto.ReviewBookDTO;
import com.boot.dto.ReviewDTO;
import com.boot.entity.Book;
import com.boot.entity.Member;
import com.boot.entity.Review;
import com.boot.repository.ReviewRepository;
import com.boot.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;
    private ReviewDTO reviewDTO;
    private Book book;
    private BookDTO bookDTO;
    private Member member;
    private Member otherMember;
    private MemberDTO memberDTO;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .bookIsbn("1234567890")
                .bookTitle("테스트 책")
                .build();

        member = Member.builder()
                .memberEmail("test@gmail.com")
                .memberName("테스터")
                .build();

        otherMember = Member.builder()
                .memberEmail("other@gmail.com")
                .memberName("다른 사용자")
                .build();

        review = Review.builder()
                .reviewId(1L)
                .reviewTitle("테스트 리뷰")
                .reviewContent("리뷰 내용")
                .book(book)
                .member(member)
                .build();

        ReflectionTestUtils.setField(review, "createDate", LocalDateTime.now());

        bookDTO = BookDTO.fromEntity(book);
        memberDTO = MemberDTO.fromEntity(member);

        reviewDTO = ReviewDTO.builder()
                .reviewTitle("수정된 리뷰 제목")
                .reviewContent("수정된 리뷰 내용")
                .build();
    }

    @Test
    void saveReview() { // 리뷰 저장
        // given
        when(reviewRepository.save(ArgumentMatchers.<Review>any())).thenReturn(review);

        // when
        reviewService.saveReview(reviewDTO, bookDTO, memberDTO);

        // then
        verify(reviewRepository, times(1)).save(ArgumentMatchers.any());
    }

    @Test
    void existsByBookIsbnAndMemberEmail() {  // 책과 회원의 존재 유무 확인
        // given
        when(reviewRepository.
                existsByBook_bookIsbnAndMember_memberEmail(bookDTO.getBookIsbn(), memberDTO.getMemberEmail())).thenReturn(true);

        //when
        boolean result = reviewService.existsByBookIsbnAndMemberEmail(bookDTO.getBookIsbn(), memberDTO.getMemberEmail());

        //then
        verify(reviewRepository, times(1))
                .existsByBook_bookIsbnAndMember_memberEmail(bookDTO.getBookIsbn(), memberDTO.getMemberEmail());
        assertThat(result).isTrue();
    }

    @Test
    void updateReview() {   // 리뷰 수정 성공
        // given
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // when
        reviewService.updateReview(1L, reviewDTO, "test@gmail.com");

        // then
        assertThat(review.getReviewTitle()).isEqualTo("수정된 리뷰 제목");
        assertThat(review.getReviewContent()).isEqualTo("수정된 리뷰 내용");
    }

    @Test
    void updateReviewNotExist() {   // 존재하지 않는 리뷰에 대해 수정 시도
        // given
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reviewService.updateReview(99L, reviewDTO, "test@gmail.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("해당 리뷰가 존재하지 않습니다.");
    }

    @Test
    void updateReviewOtherId() {    // 다른 작성자가 리뷰 수정 시도
        // given
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // when & then
        assertThatThrownBy(() -> reviewService.updateReview(1L, reviewDTO, "other@gmail.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("해당 리뷰의 수정 권한이 없습니다.");
    }

    @Test
    void deleteReview() {   // 리뷰 삭제
        // given
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // when
        reviewService.deleteReview(1L, "test@gmail.com");

        // then
        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void deleteReviewNotExist() {   // 존재하지 않는 리뷰 삭제 시도
        // given
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reviewService.deleteReview(99L, "test@gmail.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("해당 리뷰를 찾을 수 없습니다.");
    }

    @Test
    void deleteReviewOtherId() {    // 다른 회원이 리뷰 삭제 시도
        // given
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // when & then
        assertThatThrownBy(() -> reviewService.deleteReview(1L, otherMember.getMemberEmail()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("해당 리뷰의 삭제 권한이 없습니다.");
    }

    @Test
    void getReviewsWithBooks() { // 로그인 한 사용자가 작성한 리뷰 목록 가져오기
        // given
        int page = 1;   // 2번째 페이지 (리뷰 제목 40~31)
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reviewUpdateDate"));
        List<ReviewBookDTO> allReviews = new ArrayList<>();

        for (long i = 1; i <= 50; i++) {
            allReviews.add(new ReviewBookDTO(
                    i,
                    "리뷰 제목 " + i,
                    "리뷰 내용 " + i,
                    "isbn" + i,
                    "책 제목 " + i,
                    "작가 " + i,
                    "출판사 " + i,
                    "책 이미지 " + i,
                    LocalDateTime.now().minusDays(i), // 생성 날짜
                    LocalDateTime.now().minusHours(i) // 수정 날짜
            ));
        }

        allReviews.sort(Comparator.comparing(ReviewBookDTO::getReviewUpdateDate));

        // 최신순으로 2페이지 (10~19) 가져오기 (리뷰 제목 40~31)
        int start = page * size;
        int end = Math.min(start + size, allReviews.size());
        List<ReviewBookDTO> pageContent = allReviews.subList(start, end);

        Page<ReviewBookDTO> mockPage = new PageImpl<>(pageContent, pageable, allReviews.size());

        when(reviewRepository.findReviewsWithBookInfoByMemberEmail("test@gmail.com", pageable))
                .thenReturn(mockPage);

// when
        Page<ReviewBookDTO> reviews = reviewService.getReviewsWithBooks("test@gmail.com", 1, 10);

// then
        assertThat(reviews).isNotNull();
        assertThat(reviews.getContent().size()).isEqualTo(10);
        assertThat(reviews.getTotalElements()).isEqualTo(50);
        assertThat(reviews.getContent().get(0).getReviewTitle()).isEqualTo("리뷰 제목 40");
        // 생성일이 현재 시각보다 과거인지 확인
        assertThat(reviews.getContent().get(0).getReviewCreateDate()).isBeforeOrEqualTo(LocalDateTime.now());
        verify(reviewRepository, times(1)).findReviewsWithBookInfoByMemberEmail("test@gmail.com", pageable);
    }

    @Test
    void findAllReviews() { // 모든 리뷰 목록 가져오기
        // given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "reviewUpdateDate"));
        List<ReviewBookDTO> allReviews = new ArrayList<>();

        for (long i = 1; i <= 50; i++) {
            allReviews.add(new ReviewBookDTO(
                    i,
                    "리뷰 제목 " + i,
                    "리뷰 내용 " + i,
                    "isbn" + i,
                    "책 제목 " + i,
                    "작가 " + i,
                    "출판사 " + i,
                    "책 이미지 " + i,
                    LocalDateTime.now().minusDays(i), // 생성 날짜
                    LocalDateTime.now().minusHours(i) // 수정 날짜
            ));
        }

        allReviews.sort(Comparator.comparing(ReviewBookDTO::getReviewUpdateDate));

        int start = page * size;
        int end = Math.min(start + size, allReviews.size());
        List<ReviewBookDTO> pageContent = allReviews.subList(start, end);

        Page<ReviewBookDTO> mockPage = new PageImpl<>(pageContent, pageable, allReviews.size());

        when(reviewRepository.findReviewsWithBookInfo(pageable)).thenReturn(mockPage);

        // when
        Page<ReviewBookDTO> reviews = reviewService.findAllReviews(pageable);

        // then
        assertThat(reviews).isNotNull();
        assertThat(reviews.getContent().size()).isEqualTo(10);
        assertThat(reviews.getTotalElements()).isEqualTo(50);
        assertThat(reviews.getContent().get(0).getReviewTitle()).isEqualTo("리뷰 제목 50");
        assertThat(reviews.getContent().get(0).getReviewCreateDate()).isBeforeOrEqualTo(LocalDateTime.now());
        verify(reviewRepository, times(1)).findReviewsWithBookInfo(pageable);
    }
}