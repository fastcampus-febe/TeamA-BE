package com.example.travel.repository;

import com.example.travel.dto.BoardResponseDto;
import com.example.travel.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Transactional
    @Modifying
    @Query("update Board b set b.hit = b.hit + 1 where b.id = :id")
    int updateHit(@Param("id") Long id );
}
