package com.example.travel.service;

import com.example.travel.dto.PlaceResponseDto;
import com.example.travel.entity.Place;
import com.example.travel.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PlaceService {

    private final PlaceRepository pr;

    /**
     * 관광지 저장.
     */
    @Transactional
    public void save(Place place) {
        Place entity = pr.save(place);
    }
    /**
     * 관광지 저장이 되어있는지 db에 저장된 contentId 와 비교하여 확인.
     */
    public boolean findByContentId(String contentId){
        if(pr.findByContentId(Integer.parseInt(contentId)) != null){
            return true;
        }
        return false;
    }

    /**
     *
     * @param title 관광지명 기준으로 검색.
     */

    public List<PlaceResponseDto> findByTitleContaining(String title, Pageable pageable){
        Page<Place> res = pr.findByTitleContaining(title, pageable);
        List<Place> placeList = res.getContent();
        return placeList.stream().map(PlaceResponseDto::new).collect(Collectors.toList());
    }

}
