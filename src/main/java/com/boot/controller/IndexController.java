package com.boot.controller;

import com.boot.dto.ReviewBookDTO;
import com.boot.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ReviewService reviewService;

    @GetMapping("/")
    public String index(Model model, @RequestParam(defaultValue = "0") int page) {

        Page<ReviewBookDTO> reviews = reviewService.findAllReviews(PageRequest.of(page, 10, Sort.by("modifiedDate").descending()));
        int totalPages = reviews.getTotalPages();   // 전체 페이지 수
        int currentPage = reviews.getNumber();  // 현재 페이지 번호

        int displayPages = 5;
        int startPage;
        int endPage;

        if (totalPages <= displayPages) {
            startPage = 0;
            endPage = totalPages - 1;
        } else if (currentPage <= 2) {
            startPage = 0;
            endPage = displayPages - 1;
        } else if (currentPage >= totalPages - 3){
            startPage = totalPages - displayPages;
            endPage = totalPages - 1;
        } else{
            startPage = currentPage -2;
            endPage = currentPage + 2;
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "index";
    }
}
