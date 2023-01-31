package com.example.travel.dto;

import com.example.travel.entity.Board;
import com.example.travel.entity.Comment;
import com.example.travel.entity.Member;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {

    private Long id;
    private String content; // 댓글 내용
    private String writer; // 작성자
    private Member member;
    private Board board;

    public Comment toEntity() {
        return Comment.builder()
                .id(id)
                .content(content)
                .writer(writer)
                .member(member)
                .board(board)
                .build();
    }
//    public Comment toEntity() {
//        return Comment.builder()
//                .member(member)
//                .board(board)
//                .build();
//    }
}
