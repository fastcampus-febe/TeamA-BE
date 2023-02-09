package com.example.travel.dto;

import com.example.travel.entity.Member;
import com.example.travel.entity.Place;
import com.example.travel.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequestDto {

    private Long id;
    private String content; // 리뷰 내용
    private String writer; // 작성자
    private Member member;
    private Place place;

    public Review toEntity() {
        return Review.builder()
                .id(id)
                .content(content)
                .writer(writer)
                .member(member)
                .place(place)
                .build();
    }
}
