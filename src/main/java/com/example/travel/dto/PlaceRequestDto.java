package com.example.travel.dto;

import com.example.travel.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceRequestDto {

    private long id;    // 인덱스
    private int contentId; // 컨텐츠 ID
    private String title; // 컨텐츠명
    private String addr1; // 주소
    private String addr2; // 상세주소
    private double mapX; //GPS X좌표
    private double mapY; //GPS Y좌표

    public Place toEntity(){
        return Place.builder()
                .id(id)
                .contentId(contentId)
                .title(title)
                .addr1(addr1)
                .addr2(addr2)
                .mapX(mapX)
                .mapY(mapY)
                .build();
    }
}
