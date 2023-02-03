package com.example.travel.entity;


import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
@ToString
public class Member {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;
    @Column(name = "role", nullable = false)
    private String role;

    @Builder
    public Member(String id, String password, String nickname, String role){
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public void updatePwd(String newPwd){
        this.password = newPwd; }
}
