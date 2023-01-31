package com.example.travel.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.travel.dto.BoardResponseDto;
import com.example.travel.entity.Member;
import com.example.travel.repository.MemberRepository;
import com.example.travel.service.BoardService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.travel.entity.Board;
import com.example.travel.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;

@SpringBootTest
public class BoardControllerTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService bs;

    @Test
    void save() {

        // 1. 게시글 파라미터 생성
        Board params = Board.builder()
                .title("1번 게시글 제목")
                .content("1번 게시글 내용")
                .writer("bourbon")
                .hit(0)
                .build();

        // 2. 게시글 저장
        boardRepository.save(params);

        // 3. 1번 게시글 정보 조회
        Board entity = boardRepository.findById((long) 1).get();
        assertThat(entity.getTitle()).isEqualTo("1번 게시글 제목");
        assertThat(entity.getContent()).isEqualTo("1번 게시글 내용");
        assertThat(entity.getWriter()).isEqualTo("bourbon");
    }

    @Test
    void findAll() {

        // 1. 전체 게시글 수 조회
        long boardsCount = boardRepository.count();

        // 2. 전체 게시글 리스트 조회
        List<Board> boards = boardRepository.findAll();
    }

    @Test
    void delete() {

        // 1. 게시글 조회
        Board entity = boardRepository.findById((long) 1).get();

        // 2. 게시글 삭제
        boardRepository.delete(entity);
    }

    @Test
    void findByBoardId(){
        BoardResponseDto result = new BoardResponseDto(boardRepository.findById((long)1).get());
        System.out.println(result);
    }

    @Test
    void deleteById(){
        boardRepository.deleteById((long) 1);
    }

    @Test
    void boardSearchTest() {

        String searchKeyword = "";
        String writer = "bourbon";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        List<BoardResponseDto> list = null;
        if (searchKeyword ==null && writer == null){
            Page<Board> result = boardRepository.findAll(pageable);
        } else if (searchKeyword != null && writer == null) {
            Page<Board> result = boardRepository.findAllByTitleContaining(searchKeyword,pageable);
        } else {
            Page<Board> result = boardRepository.findAllByWriter(writer, pageable);
        }
        Page<Board> result = boardRepository.findAllByWriter(writer, pageable);
        List<Board> boardList = result.getContent();
        List<BoardResponseDto> boardResponseDtoList = boardList.stream().map(BoardResponseDto::new).collect(Collectors.toList());

        for (BoardResponseDto board : boardResponseDtoList) {
            System.out.println(board.getWriter().toString());
        }
    }
//    @Test
//    void thumbsUp() {
//        bs.thumbsUp((long)1);
//    }
}
