package com.example.travel.repository;

import com.example.travel.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
public interface PlaceRepository extends JpaRepository<Place, Integer> {
    Place findByContentId(int contentId);
    Page<Place> findByTitleContaining(String title, Pageable pageable);
}

