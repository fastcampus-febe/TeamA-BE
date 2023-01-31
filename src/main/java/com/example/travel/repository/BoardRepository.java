package com.example.travel.repository;

import com.example.travel.dto.BoardResponseDto;
import com.example.travel.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByTitleContaining(String searchKeyword, Pageable pageable);
    Page<Board> findAllByWriterContaining(String writer, Pageable pageable);
}
