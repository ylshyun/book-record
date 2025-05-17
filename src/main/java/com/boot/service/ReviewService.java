package com.boot.service;

import com.boot.dto.BookDTO;
import com.boot.dto.MemberDTO;
import com.boot.dto.ReviewBookDTO;
import com.boot.dto.ReviewDTO;
import com.boot.entity.Review;
import com.boot.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    // 리뷰 db에 저장하기
    @Transactional
    public void saveReview(ReviewDTO reviewDTO, BookDTO bookDTO, MemberDTO memberDTO) {
        // 리뷰 DTO -> Entity
        Review review = reviewDTO.toEntity(bookDTO, memberDTO);
        reviewRepository.save(review);
    }

    // isbn과 멤버 이메일을 통해 작성한 리뷰가 존재하는지 확인
    @Transactional(readOnly = true)
    public boolean existsByBookIsbnAndMemberEmail(String isbn, String memberEmail) {
        return reviewRepository.existsByBook_bookIsbnAndMember_memberEmail(isbn, memberEmail);
    }

    // 리뷰 수정
    @Transactional
    public void updateReview(Long reviewId, ReviewDTO reviewDTO, String memberEmail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("해당 리뷰가 존재하지 않습니다."));

        if (review.isWrittenBy(memberEmail)) {
            throw new SecurityException("해당 리뷰의 수정 권한이 없습니다.");
        }
        review.update(reviewDTO.getReviewTitle(), reviewDTO.getReviewContent());
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId, String memberEmail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        if (review.isWrittenBy(memberEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 리뷰의 삭제 권한이 없습니다.");
        }
        reviewRepository.delete(review);
    }

    // 로그인 한 사용자가 작성한 리뷰 목록 가져오기
    @Transactional(readOnly = true)
    public Page<ReviewBookDTO> getReviewsWithBooks(String memberEmail, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modifiedDate"));
        return reviewRepository.findReviewsWithBookInfoByMemberEmail(memberEmail, pageable);
    }

    // 모든 리뷰 목록 가져오기
    @Transactional(readOnly = true)
    public Page<ReviewBookDTO> findAllReviews(Pageable pageable) {
        return reviewRepository.findReviewsWithBookInfo(pageable);
    }
}

