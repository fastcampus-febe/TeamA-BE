package com.example.travel.service;

import com.example.travel.entity.Place;
import com.example.travel.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
