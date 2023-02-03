package com.example.travel.repository;

import com.example.travel.entity.Favor;
import com.example.travel.entity.Member;
import com.example.travel.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface FavorRepository extends JpaRepository<Favor, Long> {

//    Favor findByPlaceAndMember(Long place, String member);
    Favor findAllByPlaceAndMember(Place place, Member member);
    @Query(value = "select count(favor.status) " +
            "from Favor favor " +
            "where favor.status = 1")
    int sumFavorStatus();
    Favor findByMember(Member member);

}
