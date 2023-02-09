package com.example.travel.repository;

import com.example.travel.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByWriter(String nickname, Pageable pageable);
    List<Review> findReviewsByPlaceId(Long id, Sort sort);
    @Query(value = "select R.place_id, count(*) as CNT " +
            "from review R " +
            "group by R.place_id " +
            "order by CNT desc LIMIT 10;", nativeQuery = true)
    List<Object[]> findReviewRank();
}
