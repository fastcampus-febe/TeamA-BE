package com.example.travel.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
@ToString
public class User {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "nickname", nullable = false)
    private String nickname;
    @Column(name = "role", nullable = false)
    private String role;

    @Builder
    public User(String id, String password, String nickname, String role) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }
}
