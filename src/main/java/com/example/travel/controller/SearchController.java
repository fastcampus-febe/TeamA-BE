package com.example.travel.controller;

import com.example.travel.dto.PlaceResponseDto;
import com.example.travel.service.PlaceService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"관광지리스트 서비스"}, description = "관광지 정보를 보여줍니다.")
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final PlaceService placeService;
    /**
     *
     * title 기준으로 관광지 정보 검색.
     *
     */
    @GetMapping("/search/results")
    public List<PlaceResponseDto> selectPlaceWithTitle(@RequestParam @Nullable String title, Pageable pageable ){
        return placeService.findByTitleContaining(title, pageable);
    }
}
