package com.example.travel.controller;

import com.example.travel.dto.*;
import com.example.travel.entity.Member;
import com.example.travel.ext.JwtTokenProvider;
import com.example.travel.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = {"회원 서비스"}, description = "회원 서비스를 담당합니다.")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입을 수행합니다.")
    public String signUp(@RequestBody MemberSignUpRequest req){
        return memberService.signUp(req);
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인을 수행합니다.")
    public MemberLoginResponse signIn(@RequestBody MemberLoginRequest req){
        return memberService.login(req);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃", notes = "로그아웃을 수행합니다.")
    public String logout(HttpServletRequest request){
        return memberService.logout(request);
    }

    @ApiOperation(value = "마이페이지내 회원 정보", notes = "회원의 정보를 보여줍니다.")
    @GetMapping("/myPage/info/{id}")
    public Member mypageInfo(@PathVariable String id){
        Member member = memberService.findById(id);
        return member;
    }

    @ApiOperation(value = "패스워드 변경", notes = "패스워드를 확인후 변경합니다.")
    @PostMapping("/myPage/changePassword/{id}")
    public String changePassword(@PathVariable("id") String id,
                                               @RequestBody PasswordDto dto) {
        String currentPwd = dto.getCurrentPwd();
        String newPwd = dto.getNewPwd();
        if (memberService.updatePwd(id, currentPwd, newPwd) == null){
            return "현재 비밀번호가 일치하지않습니다.";}
        if (currentPwd.equals(newPwd)){
            return "현재 비밀번호와 동일합니다.";
        } else {
            return "비밀번호가 변경되었습니다.";
        }
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원을 탈퇴합니다")
    @DeleteMapping("/myPage/info/{id}")
    public String deleteMember(@PathVariable String id){
        try {
            memberService.deleteMember(id);
            return "탈퇴가 완료되었습니다.";
        } catch (Exception e){
            e.printStackTrace();
            return "탈퇴를 실패하였습니다.";
        }
    }

    @ApiOperation(value = "찜 목록 조회", notes = "회원의 찜 목록을 조회합니다.")
    @GetMapping("/myPage/favor/{id}")
    public List<FavorResponseDto> getFavors(@PathVariable String id, @PageableDefault(page=0, size=10, sort="id", direction= Sort.Direction.DESC) Pageable pageable){
        return memberService.getFavors(id, pageable);
    }

    @ApiOperation(value = "리뷰 목록 조회", notes = "회원의 리뷰 목록을 조회합니다.")
    @GetMapping("/myPage/review/{id}")
    public List<ReviewResponseDto> getReviews(@PathVariable String id, @PageableDefault(page=0, size=10, sort="id", direction= Sort.Direction.DESC) Pageable pageable){
        return memberService.getReviews(id, pageable);
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
