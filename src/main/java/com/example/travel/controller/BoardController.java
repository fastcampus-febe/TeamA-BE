package com.example.travel.controller;

import com.example.travel.dto.BoardRequestDto;
import com.example.travel.dto.BoardResponseDto;
import com.example.travel.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"게시판 서비스"}, description = "게시글 서비스를 담당합니다.")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 생성
     */
    @PostMapping("/board/insert")
    public Long save(@RequestBody final BoardRequestDto params) {
        return boardService.save(params);
    }

    /**
     * 게시글 리스트 조회
     */
    @GetMapping("/board/selectAll")
    public List<BoardResponseDto> findAll() {
        return boardService.findAll();
    }

    /**
     * 게시글 수정
     */
    @PatchMapping("/board/{id}")
    public Long save(@PathVariable final Long id, @RequestBody final BoardRequestDto params) {
        return boardService.update(id, params);
    }

    /**
     * 상세 게시글 내용 가져오기
     */
    @GetMapping("/board/{id}")
    @ApiOperation(value = "게시글 조회", notes = "상세 게시글 내용을 가져옵니다.")
    public BoardResponseDto findByBoardId(@PathVariable final Long id){
        return boardService.findByBoardId(id);
    }

    /**
     * 게시글 삭제
     */
    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다.")
    @DeleteMapping("/board/{id}")
    public void deleteBoardById(@PathVariable final Long id){
        boardService.deleteById(id);
    }
}
