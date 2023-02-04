package com.example.travel.service;

import com.example.travel.dto.PlaceResponseDto;
import com.example.travel.dto.WeatherShortResponseDto;
import com.example.travel.entity.Favor;
import com.example.travel.entity.Member;
import com.example.travel.entity.Place;
import com.example.travel.exception.CustomException;
import com.example.travel.exception.ErrorCode;
import com.example.travel.repository.FavorRepository;
import com.example.travel.repository.MemberRepository;
import com.example.travel.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PlaceService {

    private final PlaceRepository pr;
    private final FavorRepository fr;
    private final MemberRepository mr;
    private final WeatherService ws;

    /**
     * 관광지 저장.
     */
    @Transactional
    public void save(Place place) {
        pr.save(place);
    }

    /**
     * 관광지 저장이 되어있는지 db에 저장된 contentId 와 비교하여 확인.
     */
    public boolean findByContentId(String contentId) {
        if (pr.findByContentId(Integer.parseInt(contentId)) != null) {
            return true;
        }
        return false;
    }

    /**
     * @param title 관광지명 기준으로 검색.
     */

    public List<PlaceResponseDto> findByTitleContaining(String title, Pageable pageable) {
        Page<Place> res = pr.findByTitleContaining(title, pageable);
        List<Place> placeList = res.getContent();
        HashMap mapFavor = new HashMap<>();
        List<PlaceResponseDto> placeResponseDtoList = new ArrayList<>();
        for (int i = 0; i < placeList.size(); i++) {
            if (fr.sumFavorStatus(placeList.get(i)) != 0) {
                mapFavor.put(i, fr.sumFavorStatus(placeList.get(i)));
            } else {
                mapFavor.put(i, 0);
            }
            placeResponseDtoList.add(i, (new PlaceResponseDto(placeList.get(i), (Integer) mapFavor.get(i))));
        }
        return placeResponseDtoList;
    }

    /**
     * id 기준으로 Place 테이블의 상세 정보 보여주기.
     * favor 이 1 이면 로그인한 유저가 해당 게시물을 찜하기 누른 상태, 0이면 찜하지 않은 상태.
     */
    public PlaceResponseDto findById(Long id, Member member) throws IOException, ParseException {
        Place res = pr.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        String mapX = String.valueOf(res.getMapX());
        String mapY = String.valueOf(res.getMapY());
        List<WeatherShortResponseDto> shortResponse = ws.selectShortWeather(mapX, mapY);
        if (member != null) {
            if (fr.findAllByPlaceAndMember(res, member) != null) {
                return new PlaceResponseDto(res, fr.findAllByPlaceAndMember(res, member).getStatus(), shortResponse);
            }
        }
        return new PlaceResponseDto(res, shortResponse);
    }

    /**
     * 찜하기 or 찜하기 취소
     */
    public String save(Long id, String memberId) {
        if (memberId != null) {
            Member member = mr.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
            Place place = pr.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
            if (fr.findAllByPlaceAndMember(place, member) == null) {
                updateFavorStatus("new", null, 1, place, member);
                return "찜하기 성공";
            } else {
                if (fr.findAllByPlaceAndMember(place, member).getStatus() == 1) {
                    updateFavorStatus(null, fr.findAllByPlaceAndMember(place, member), 0, place, null);
                    return "찜하기 취소";
                } else {
                    updateFavorStatus(null, fr.findAllByPlaceAndMember(place, member), 1, place, null);
                    return "찜하기 성공";
                }
            }
        }
        return "no user";
    }

    public void updateFavorStatus(String chk, Favor res, int status, Place place, Member member) {
        Favor favor;
        if (chk == null) {
            favor = new Favor(res.getId(), res.getMember(), place, status);
        } else {
            favor = new Favor(null, member, place, status);
        }
        fr.save(favor);
    }

    /**
     * 찜이 가장 많은 관광지 10 곳을 보여준다.
     * 찜 테이블이 없는 경우에는 place 테이블의 id 기준 오름차순으로 관광지 10 곳 보여준다.
     */
    public List<PlaceResponseDto> findFavorRank() {
        List<Place> placeList = new ArrayList<>();
        List<PlaceResponseDto> placeResponseDtoList = new ArrayList<>();
        List<Object[]> objects = fr.findFavorRank();
        if (objects.size() == 0 || objects == null) {
            List<Place> notFavorPlaceList = pr.findTop10OrderByIdAsc();
            return notFavorPlaceList.stream().map(PlaceResponseDto::new).collect(Collectors.toList());
        }
        int cnt = 0;
        for (Object[] obj : objects) {
            placeList.add(pr.findAllById(((BigInteger) obj[0]).longValue()));
            placeResponseDtoList.add(cnt, new PlaceResponseDto(placeList.get(cnt), ((BigInteger) obj[1]).intValue()));
            cnt++;
        }
        return placeResponseDtoList;
    }
}