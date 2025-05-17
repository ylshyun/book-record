package com.boot.controller;

import com.boot.dto.ReviewBookDTO;
import com.boot.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        Page<ReviewBookDTO> reviews = reviewService.findAllReviews(PageRequest.of(page, 10));
        model.addAttribute("reviews", reviews);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviews.getTotalPages());
        return "index";
    }
}
