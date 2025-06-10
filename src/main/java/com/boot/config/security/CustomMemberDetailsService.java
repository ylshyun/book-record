package com.boot.config.security;

import com.boot.entity.Member;
import com.boot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberEmail(memberEmail);
        if (member == null) {
            throw new UsernameNotFoundException("일치하는 이메일이 없습니댜:" + memberEmail);
        }

        // 일치하는 회원이 있다면 db에 저장되어 있는 회원의 role add
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(member.getRole()));

        // UserDetails 객체로 생성 후 리턴
        return CustomMemberDetails.builder()
                .username(member.getMemberEmail())
                .name(member.getMemberName())
                .password(member.getMemberPassword())
                .authorities(grantedAuthorities)
                .build();
    }
}
