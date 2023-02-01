package com.example.travel.repository;

import com.example.travel.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
    Place findByContentId(int contentId);
}

