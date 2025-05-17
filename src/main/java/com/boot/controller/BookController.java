package com.boot.controller;

import com.boot.config.security.CustomMemberDetails;
import com.boot.dto.BookDTO;
import com.boot.service.BookService;
import com.boot.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ReviewService reviewService;

    // 검색한 책 리스트 보기
    @GetMapping("/search")
    public String searchBooks(@RequestParam("keyword") String keyword,
                              @PageableDefault(size = 10) Pageable pageable, Model model) throws Exception {
        // 검색어로 API 호출
        Page<BookDTO> books = bookService.searchBooks(keyword, pageable);
        // 검색 결과를 뷰로 전달
        model.addAttribute("bookDtoList", books);
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", books.getTotalPages());
        model.addAttribute("keyword", keyword);
        return "book/search";
    }

    // 검색한 리스트 중 선택한 책 상세 보기
    @GetMapping("/{isbn}")
    public String bookDetail(@PathVariable String isbn, Model model,
                             @AuthenticationPrincipal CustomMemberDetails memberDetails) throws Exception {

        // 뷰에 isbn으로 검색한 책 전달
        BookDTO book = bookService.searchBookByIsbn(isbn);
        model.addAttribute("book", book);

        // 로그인 한 사용자의 이메일 가져오기
        String loginMemberEmail = memberDetails != null ? memberDetails.getUsername() : null;
        model.addAttribute("LoginMemberEmail", loginMemberEmail);

        // 로그인 한 사용자가 작성한 리뷰 존재 유무 확인
        boolean alreadyWrite =
                loginMemberEmail != null && reviewService.existsByBookIsbnAndMemberEmail(isbn, loginMemberEmail);
        model.addAttribute("alreadyWrite", alreadyWrite);

        return "book/detail";
    }
}

