package com.example.travel.service;

import com.example.travel.dto.MemberLoginRequest;
import com.example.travel.dto.MemberSignUpRequest;
import com.example.travel.dto.TokenDto;
import com.example.travel.entity.Member;
import com.example.travel.ext.JwtTokenProvider;
import com.example.travel.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    @Transactional(readOnly = true)
    public Member findById(final String memberId){
        return mr.findById(memberId).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public Member updatePwd(String memberId, String currentPwd, String newPwd){
        Member member = mr.findById(memberId).orElseThrow(IllegalArgumentException::new);
        boolean isSuccess = passwordEncoder.matches(currentPwd, member.getPassword());
        System.out.println(isSuccess);
        if(isSuccess){
            member.updatePwd(encodingPassword(newPwd));
            return mr.save(member);}
        return null;
    }
}
