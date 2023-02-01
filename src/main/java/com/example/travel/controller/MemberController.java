package com.example.travel.controller;

import com.example.travel.dto.MemberLoginRequest;
import com.example.travel.dto.MemberSignUpRequest;
import com.example.travel.dto.TokenDto;
import com.example.travel.entity.Member;
import com.example.travel.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "마이페이지내 회원 정보", notes = "회원의 정보를 보여줍니다.")
    @GetMapping("/myPage/info/{id}")
    public Member mypageInfo(@PathVariable String id){
        Member member = memberService.findById(id);
        return member;
    }

    @ApiOperation(value = "패스워드 변경", notes = "패스워드를 확인후 변경합니다.")
    @PostMapping("/myPage/changePassword/{id}")
    public @ResponseBody String changePassword(@PathVariable("id") String id,
                                               @RequestParam("currentPwd") String currentPwd,
                                               @RequestParam("newPwd") String newPwd) {
        if (currentPwd.equals(newPwd)){
            return "비밀번호가 동일합니다.";
        }
        if (memberService.updatePwd(id, currentPwd, newPwd) == null){
            return "비밀번호가 일치하지않습니다.";
        } else {
            return "비밀번호가 변경되었습니다.";
        }
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
