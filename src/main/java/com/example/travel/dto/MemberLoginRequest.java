package com.example.travel.dto;


import com.example.travel.entity.Member;
import io.jsonwebtoken.Claims;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberLoginRequest {

    private String id;
    private String password;
    private String nickname;
    private String role;

    @Builder
    public MemberLoginRequest(String id, String password, String nickname, String role){
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    //JwtTokenProvider에서 Token 값을 추출하여 dto 객체를 만들때 사용하는 메서드
    public MemberLoginRequest(Claims claims){
        this.id = claims.get("id", String.class);
        this.nickname = claims.get("nickname", String.class);
        this.role = claims.get("role", String.class);
    }

    public Member toEntity(){
        return Member.builder()
                .id(id)
                .password(password)
                .nickname(nickname)
                .role(role)
                .build();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_USER, ROLE_ADMIN 은 정해저 있는 네이밍임, getAuthorities() 이름도 같아야함
        // 만약 하나로 구분이 아니고 여러 개의 권한을 가져야 한다면, 아래와 같이 쓸것
        //return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"),new SimpleGrantedAuthority("ROLE_ADMIN"));
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
