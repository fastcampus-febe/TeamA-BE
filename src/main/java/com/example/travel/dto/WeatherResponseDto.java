package com.example.travel.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class WeatherResponseDto {
    private List<WeatherShortResponseDto> weatherShortResponseDto;
}