package com.boot.dto;

import com.boot.entity.Book;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookDTO {
    private String bookTitle;
    private String bookAuthor;
    private String bookPublisher;
    private int bookDiscount;
    private String bookIsbn;
    private String bookImageURL;
    private String bookDescription;

    @Builder
    public BookDTO(String bookTitle, String bookAuthor, String bookPublisher, int bookDiscount, String bookIsbn, String bookImageURL, String bookDescription) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.bookDiscount = bookDiscount;
        this.bookIsbn = bookIsbn;
        this.bookImageURL = bookImageURL;
        this.bookDescription = bookDescription;
    }
    // DTO -> Entity
    public Book toEntity(){
        return Book.builder()
                .bookTitle(bookTitle)
                .bookAuthor(bookAuthor)
                .bookPublisher(bookPublisher)
                .bookDiscount(bookDiscount)
                .bookIsbn(bookIsbn)
                .bookImageURL(bookImageURL)
                .bookDescription(bookDescription)
                .build();
    }

    // Entity -> DTO
    public static BookDTO fromEntity(Book book) {
        return BookDTO.builder()
                .bookTitle(book.getBookTitle())
                .bookAuthor(book.getBookAuthor())
                .bookPublisher(book.getBookPublisher())
                .bookDiscount(book.getBookDiscount())
                .bookIsbn(book.getBookIsbn())
                .bookImageURL(book.getBookImageURL())
                .bookDescription(book.getBookDescription())
                .build();
    }
}
