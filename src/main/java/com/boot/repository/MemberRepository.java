package com.boot.repository;

import com.boot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsMemberByMemberEmail(String memberEmail);
    Member findByMemberEmail(String memberEmail);
}
