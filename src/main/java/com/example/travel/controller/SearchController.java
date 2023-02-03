package com.example.travel.controller;

import com.example.travel.dto.MemberLoginRequest;
import com.example.travel.dto.PlaceResponseDto;
import com.example.travel.entity.Member;
import com.example.travel.service.PlaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"관광지 리스트 서비스"}, description = "관광지 정보를 보여줍니다.")
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final PlaceService placeService;
    /**
     * title 기준으로 관광지 정보 검색.
     */
    @ApiOperation(value = "관광지 검색", notes = "title 기준으로 관광지 정보를 검색합니다.")
    @GetMapping("/search/results")
    public List<PlaceResponseDto> selectPlaceWithTitle(@RequestParam @Nullable String title, Pageable pageable ){
        return placeService.findByTitleContaining(title, pageable);
    }

    /**
     * 관광지의 상세정보 출력
     */
    @ApiOperation(value = "관광지 상세정보", notes = "id 기준으로 해당 관광지의 상세 내용을 보여줍니다")
    @GetMapping("/place/{id}")
    public PlaceResponseDto detailPlaceWithId(@PathVariable Long id, Authentication authentication){
        MemberLoginRequest memberLoginRequest = (MemberLoginRequest) authentication.getPrincipal();
        Member member = Member.builder()
                .id(memberLoginRequest.getId())
                .build();
        return placeService.findById(id, member);
    }

    /**
     * 찜하기 or 찜하기 취소
     */
    @ApiOperation(value = "찜 기능", notes = "유저가 관광지를 찜 하거나 찜 취소를 할 수 있습니다.")
    @PostMapping("/favor/{id}")
    public String doFavor(@PathVariable Long id, Authentication authentication){
        MemberLoginRequest memberLoginRequest = (MemberLoginRequest) authentication.getPrincipal();
        String strMemberId = memberLoginRequest.getId();
        return placeService.save(id, strMemberId);
    }
}
