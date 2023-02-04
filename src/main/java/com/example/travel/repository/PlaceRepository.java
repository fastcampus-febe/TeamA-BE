package com.example.travel.repository;

import com.example.travel.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findByContentId(int contentId);
    Page<Place> findByTitleContaining(String title, Pageable pageable);
    Place findAllById(Long id);
    @Query(value = "select * from place p " +
            "order by p.id Asc LIMIT 10;", nativeQuery = true)
    List<Place> findTop10OrderByIdAsc();

}

