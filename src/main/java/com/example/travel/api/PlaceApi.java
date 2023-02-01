package com.example.travel.api;

import com.example.travel.entity.Place;
import com.example.travel.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlaceApi {

    private final PlaceService placeService;
        private final String tourApiKey = "";


    private String tempSb;
//    @PostConstruct
    public void init() {
        try {
            getPlaceInfo();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public void getPlaceInfo() throws IOException, ParseException {
        String baseUrl = "http://apis.data.go.kr/B551011/KorService";

        //지역 기반 조회를 통해 contentId insert 해줌.
        for (int i = 1; i <= 12010; i++) {
            StringBuilder urlBuilder = new StringBuilder(baseUrl);
            urlBuilder.append("/areaBasedList");  /*지역 기반 조회 api*/
            urlBuilder.append("?" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(1), "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(i), "UTF-8")); /*페이지 번호*/
            urlBuilder.append("&" + "MobileOS=etc&MobileApp=travel"); /*OS 구분: etc, 서비스명:travel*/
            urlBuilder.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(tourApiKey, "UTF-8")); /*Service Key*/
//            urlBuilder.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + tourApiKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*JSON 타입의 응답*/
            urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode("O", "UTF-8")); /*대표이미지가 반드시 있는 정렬 O=제목순*/
            urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode("12", "UTF-8")); /*관광타입:관광지*/

            String sb = connApi(urlBuilder);

            String contentId = (String) jsonParsing(sb).get("contentid");

            if (!contentId.equals("")) {
                if (placeService.findByContentId(contentId)) continue;
                else {
                }
            }

            urlBuilder = new StringBuilder(baseUrl);
            urlBuilder.append("/detailCommon");  /*공통 정보 조회 api*/
            urlBuilder.append("?" + "MobileOS=etc&MobileApp=travel"); /*OS 구분: etc, 서비스명:travel*/
            urlBuilder.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(tourApiKey, "UTF-8")); /*Service Key*/
//            urlBuilder.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + tourApiKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*JSON 타입의 응답*/
            urlBuilder.append("&" + URLEncoder.encode("contentId", "UTF-8") + "=" + URLEncoder.encode(contentId, "UTF-8")); /*contentId*/
            urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(12), "UTF-8")); /*관광타입:관광지*/
            urlBuilder.append("&" + URLEncoder.encode("defaultYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8")); /*기본정보조회여부*/
            urlBuilder.append("&" + URLEncoder.encode("firstImageYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8")); /*원본, 썸네일대표이미지조회여부*/
            urlBuilder.append("&" + URLEncoder.encode("areacodeYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8")); /*지역코드, 시군구코드조회여부*/
            urlBuilder.append("&" + URLEncoder.encode("catcodeYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8")); /*대,중,소분류코드조회여부*/
            urlBuilder.append("&" + URLEncoder.encode("addrinfoYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8")); /*주소, 상세주소조회여부*/
            urlBuilder.append("&" + URLEncoder.encode("mapinfoYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8")); /*좌표X, Y 조회여부*/
            urlBuilder.append("&" + URLEncoder.encode("overviewYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8")); /*콘텐츠개요조회여부*/

            sb = connApi(urlBuilder);
            Place place = jsonParsingToEntity(sb);

            placeService.save(place);
        }
    }

    public String connApi(StringBuilder urlBuilder) throws IOException {
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            log.info("Response code: {}", conn.getResponseCode());
            BufferedReader rd;

            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            tempSb = sb.toString();
            return sb.toString();

        } catch (SocketException se) {
            if(conn != null) conn.disconnect();
        }
        return tempSb;
    }

    public JSONObject jsonParsing(String sb) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(sb);
        JSONObject response = (JSONObject) obj.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items.get("item");
        JSONObject objResult = (JSONObject) item.get(0);

        return objResult;
    }

    public Place jsonParsingToEntity(String sb) throws ParseException {
        JSONObject objResult = jsonParsing(sb);
        if(objResult.get("areacode").equals("")) objResult.put("areacode", "0");
        Place place = Place.builder()
                .contentId(Integer.parseInt((String) objResult.get("contentid")))
                .title((String) objResult.get("title"))
                .addr1((String) objResult.get("addr1"))
                .addr2((String) objResult.get("addr2"))
                .areaCode(Integer.parseInt((String) objResult.get("areacode")))
                .sigunguCode((String) objResult.get("sigungucode"))
                .cat1((String) objResult.get("cat1"))
                .cat2((String) objResult.get("cat2"))
                .cat3((String) objResult.get("cat3"))
                .firstImage((String) objResult.get("firstimage"))
                .firstImage2((String) objResult.get("firstimage2"))
                .mapX(Double.parseDouble((String) objResult.get("mapx")))
                .mapY(Double.parseDouble((String) objResult.get("mapy")))
                .tel((String) objResult.get("tel"))
                .homepage((String) objResult.get("homepage"))
                .overview((String) objResult.get("overview"))
                .build();
        return place;
    }

}
