package com.example.travel.service;

import com.example.travel.dto.WeatherResponseDto;
import com.example.travel.dto.WeatherShortResponseDto;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    public WeatherResponseDto findWeatherResDTOByMapXAndMapY(String mapX, String mapY) throws IOException, ParseException {
        List<WeatherShortResponseDto> shortResponse = selectShortWeather(mapX, mapY);
        WeatherResponseDto weatherResponseDto = new WeatherResponseDto(shortResponse);
        return weatherResponseDto;
    }

    public List<WeatherShortResponseDto> selectShortWeather(String mapX, String mapY) throws IOException, ParseException {
        List<WeatherShortResponseDto> shortResponseDTOs = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            JSONArray shortJSONArray = selectShortWeatherJSON(mapX, mapY, i);

            // JSONArray(shortJSONArray)에서 필요한 값을 찾기 위해, index로 쓰일 값을 초기화함
            int eightAmPOPRainNum = 0;

            // 필요한 값(오전 8시의 강수확률(POP) = eightAmPOPRainNum)을 찾기 위해서
            // for문(반복문)을 돌며 JSONArray(shortJSONArray)에서 해당 index 값을 찾음
            for (int k = 0; k < shortJSONArray.size(); k++) {
                JSONObject jsonObject = (JSONObject)shortJSONArray.get(k);

                if (jsonObject.get("category").equals("POP") && jsonObject.get("fcstTime").equals("0800")) {
                    eightAmPOPRainNum = k;
                    break;
                }
            }

            // 사용하는 날씨 api (https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15084084) 에서
            // eightAmPOPRainNum(오전 8시의 강수확률(POP) 데이터가 있는 index 값)을 알면 나머지 필요한 정보의 index 값도 알 수 있으므로
            // 위에서 찾은 eightAmPOPRainNum로 나머지 index 값도 구함.
            int fivePmPOPRainNum = eightAmPOPRainNum+109;
            int eightAmSKYWeatherNum = eightAmPOPRainNum-2;
            int fivePmSKYWeatherNum = eightAmPOPRainNum+107;
            int lowTempTMNNum = eightAmPOPRainNum-20;
            int highTempTMXNum = eightAmPOPRainNum+89;

            // WeatherShortMiddleResDTO로 만들기 위한 작업
            // 위에서 찾은 index 값으로 데이터를 가져온다
            LocalDate date = LocalDate.now().plusDays(i-1);
            String rainAm = getFcstValueByJSONArrayIndex(shortJSONArray, eightAmPOPRainNum);
            String rainPm = getFcstValueByJSONArrayIndex(shortJSONArray, fivePmPOPRainNum);

            String weatherAmBeforeConvert = getFcstValueByJSONArrayIndex(shortJSONArray, eightAmSKYWeatherNum);
            String weatherAm = convertWeatherToStringByWeatherNum(weatherAmBeforeConvert);
            String weatherPmBeforeConvert = getFcstValueByJSONArrayIndex(shortJSONArray, fivePmSKYWeatherNum);
            String weatherPm = convertWeatherToStringByWeatherNum(weatherPmBeforeConvert);

            String lowTemp = getFcstValueByJSONArrayIndex(shortJSONArray, lowTempTMNNum);
            String highTemp = getFcstValueByJSONArrayIndex(shortJSONArray, highTempTMXNum);

            // WeatherShortMiddleResDTO 생성
            WeatherShortResponseDto shortResponseDTO = WeatherShortResponseDto.builder()
                    .date(date).precipitationAM(rainAm)
                    .precipitationPM(rainPm).weatherAM(weatherAm)
                    .weatherPM(weatherPm).minTemp(lowTemp).maxTemp(highTemp).build();

            // List<WeatherShortResponseDto> shortResponseDTOs에 WeatherShortResponseDto를 추가하고
            shortResponseDTOs.add(shortResponseDTO);
        }

        // 그 List (List<WeatherShortResponseDto> shortResponseDTOs) 를 반환
        return shortResponseDTOs;
    }

    public JSONArray selectShortWeatherJSON(String mapX, String mapY, int pageNo) throws IOException, ParseException {

        String yesterday = LocalDate.now().minusDays(1).toString().replace("-", "");

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + "pxw6VT6wxnVaFA9zeJwEBt73z6SvUq1Pz6mSwCgKHMH8B0G3dw0LWGUing7x4F7Ft1oH0I9O13FX6hu%2BxCYhfw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("294", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(yesterday, "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("2300", "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(String.valueOf(Math.round(Float.parseFloat(mapY))), "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(String.valueOf(Math.round(Float.parseFloat(mapX))), "UTF-8")); /*예보지점의 Y 좌표값*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();

        // result는 api로부터 받아온 값
        String result = sb.toString();

        JSONParser parser = new JSONParser(); // Json parser를 만들어 만들어진 문자열 데이터를 객체화
        JSONObject obj = (JSONObject) parser.parse(result);

        JSONObject parse_response = (JSONObject) obj.get("response"); // response 키를 가지고 데이터를 파싱
        JSONObject parse_body = (JSONObject) parse_response.get("body"); // response 로 부터 body 찾기
        JSONObject parse_items = (JSONObject) parse_body.get("items"); // body 로 부터 items 찾기
        JSONArray parse_item = (JSONArray) parse_items.get("item"); // items로 부터 itemlist 를 받기
        return parse_item;
    }

    public String getFcstValueByJSONArrayIndex(JSONArray jsonArray, int jsonIndex) {
        JSONObject jsonObject = (JSONObject)jsonArray.get(jsonIndex);
        return String.valueOf(jsonObject.get("fcstValue"));
    }

    public String convertWeatherToStringByWeatherNum(String stringWeatherNum) {
        double weatherNum = Double.parseDouble(stringWeatherNum);
        String weatherString = "";

        if ( weatherNum < 6 ) {
            weatherString = "맑음";
        } else if ( weatherNum < 9 ) {
            weatherString = "구름많음";
        } else if (weatherNum < 11) {
            weatherString = "흐림";
        } else {
            weatherString = "?";
        }

        return weatherString;
    }
}
