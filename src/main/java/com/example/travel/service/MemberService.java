package com.example.travel.service;

import com.example.travel.dto.*;
import com.example.travel.entity.Favor;
import com.example.travel.entity.Member;
import com.example.travel.entity.Place;
import com.example.travel.entity.Review;
import com.example.travel.ext.JwtTokenProvider;
import com.example.travel.repository.FavorRepository;
import com.example.travel.repository.MemberRepository;
import com.example.travel.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {
    private final MemberRepository mr;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final FavorRepository favorRepository;
    private final ReviewRepository reviewRepository;

    //회원가입(비밀번호 암호화)
    public String signUp(MemberSignUpRequest req) {

        log.info("req :" + req);
        String encodingPassword = encodingPassword(req.getPassword());
        req.setPassword(encodingPassword);
        if (req.getRole() == null || req.getRole().equals("")) req.setRole("ROLE_USER");

        try {
            Member member = req.toEntity();
            mr.save(member);
        } catch (Exception e) {
            log.info(e.getMessage());
            return "failed";
        }
        return "success";
    }

    //회원 로그인(토큰 발급해줌)
    public TokenDto login(MemberLoginRequest req) {
        log.info("req : " + req);
        Member member = req.toEntity();
        Member result = mr.findById(member.getId()).orElseThrow(IllegalArgumentException::new);
        log.info("member : " + member);

        if (result != null) {
            passwordMustBeSame(req.getPassword(), result.getPassword());
            String token = jwtTokenProvider.makeJwtToken(result);

            return new TokenDto(token);
        } else {
            return null;
        }
    }


    private void passwordMustBeSame(String requestPassword, String password) {
        if (!passwordEncoder.matches(requestPassword, password)) {
            throw new IllegalArgumentException();
        }
    }

    private String encodingPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Transactional(readOnly = true)
    public Member findById(final String memberId) {
        return mr.findById(memberId).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public Member updatePwd(String memberId, String currentPwd, String newPwd) {
        Member member = mr.findById(memberId).orElseThrow(IllegalArgumentException::new);
        boolean isSuccess = passwordEncoder.matches(currentPwd, member.getPassword());
        System.out.println(isSuccess);
        if (isSuccess) {
            member.updatePwd(encodingPassword(newPwd));
            return mr.save(member);
        }
        return null;
    }

    @Transactional
    public void deleteMember(String id) {
        mr.deleteById(id);
    }

    @Transactional
    public List<FavorResponseDto> getFavors(String memberId, Pageable pageable) {
        Member member = mr.findById(memberId).orElseThrow(IllegalArgumentException::new);
        List<Favor> list = favorRepository.findAllByMember(member, pageable);
        return list.stream().map(FavorResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public List<ReviewResponseDto> getReviews(String memberId, Pageable pageable){
        Member member = mr.findById(memberId).orElseThrow(IllegalArgumentException::new);
        List<Review> list = reviewRepository.findAllByWriter(member.getNickname(), pageable);
        return list.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }
}
