package com.example.travel.dto;

import com.example.travel.entity.Favor;
import com.example.travel.entity.Place;
import lombok.*;

@Getter
public class FavorResponseDto {

    private Long placeId;
    private String placeImg;
    private String title;
    private String addr1;

    @Builder
    public FavorResponseDto(Favor favor){
        Place place = favor.getPlace();
        this.placeId = place.getId();
        this.placeImg = place.getFirstImage();
        this.title = place.getTitle();
        this.addr1 = place.getAddr1();
    }
}
