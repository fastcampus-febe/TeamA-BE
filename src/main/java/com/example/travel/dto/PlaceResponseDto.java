package com.example.travel.dto;

import com.example.travel.entity.Place;
import lombok.Getter;

import java.util.List;

@Getter
public class PlaceResponseDto {

    private Long id;    //인덱스
    private int contentId; // 컨텐츠 ID
    private String title; // 컨텐츠명
    private String addr1; // 주소
    private String addr2; // 상세주소
    private int areaCode; // 지역코드
    private String sigunguCode; // 시군구코드
    private String cat1; // 대분류
    private String cat2; // 중분류
    private String cat3; // 소분류
    private String firstImage; // 대표이미지(원본)
    private String firstImage2; // 대표이미지(썸네일)
    private double mapX; // GPS X좌표
    private double mapY; // GPS Y좌표
    private String tel; // 전화번호
    private String homepage; // 홈페이지 주소
    private String overview; // 개요
    private int favor; // 찜 cnt
    private List<WeatherShortResponseDto> weather; //날씨

    public PlaceResponseDto(Place place) {
        this.id = place.getId();
        this.contentId = place.getContentId();
        this.title = place.getTitle();
        this.addr1 = place.getAddr1();
        this.addr2 = place.getAddr2();
        this.areaCode = place.getAreaCode();
        this.sigunguCode = place.getSigunguCode();
        this.cat1 = place.getCat1();
        this.cat2 = place.getCat2();
        this.cat3 = place.getCat3();
        this.firstImage = place.getFirstImage();
        this.firstImage2 = place.getFirstImage2();
        this.mapX = place.getMapX();
        this.mapY = place.getMapY();
        this.tel = place.getTel();
        this.homepage = place.getHomepage();
        this.overview = place.getOverview();
    }

    public PlaceResponseDto(Place place, List<WeatherShortResponseDto> weather) {
        this.id = place.getId();
        this.contentId = place.getContentId();
        this.title = place.getTitle();
        this.addr1 = place.getAddr1();
        this.addr2 = place.getAddr2();
        this.areaCode = place.getAreaCode();
        this.sigunguCode = place.getSigunguCode();
        this.cat1 = place.getCat1();
        this.cat2 = place.getCat2();
        this.cat3 = place.getCat3();
        this.firstImage = place.getFirstImage();
        this.firstImage2 = place.getFirstImage2();
        this.mapX = place.getMapX();
        this.mapY = place.getMapY();
        this.tel = place.getTel();
        this.homepage = place.getHomepage();
        this.overview = place.getOverview();
        this.weather = weather;
    }

    public PlaceResponseDto(Place place, int sumFavorStatus) {
        this.id = place.getId();
        this.contentId = place.getContentId();
        this.title = place.getTitle();
        this.addr1 = place.getAddr1();
        this.addr2 = place.getAddr2();
        this.areaCode = place.getAreaCode();
        this.sigunguCode = place.getSigunguCode();
        this.cat1 = place.getCat1();
        this.cat2 = place.getCat2();
        this.cat3 = place.getCat3();
        this.firstImage = place.getFirstImage();
        this.firstImage2 = place.getFirstImage2();
        this.mapX = place.getMapX();
        this.mapY = place.getMapY();
        this.tel = place.getTel();
        this.homepage = place.getHomepage();
        this.overview = place.getOverview();
        this.favor = sumFavorStatus;
    }

    public PlaceResponseDto(Place place, int sumFavorStatus, List<WeatherShortResponseDto> weather) {
        this.id = place.getId();
        this.contentId = place.getContentId();
        this.title = place.getTitle();
        this.addr1 = place.getAddr1();
        this.addr2 = place.getAddr2();
        this.areaCode = place.getAreaCode();
        this.sigunguCode = place.getSigunguCode();
        this.cat1 = place.getCat1();
        this.cat2 = place.getCat2();
        this.cat3 = place.getCat3();
        this.firstImage = place.getFirstImage();
        this.firstImage2 = place.getFirstImage2();
        this.mapX = place.getMapX();
        this.mapY = place.getMapY();
        this.tel = place.getTel();
        this.homepage = place.getHomepage();
        this.overview = place.getOverview();
        this.weather = weather;
    }
}
