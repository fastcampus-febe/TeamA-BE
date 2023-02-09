package com.example.travel.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class WeatherShortResponseDto {
    private LocalDate date;
    private String weatherAM;
    private String weatherPM;
    private String precipitationAM;
    private String precipitationPM;
    private String minTemp;
    private String maxTemp;
}