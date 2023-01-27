package com.example.travel.repository;

import com.example.travel.entity.Board;
import com.example.travel.entity.Member;
import com.example.travel.entity.Thumb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbRepository extends JpaRepository<Thumb, Long> {

    Thumb findByBoardAndMember(Board board, Member member);
}
