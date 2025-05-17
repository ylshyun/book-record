package com.boot.repository;

import com.boot.dto.ReviewBookDTO;
import com.boot.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByBook_bookIsbn(String bookIsbn, Pageable pageable);
    Boolean existsByBook_bookIsbnAndMember_memberEmail(String bookIsbn, String memberEmail);

    @Query("SELECT new com.boot.dto.ReviewBookDTO(r.reviewId, r.reviewTitle, r.reviewContent, " +
            "b.bookIsbn, b.bookTitle, b.bookAuthor, b.bookPublisher, b.bookImageURL, r.createDate, r.modifiedDate) " +
            "FROM Review r JOIN r.book b " +
            "WHERE r.member.memberEmail = :memberEmail")
    Page<ReviewBookDTO> findReviewsWithBookInfoByMemberEmail(@Param("memberEmail") String memberEmail, Pageable pageable);

    @Query("SELECT new com.boot.dto.ReviewBookDTO(r.reviewId, r.reviewTitle, r.reviewContent, " +
            "b.bookIsbn, b.bookTitle, b.bookAuthor, b.bookPublisher, b.bookImageURL, r.createDate, r.modifiedDate)" +
            "FROM Review r JOIN r.book b ")
    Page<ReviewBookDTO> findReviewsWithBookInfo(Pageable pageable);
}
