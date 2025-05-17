package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReviewBookDTO {
    private Long reviewId;
    private String reviewTitle;
    private String reviewContent;
    private String bookIsbn;
    private String bookTitle;
    private String bookAuthor;
    private String bookPublisher;
    private String bookImageUrl;
    private LocalDateTime reviewCreateDate;
    private LocalDateTime reviewUpdateDate;
}
