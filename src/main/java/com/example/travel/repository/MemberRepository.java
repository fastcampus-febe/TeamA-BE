package com.example.travel.repository;

import com.example.travel.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    /* Security */
//    Optional<Member> findByUserId(String id);
//
//    /* member GET */
    Member findByNickname(String nickname);
//
//    /*
//    * 중복인 경우 true, 중복되지 않은경우 false 리턴
//    * */
//    boolean existsByUserId(String id);
//    boolean existsByNickname(String nickname);
}
