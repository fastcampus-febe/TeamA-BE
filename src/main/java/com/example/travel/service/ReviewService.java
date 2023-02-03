package com.example.travel.service;

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
    public Long save(Long placeId, final ReviewRequestDto dto, String memberId) {
        Place place = placeRepository.findById(placeId).orElseThrow(() ->
                new IllegalArgumentException("댓글 작성 실패 : 해당 게시글 id가 존재하지 않습니다. => " + placeId));
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("댓글 작성 실패 : 해당 Member id가 존재하지 않습니다. => " + memberId));
        dto.setWriter(member.getNickname());
        dto.setPlace(place);
        dto.setMember(member);

        Review review = reviewRepository.save(dto.toEntity());
        return review.getId();
    }

    /**
     * 리뷰 리스트 조회
     */
    public List<ReviewResponseDto> findAll(Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(() ->
                new IllegalArgumentException("댓글 작성 실패 : 해당 게시글 id가 존재하지 않습니다. => " + placeId));
        Sort sort = Sort.by(Sort.Direction.DESC, "id", "createdDate");
        List<Review> list = reviewRepository.findAll(sort);
        return list.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public Long update(final Long reviewId, final ReviewRequestDto params) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("리뷰 작성 실패 : 해당 리뷰 id가 존재하지 않습니다. => " + reviewId));
        review.update(params.getContent());
        return reviewId;
    }

    /**
     * 리뷰 삭제하기
     */
    @Transactional
    public void deleteById(final Long reviewId) {
        placeRepository.deleteById(reviewId);
    }
}
