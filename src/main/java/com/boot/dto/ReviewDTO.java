package com.boot.dto;

import com.boot.entity.Review;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewDTO {
    private Long reviewId;
    private String reviewTitle;
    private String reviewContent;
    private String bookIsbn;
    private String memberEmail;
    private String memberName;
    private String createDate;
    private String modifiedDate;

    @Builder
    public ReviewDTO(Long reviewId, String reviewTitle, String reviewContent, String bookIsbn,
                     String memberEmail, String memberName, LocalDateTime createDate, LocalDateTime modifiedDate) {
        this.reviewId = reviewId;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.bookIsbn = bookIsbn;
        this.memberEmail = memberEmail;
        this.memberName = memberName;
        this.createDate = (createDate != null)
                ? createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : null;
        this.modifiedDate = modifiedDate != null ? modifiedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : null;
    }


    public Review toEntity(BookDTO bookDTO, MemberDTO memberDTO) {
        return Review.builder()
                .reviewTitle(this.reviewTitle)
                .reviewContent(this.reviewContent)
                .book(bookDTO.toEntity())
                .member(memberDTO.toEntity())
                .build();
    }

    public static ReviewDTO fromEntity(Review review) {
        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .reviewTitle(review.getReviewTitle())
                .reviewContent(review.getReviewContent())
                .bookIsbn(review.getBook().getBookIsbn())
                .memberEmail(review.getMember().getMemberEmail())
                .memberName(review.getMember().getMemberName())
                .createDate(review.getCreateDate())
                .modifiedDate(review.getModifiedDate())
                .build();
    }
}
