package com.example.travel.dto;

import com.example.travel.entity.Board;
import com.example.travel.entity.Member;
import lombok.*;

//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardRequestDto {

    private Long id;
    private String title; // 제목
    private String content; // 내용
    private String writer; // 작성자
    private Member member;

    public Board toEntity() {
        return Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .thumb(0)
                .hit(0)
                .member(member)
                .build();
    }
}
