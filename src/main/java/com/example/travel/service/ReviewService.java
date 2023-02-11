package com.example.travel.service;

import com.example.travel.dto.PlaceResponseDto;
import com.example.travel.dto.PlaceReviewResponseDto;
import com.example.travel.dto.ReviewRequestDto;
import com.example.travel.dto.ReviewResponseDto;
import com.example.travel.entity.*;
import com.example.travel.repository.MemberRepository;
import com.example.travel.repository.PlaceRepository;
import com.example.travel.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    /**
     * 리뷰 생성
     */
    @Transactional
    public String save(Long placeId, final ReviewRequestDto dto, String memberId) {
        Place place = placeRepository.findById(placeId).orElseThrow(() ->
                new IllegalArgumentException("댓글 작성 실패 : 해당 게시글 id가 존재하지 않습니다. => " + placeId));
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("댓글 작성 실패 : 해당 Member id가 존재하지 않습니다. => " + memberId));
        if (dto.getContent()==null || dto.getContent()==""){
            return "내용을 입력해주세요";
        }
        dto.setWriter(member.getNickname());
        dto.setPlace(place);
        dto.setMember(member);

        Review review = reviewRepository.save(dto.toEntity());
        return String.valueOf(review.getId());
    }

    /**
     * 리뷰 리스트 조회
     */
    public List<ReviewResponseDto> findAll(Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(() ->
                new IllegalArgumentException("댓글 작성 실패 : 해당 게시글 id가 존재하지 않습니다. => " + placeId));
        Sort sort = Sort.by(Sort.Direction.DESC, "id", "createdDate");
        List<Review> list = reviewRepository.findReviewsByPlaceId(placeId, sort);
        return list.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public String update(final Long reviewId, final ReviewRequestDto params) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("리뷰 작성 실패 : 해당 리뷰 id가 존재하지 않습니다. => " + reviewId));
        if (params.getContent()==null || params.getContent()==""){
            return "내용을 입력해주세요";
        }
        review.update(params.getContent());
        return String.valueOf(reviewId);
    }

    /**
     * 리뷰 삭제하기
     */
    @Transactional
    public void deleteById(final Long reviewId) {
        placeRepository.deleteById(reviewId);
    }

    /**
     * 리뷰 가장 많은 관광지 10 곳을 보여준다.
     * 리뷰 테이블이 없는 경우에는 place 테이블의 id 기준 오름차순으로 관광지 10 곳을 보여준다.
     */
    public List<PlaceReviewResponseDto> findReviewRank(){
        List<Place> placeList = new ArrayList<>();
        List<PlaceReviewResponseDto> placeReviewResponseDtos = new ArrayList<>();
        List<Object[]> objects = reviewRepository.findReviewRank();
        if (objects.size() == 0) {
            List<Place> notFavorPlaceList = placeRepository.findTop10OrderByIdAsc();
            return notFavorPlaceList.stream().map(PlaceReviewResponseDto::new).collect(Collectors.toList());
        }
        int cnt = 0;
        for (Object[] obj : objects) {
            placeList.add(placeRepository.findAllById(((BigInteger) obj[0]).longValue()));
            placeReviewResponseDtos.add(cnt, new PlaceReviewResponseDto(placeList.get(cnt), ((BigInteger) obj[1]).intValue()));
            cnt++;
        }
        return placeReviewResponseDtos;
    }

    static List<PlaceResponseDto> getPlaceResponseDtos(List<Place> placeList, List<PlaceResponseDto> placeResponseDtoList, List<Object[]> objects, PlaceRepository placeRepository) {
        int cnt = 0;
        for (Object[] obj : objects) {
            placeList.add(placeRepository.findAllById(((BigInteger) obj[0]).longValue()));
            placeResponseDtoList.add(cnt, new PlaceResponseDto(placeList.get(cnt), ((BigInteger) obj[1]).intValue()));
            cnt++;
        }
        return placeResponseDtoList;
    }
}
