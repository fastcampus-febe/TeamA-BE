package com.example.travel.dto;

import com.example.travel.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {

    private Long id; // PK
    private String content; // 리뷰 내용
    private String writer; // 작성자
    private Long place_id; // 관광지 상세게시물 id
    private LocalDateTime createdDate; // 생성일
    private LocalDateTime modifiedDate; // 수정일

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        this.writer = review.getMember().getNickname();
        this.createdDate = review.getCreatedDate();
        this.modifiedDate = review.getModifiedDate();
        this.place_id = review.getPlace().getId();
    }
}
