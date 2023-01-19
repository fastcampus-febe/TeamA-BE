package com.example.travel.controller;

import com.example.travel.dto.TokenDto;
import com.example.travel.dto.UserDto;
import com.example.travel.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"회원 서비스"}, description = "회원 서비스를 담당합니다.")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final UserService userAccountService;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입을 수행합니다.")
    public UserDto signUp(UserDto addUserDto) {
        UserDto savedUser = userAccountService.signUp(addUserDto);
        return savedUser;
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인을 수행합니다.")
    public TokenDto signIn(UserDto loginUserDto) {
        TokenDto loginUserResponse = userAccountService.login(loginUserDto);
        return loginUserResponse;
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAnyRole('USER')") // USER 권한만 호출 가능
    public String hello(@AuthenticationPrincipal UserDto user) {
        return user.getId() + ", 안녕하세요!";
    }

    @GetMapping("/helloAdmin")
    @PreAuthorize("hasAnyRole('ADMIN')") // ADMIN 권한만 호출 가능
    public String helloAdmin(@AuthenticationPrincipal UserDto user) {
        return user.getId() + ", 안녕하세요!";
    }
}
