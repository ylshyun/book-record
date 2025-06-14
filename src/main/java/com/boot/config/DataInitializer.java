package com.boot.config;

import com.boot.dto.BookDTO;
import com.boot.entity.Book;
import com.boot.entity.Member;
import com.boot.entity.Review;
import com.boot.repository.BookRepository;
import com.boot.repository.MemberRepository;
import com.boot.repository.ReviewRepository;
import com.boot.service.BookService;
import com.boot.service.NaverApi;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev") // 로컬 환경에서만 사용하도록 설정
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final BookService bookService;
    private final NaverApi naverApi;
    private final PasswordEncoder passwordEncoder;
    private final BookRepository bookRepository;

    public void run(ApplicationArguments args) throws Exception {
        //  인덱스 페이지네이션 테스트용 데이터
        List<String> keywords = List.of("자바", "알고리즘", "스프링부트", "jpa", "데이터베이스", "java", "database", "mysql");

        for (String keyword : keywords) {
            List<BookDTO> apiBooks = naverApi.searchBooksByKeyword("%" + keyword + "%");
            bookService.saveBooksIfNotExist(apiBooks);
        }

        if (memberRepository.count() < 50) {
            for (int i = 0; i < 50; i++) {
                memberRepository.save(Member.builder()
                        .memberName("member" + i)
                        .memberEmail("memberEmail" + i + "@gmail.com")
                        .memberPassword(passwordEncoder.encode("memberPassword" + i))
                        .role("ROLE_MEMBER")
                        .build());
            }
        }
        if (reviewRepository.count() == 0) {
            List<Book> books = bookRepository.findAll();
            List<Member> members = memberRepository.findAll();

            for (int i = 0; i < 100; i++) {
                Book book = books.get(i % books.size());    // 모든 리뷰에 순차적으로 책 연결
                Member member = members.get(i % members.size());    // 모든 리뷰에 순차적으로 회원 연결

                reviewRepository.save(Review.builder()
                        .member(memberRepository.findByMemberEmail("memberEmail" + i + "@gmail.com"))
                        .reviewContent("이 리뷰는 테스트용으로 작성되었으며, 리뷰의 내용은 50자 이상이어야 합니다. " + i)
                        .reviewTitle("리뷰 제목 " + i)
                        .book(book)
                        .member(member)
                        .build());
            }

            //  마이페이지 페이지네이션 테스트용 데이터
            Member testMember = memberRepository.save(Member.builder()
                    .memberName("testMember")
                    .memberEmail("testMemberEmail@gmail.com")
                    .memberPassword(passwordEncoder.encode("testMemberPassword"))
                    .role("ROLE_MEMBER")
                    .build());

            System.out.println("total books = " + books.size());

            for (int i = 0; i < 70; i++) {
                Book testBook = books.get(i);
                reviewRepository.save(Review.builder()
                        .book(testBook)
                        .member(testMember)
                        .reviewContent("이 리뷰는 테스트용으로 작성되었으며, 리뷰의 내용은 50자 이상이어야 합니다. " + i)
                        .reviewTitle("리뷰 제목 " + i)
                        .build());
            }
        }
    }
}
