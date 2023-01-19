package com.example.travel.dto;

import com.example.travel.entity.User;
import io.jsonwebtoken.Claims;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserDto { // 편의상 파라미터 IN/OUT을 담당하는 DTO객체

    private String id;
    private String password;
    private String nickname;
    private String role;

    public UserDto(Claims claims) { //JwtTokenProvider에서 Token 값을 추출하여 UserDto 객체를 만들때 사용하는 메서드
        this.id = claims.get("id", String.class);
        this.role = claims.get("role", String.class);
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.role = user.getRole();
    }

    public User toEntity() {//insert 할때 쓰려고 한 메서드
        return User.builder().id(this.id)
                .password(this.password)
                .nickname(this.nickname)
                .role(this.role)
                .build();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_USER, ROLE_ADMIN 은 정해저 있는 네이밍임, getAuthorities() 이름도 같아야함
        // 만약 하나로 구분이 아니고 여러 개의 권한을 가져야 한다면, 아래와 닽이 쓸것
        //return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"),new SimpleGrantedAuthority("ROLE_ADMIN"));
        return Collections.singleton(new SimpleGrantedAuthority(this.role));
    }
}
