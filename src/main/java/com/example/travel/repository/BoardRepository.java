package com.example.travel.repository;

import com.example.travel.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByTitleContaining(String searchKeyword, Pageable pageable);
    Page<Board> findAllByWriter(String writer, Pageable pageable);
}
