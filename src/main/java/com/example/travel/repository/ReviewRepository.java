package com.example.travel.repository;

import com.example.travel.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByWriter(String nickname, Pageable pageable);
    List<Review> findReviewsByPlaceId(Long id, Sort sort);
}
