package com.example.travel.dto;

import com.example.travel.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberSignUpRequest {

    private String id;
    private String password;
    private String nickname;
    private String role;

    public MemberSignUpRequest(Member member){
        this.id = member.getId();
        this.password = member.getPassword();
        this.nickname = member.getNickname();
        this.role = member.getRole();
    }

    public Member toEntity(){
        return Member.builder()
                .id(id)
                .password(password)
                .nickname(nickname)
                .role(role)
                .build();
    }



}
