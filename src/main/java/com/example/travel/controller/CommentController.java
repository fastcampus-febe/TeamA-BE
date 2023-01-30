package com.example.travel.controller;

import com.example.travel.dto.CommentRequestDto;
import com.example.travel.dto.CommentResponseDto;
import com.example.travel.dto.MemberLoginRequest;
import com.example.travel.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"댓글 서비스"}, description = "게시글의 댓글을 담당합니다.")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     */
    @ApiOperation(value = "댓글 작성", notes = "댓글을 작성 합니다.")
    @PostMapping("/board/{boardId}/comment")
    public Long save(@PathVariable Long boardId, @RequestBody final CommentRequestDto dto, Authentication authentication) {
        MemberLoginRequest memberLoginRequest = (MemberLoginRequest) authentication.getPrincipal();
        String strMemberId = memberLoginRequest.getId();
        return commentService.save(boardId, dto, strMemberId);
    }

    /**
     * 댓글 조회 (게시글 별)
     */
    @GetMapping("/board/{boardId}/comment")
    @ApiOperation(value = "댓글 목록 조회", notes = "댓글을 조회 합니다.")
    public List<CommentResponseDto> findAll(@PathVariable Long boardId) {
        return commentService.findAll(boardId);
    }

    /**
     * 댓글 수정
     */
    @PatchMapping("/board/{boardId}/comment/{commentId}")
    @ApiOperation(value = "댓글 수정", notes = "댓글을 수정 합니다.")
    public Long save(@PathVariable final Long commentId, @RequestBody final CommentRequestDto params) {
        return commentService.update(commentId, params);
    }


    /**
     * 댓글 삭제
     */
    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제 합니다.")
    @DeleteMapping("/board/{boardId}/comment/{commentId}")
    public void deleteCommentById(@PathVariable final Long commentId) {
        commentService.deleteById(commentId);
    }
}

