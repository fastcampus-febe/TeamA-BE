package com.example.travel.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokenDto {
    private final String accessToken;

    public TokenDto(String accessToken) {
        this.accessToken = accessToken;
    }

}
