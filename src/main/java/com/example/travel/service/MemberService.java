package com.example.travel.service;

import com.example.travel.dto.*;
import com.example.travel.entity.*;
import com.example.travel.ext.JwtProperties;
import com.example.travel.ext.JwtTokenProvider;
import com.example.travel.repository.FavorRepository;
import com.example.travel.repository.MemberRepository;
import com.example.travel.repository.ReviewRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    private final JwtProperties jwtProperties;
    private final StringRedisTemplate redisTemplate;


    //회원가입(비밀번호 암호화)
    public String signUp(MemberSignUpRequest req) {
        if(mr.existsById(req.getId())){
            return req.getId()+"는 이미 존재하는 아이디 입니다.";
        } if(mr.findByNickname(req.getNickname())!= null){
            return req.getNickname()+"는 이미 존재하는 닉네임 입니다.";
        }
        String encodingPassword = encodingPassword(req.getPassword());
        req.setPassword(encodingPassword);
        if (req.getRole() == null || req.getRole().equals("")) req.setRole("ROLE_USER");
        Member member = req.toEntity();
        mr.save(member);
        return "회원가입이 완료되었습니다.";
    }

    //회원 로그인(토큰 발급해줌)
    @Transactional
    public MemberLoginResponse login(MemberLoginRequest req) {
        if(req.getId()==null||req.getPassword()==null){
            return new MemberLoginResponse("아이디와 비밀번호를 입력하세요");}
        Member member = req.toEntity();
        Member result = mr.findById(member.getId()).orElseThrow(IllegalArgumentException::new);
        try {
            if (result == null || !passwordMustBeSame(req.getPassword(), result.getPassword())){
                throw new IllegalArgumentException();}
            String token = jwtTokenProvider.makeJwtToken(result);
            MemberLoginResponse memberLoginResponse = new MemberLoginResponse(result, token);
            redisTemplate.opsForValue().set("RT:" + member.getId(), token);
            return memberLoginResponse;
        } catch (Exception e){
            e.printStackTrace();
            return new MemberLoginResponse("아이디 또는 비밀번호가 일치하지않습니다.");
        }
    }

    @Transactional
    public String logout(HttpServletRequest request){
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring(jwtProperties.getTokenPrefix().length());
        Claims claims = jwtTokenProvider.parsingToken(token);
        String id= (String) claims.get("id");
        System.out.println(id);
        if (redisTemplate.opsForValue().get("RT:" + id)!= null) {
                redisTemplate.delete("RT:" + id);}
        redisTemplate.opsForValue().set(token, "logout");
        return "로그아웃 되었습니다.";
    }

    private boolean passwordMustBeSame(String requestPassword, String password) {
        if (!passwordEncoder.matches(requestPassword, password)) {
            throw new IllegalArgumentException();
        }
        return true;
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
        if (isSuccess) {
            member.updatePwd(encodingPassword(newPwd));
            return mr.save(member);}
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
