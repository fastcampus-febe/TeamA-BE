package com.example.travel.service;

import com.example.travel.dto.MemberLoginRequest;
import com.example.travel.dto.MemberSignUpRequest;
import com.example.travel.dto.TokenDto;
import com.example.travel.entity.Member;
import com.example.travel.ext.JwtTokenProvider;
import com.example.travel.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {
    private final MemberRepository mr;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    //회원가입(비밀번호 암호화)
    public String signUp(MemberSignUpRequest req){

        log.info("req :" + req);
        String encodingPassword = encodingPassword(req.getPassword());
        req.setPassword(encodingPassword);
        if(req.getRole() == null || req.getRole().equals("")) req.setRole("ROLE_USER");

        try{
            Member member = req.toEntity();
            mr.save(member);
        } catch(Exception e){
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


}
