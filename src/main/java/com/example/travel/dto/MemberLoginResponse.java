package com.example.travel.dto;

import com.example.travel.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class MemberLoginResponse {
    //변수 선언, 생성자

    private String id;
    private String nickname;
    private String role;
    private String token;
    private String loginFail;

    public MemberLoginResponse(Member member, String token){
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.role = member.getRole();
        this.token = token;
    }

    public MemberLoginResponse(String loginFail){
        this.loginFail = loginFail;
    }
}
