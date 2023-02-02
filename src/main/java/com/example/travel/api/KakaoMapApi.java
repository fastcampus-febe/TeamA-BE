package com.example.travel.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
public class KakaoMapApi {

    public static void main(String[] args) throws Exception {
        System.out.println(addrToCoord(URLEncoder.encode("서울특별시 강남구 강남대로 364","UTF-8")));
        System.out.println(coordToAddr("37","126"));
    }

    public static String addrToCoord(String addr) {
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + addr;
        String json = "";
        try {
            json = getJSONData(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String coordToAddr(String x, String y) {
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=" + x + "&y=" + y + "&input_coord=WGS84";
        String json = "";
        try {
            json = getJSONData(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    private static String getJSONData(String apiUrl) throws Exception {
        String jsonString = new String();
        String buf;
        String apikey = "3ab920c3e1dbb15b05c7d166bdf5f35d"; //apikey

        URL url = new URL(apiUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        String auth = "KakaoAK " + apikey;
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-Requested-With", "curl");
        conn.setRequestProperty("Authorization", auth);

        BufferedReader br = new BufferedReader(new InputStreamReader(
                conn.getInputStream(), "UTF-8"));
        while ((buf = br.readLine()) != null) {
            jsonString += buf;
        }
        return jsonString;
    }
}
