package com.example.travel.repository;

import com.example.travel.entity.Favor;
import com.example.travel.entity.Member;
import com.example.travel.entity.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FavorRepository extends JpaRepository<Favor, Long> {
    Favor findAllByPlaceAndMember(Place place, Member member);
    @Query(value = "select count(favor.status) " +
            "from Favor favor " +
            "where favor.status = 1" +
            "and favor.place = :place")
    int sumFavorStatus(@Param("place") Place place);
    Favor findByMember(Member member);
    List<Favor> findAllByMember(Member member, Pageable pageable);
    @Query(value = "select f.place_id, count(*) as CNT " +
            "from favor f " +
            "where f.status = 1 " +
            "group by f.place_id " +
            "order by CNT desc LIMIT 10;", nativeQuery = true)
    List<Object[]> findFavorRank();
}
