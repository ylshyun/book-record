package com.boot.service;

import com.boot.dto.BookDTO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class NaverApi {

    private static final String apiURL = "https://openapi.naver.com/v1/search/book.json?query=";

    @Value("${naver_client_id}")
    private String clientId;

    @Value("${naver_client_secret}")
    private String clientSecret;

    public List<BookDTO> searchBooksByKeyword(String keyword) {

        try {
            String encodeKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            String apiUrlWithParams = apiURL + encodeKeyword;
            URL url = new URL(apiUrlWithParams);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            StringBuilder response = getStringBuilder(con);
            con.disconnect();

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("items");
            List<BookDTO> bookDtoList = new ArrayList<>();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject book = jsonArray.get(i).getAsJsonObject();

                // isbn이 존재하고, 비어있지 않은 경우에만 리스트에 넣기
                if (book.has("isbn") && !book.get("isbn").getAsString().trim().isEmpty()) {
                    bookDtoList.add(BookDTO.builder().bookTitle(book.get("title").getAsString()).bookAuthor(book.get("author").getAsString()).bookPublisher(book.get("publisher").getAsString()).bookDiscount(book.get("discount").getAsInt()).bookIsbn(book.get("isbn").getAsString()).bookImageURL(book.get("image").getAsString()).bookDescription(book.get("description").getAsString()).build());
                }
            }
            return bookDtoList;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static StringBuilder getStringBuilder(HttpURLConnection con) throws IOException {
        int responseCode = con.getResponseCode();
        BufferedReader br;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();
        return response;
    }
}