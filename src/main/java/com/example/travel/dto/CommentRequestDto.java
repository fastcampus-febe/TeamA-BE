package com.example.travel.dto;

import com.example.travel.entity.Board;
import com.example.travel.entity.Comment;
import com.example.travel.entity.Member;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequestDto {

    private String content;
    private Member member;
    private Board board;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .member(member)
                .board(board)
                .build();
    }
}
