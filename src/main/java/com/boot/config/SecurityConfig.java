package com.boot.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 정적 자원에 대한 인증 무시 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    // 시큐리티 필터 체인 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/","/login","/signup", "/books/**").permitAll()
                        .requestMatchers("/api/member/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
						.loginPage("/login") // 커스텀 로그인 페이지
                        .loginProcessingUrl("/loginProc") // 로그인 처리 URL
						.defaultSuccessUrl("/",true) //로그인 성공 시 리다이렉트
                        .successHandler(
                                (request, response, authentication) -> {
                                    System.out.println("authentication : " + authentication.getName());
                                })
                        .failureHandler(
                                (request, response, exception) -> {
                                    System.out.println("exception : " + exception.getMessage());
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
                                    response.setContentType("application/json;charset=UTF-8");  // JSON 응답을 반환하도록 변경
                                    response.getWriter().write("{\"message\": \"비밀번호가 일치하지 않습니다.\"}");
                                }
                        )
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 처리 URL
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 리다이렉트
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .addLogoutHandler((request, response, authentication) -> {
                            System.out.println("User Logged Out: " +
                                    (authentication != null ? authentication.getName() : "Unknown"));
                        })
                        .permitAll()
                );
        return http.build();
    }

    // 비밀번호 암호화 및 인증 처리
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
