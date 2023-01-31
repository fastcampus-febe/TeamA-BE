package com.example.travel.controller;

import com.example.travel.dto.MemberLoginRequest;
import com.example.travel.dto.MemberSignUpRequest;
import com.example.travel.dto.TokenDto;
import com.example.travel.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"회원 서비스"}, description = "회원 서비스를 담당합니다.")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입을 수행합니다.")
    public String signUp(@RequestBody MemberSignUpRequest req){
        return memberService.signUp(req);
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인을 수행합니다.")
    public TokenDto signIn(@RequestBody MemberLoginRequest req){
        TokenDto res =  memberService.login(req);
        return res;
    }


//    @GetMapping("/hello")
//    @PreAuthorize("hasAnyRole('USER')") // USER 권한만 호출 가능
//    public String hello(@AuthenticationPrincipal UserDto user) {
//        return user.getId() + ", 안녕하세요!";
//    }
//
//    @GetMapping("/helloAdmin")
//    @PreAuthorize("hasAnyRole('ADMIN')") // ADMIN 권한만 호출 가능
//    public String helloAdmin(@AuthenticationPrincipal UserDto user) {
//        return user.getId() + ", 안녕하세요!";
//    }
}
