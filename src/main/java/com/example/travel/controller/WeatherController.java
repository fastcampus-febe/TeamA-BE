package com.example.travel.controller;

import com.example.travel.dto.MapRequestDto;
import com.example.travel.dto.WeatherResponseDto;
import com.example.travel.service.WeatherService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather")
    public Result findWeather(@RequestBody MapRequestDto mapRequestDto) throws IOException, ParseException {
        WeatherResponseDto weatherResDTO = weatherService.findWeatherResDTOByMapXAndMapY(mapRequestDto.getMapX(), mapRequestDto.getMapY());
        return new Result(weatherResDTO);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}