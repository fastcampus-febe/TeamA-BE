package com.example.travel.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "place")
public class Place {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content_id", nullable = false, unique = true)
    private int contentId; // 컨텐츠 ID

    @Column(name = "title")
    private String title; // 컨텐츠명

    @Column(name = "addr1")
    private String addr1; // 주소

    @Column(name = "addr2")
    private String addr2; // 상세주소

    @Column(name = "area_code")
    private int areaCode; // 지역코드

    @Column(name = "sigungu_code")
    private String sigunguCode; // 시군구코드

    @Column(name = "cat1")
    private String cat1; // 대분류

    @Column(name = "cat2")
    private String cat2; // 중분류

    @Column(name = "cat3")
    private String cat3; // 소분류

    @Column(name = "first_image")
    private String firstImage; // 대표이미지(원본)

    @Column(name = "first_image2")
    private String firstImage2; // 대표이미지(썸네일)

    @Column(name = "mapX")
    private double mapX; // GPS X좌표

    @Column(name = "mapY")
    private double mapY; // GPS Y좌표

    @Column(name = "tel")
    private String tel; // 전화번호

    @Column(name = "homepage", length = 1000)
    private String homepage; // 홈페이지 주소

    @Column(name = "overview", length = 10000)
    private String overview; // 개요

    @OneToMany(mappedBy = "place", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id desc") // 리뷰 정렬
    private List<Review> reviews;

    @Builder
    public Place(Long id, int contentId, String title, String addr1, String addr2, int areaCode, String sigunguCode, String cat1, String cat2, String cat3, String firstImage, String firstImage2, double mapX, double mapY, String tel, String homepage, String overview) {
        this.id = id;
        this.contentId = contentId;
        this.title = title;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.areaCode = areaCode;
        this.sigunguCode = sigunguCode;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.firstImage = firstImage;
        this.firstImage2 = firstImage2;
        this.mapX = mapX;
        this.mapY = mapY;
        this.tel = tel;
        this.homepage = homepage;
        this.overview = overview;
    }

}
