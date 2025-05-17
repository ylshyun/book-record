package com.boot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="book")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {
    @Id
    @Column(name = "book_isbn")
    private String bookIsbn;

    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "book_author")
    private String bookAuthor;

    @Column(name = "book_publisher")
    private String bookPublisher;

    @Column(name = "book_discount")
    private int bookDiscount;

    @Column(name = "book_imageURL")
    private String bookImageURL;

    @Column(name = "book_description", columnDefinition = "text")
    private String bookDescription;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book",cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Book(String bookTitle, String bookAuthor, String bookPublisher, int bookDiscount, String bookImageURL, String bookIsbn, String bookDescription) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.bookDiscount = bookDiscount;
        this.bookIsbn = bookIsbn;
        this.bookImageURL = bookImageURL;
        this.bookDescription = bookDescription;
    }
}
