package com.example.travel.dto;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {

    private String currentPwd;
    private String newPwd;

}

