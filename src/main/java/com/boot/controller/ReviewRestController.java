package com.boot.controller;

import com.boot.config.security.CustomMemberDetails;
import com.boot.dto.BookDTO;
import com.boot.dto.MemberDTO;
import com.boot.dto.ReviewDTO;
import com.boot.repository.MemberRepository;
import com.boot.service.BookService;
import com.boot.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class ReviewRestController {

    private final BookService bookService;
    private final MemberRepository memberRepository;
    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping("/{isbn}/reviews")
    @ResponseBody
    public ResponseEntity<String> createReview(
            @PathVariable("isbn") String isbn,
            @RequestBody ReviewDTO reviewDTO,
            @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        try {
            BookDTO bookDTO = bookService.searchBookByIsbn(isbn);
            MemberDTO memberDTO = MemberDTO.fromEntity(memberRepository.findByMemberEmail(memberDetails.getUsername()));
            if (reviewDTO.getReviewContent() == null || reviewDTO.getReviewContent().length() < 50) {
                return ResponseEntity.badRequest().body("최소 50자 이상 작성해야 합니다.");
            }
            reviewService.saveReview(reviewDTO, bookDTO, memberDTO);
            return ResponseEntity.ok("리뷰 작성 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 작성 실패: " + e.getMessage());
        }
    }

    // 리뷰 목록 불러오기
    @GetMapping("/{isbn}/reviews")
    @ResponseBody
    public Page<ReviewDTO> getReviews(
            @PathVariable("isbn") String isbn,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("modifiedDate")));
        return bookService.findReviewsByBookIsbn(isbn, pageable);
    }

    // 리뷰 수정
    @PutMapping("/{isbn}/reviews/{reviewId}")
    public ResponseEntity<String> editReview(
            @PathVariable String isbn,
            @PathVariable Long reviewId,
            @RequestBody ReviewDTO reviewDTO,
            @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        if (reviewDTO.getReviewContent() == null || reviewDTO.getReviewContent().length() < 50) {
            return ResponseEntity.badRequest().body("최소 50자 이상 작성해야 합니다.");
        }
        reviewService.updateReview(reviewId, reviewDTO, memberDetails.getUsername());
        return ResponseEntity.ok("리뷰 수정이 완료되었습니다.");
    }

    // 리뷰 삭제
    @DeleteMapping("/{isbn}/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable String isbn,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        try{
            reviewService.deleteReview(reviewId, memberDetails.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제에 실패했습니다.");
        }
    }
}
