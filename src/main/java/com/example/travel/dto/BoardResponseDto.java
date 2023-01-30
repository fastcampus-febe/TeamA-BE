package com.example.travel.dto;

import com.example.travel.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardResponseDto {

    private Long id; // PK
    private String title; // 제목
    private String content; // 내용
    private String writer; // 작성자
    private int thumb; // 좋아요
    private int hit; // 조회 수
    private LocalDateTime createdDate; // 생성일
    private LocalDateTime modifiedDate; // 수정일
//    private String member_id; // 작성자 id
//    private List<CommentResponseDto> comments;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getMember().getNickname();
        this.thumb = board.getThumb();
        this.hit = board.getHit();
        this.createdDate = board.getCreatedDate();
        this.modifiedDate = board.getModifiedDate();
//        this.member_id = board.getMember().getId();
//        this.comments = board.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }
}
