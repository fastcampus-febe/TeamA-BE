package com.example.travel.controller;

import com.example.travel.dto.*;
import com.example.travel.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"리뷰 서비스"}, description = "관광지 상세게시글의 리뷰를 담당합니다.")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 작성
     */
    @ApiOperation(value = "리뷰 작성", notes = "리뷰를 작성 합니다.")
    @PostMapping("/place/{placeId}/review")
    public Long save(@PathVariable Long placeId, @RequestBody final ReviewRequestDto dto, Authentication authentication) {
        MemberLoginRequest memberLoginRequest = (MemberLoginRequest) authentication.getPrincipal();
        String strMemberId = memberLoginRequest.getId();
        return reviewService.save(placeId, dto, strMemberId);
    }

    /**
     * 리뷰 조회 (관광지 상세게시글 별)
     */
    @GetMapping("/place/{placeId}/review")
    @ApiOperation(value = "리뷰 목록 조회", notes = "리뷰를 조회 합니다.")
    public List<ReviewResponseDto> findAll(@PathVariable Long placeId) {
        return reviewService.findAll(placeId);
    }

    /**
     * 리뷰 수정
     */
    @PatchMapping("/place/{placeId}/review/{reviewId}")
    @ApiOperation(value = "리뷰 수정", notes = "리뷰를 수정 합니다.")
    public Long save(@PathVariable final Long reviewId, @RequestBody final ReviewRequestDto dto) {
        return reviewService.update(reviewId, dto);
    }


    /**
     * 리뷰 삭제
     */
    @ApiOperation(value = "리뷰 삭제", notes = "리뷰를 삭제 합니다.")
    @DeleteMapping("/place/{placeId}/review/{reviewId}")
    public void deleteReviewById(@PathVariable final Long reviewId) {
        reviewService.deleteById(reviewId);
    }


    /**
     * 리뷰 가장 많은 관광지 10 곳을 보여준다.
     * 리뷰 테이블이 없는 경우에는 place 테이블의 id 기준 오름차순으로 관광지 10 곳을 보여준다.
     */
    @ApiOperation(value = "리뷰 랭킹", notes = "관광지의 리뷰가 많은 순으로 10곳의 관광지 리스트를 보여줍니다.")
    @GetMapping("/review/rank")
    public List<PlaceReviewResponseDto> selectBoardRank(){
        return reviewService.findReviewRank();
    }
}
