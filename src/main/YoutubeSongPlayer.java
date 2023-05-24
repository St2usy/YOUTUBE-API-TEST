package main;
import java.util.Arrays;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Random;

public class YoutubeSongPlayer {
    private static final String API_KEY = "AIzaSyDFZjXp1e56YlvQ1Y2k2nX33bhOIqYTjaA";
    private static final long MAX_RESULTS = 10;

    private YouTube youtube;

    public YoutubeSongPlayer() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        youtube = new YouTube.Builder(httpTransport, jsonFactory, getRequestInitializer())
                .setApplicationName("YoutubeSongPlayer")
                .build();
    }

    private HttpRequestInitializer getRequestInitializer() {
        return request -> {
        };
    }

    public String getRandomSongTitle(int year) throws IOException {
        String searchQuery = year + "년 노래";
        YouTube.Search.List searchRequest = youtube.search().list(Arrays.asList("id", "snippet"));
        searchRequest.setKey(API_KEY);
        searchRequest.setQ(searchQuery);
        searchRequest.setMaxResults(MAX_RESULTS);

        SearchListResponse searchResponse = searchRequest.execute();
        List<SearchResult> searchResults = searchResponse.getItems();

        if (searchResults != null && !searchResults.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(searchResults.size());
            SearchResult randomResult = searchResults.get(randomIndex);
            return randomResult.getSnippet().getTitle();
        }

        return null;
    }

    public static void main(String[] args) {
        try {
            YoutubeSongPlayer songPlayer = new YoutubeSongPlayer();

            // 임의의 정수 생성 (2000 ~ 2022)
            int year = new Random().nextInt(23) + 2000;

            String randomSongTitle = songPlayer.getRandomSongTitle(year);
            if (randomSongTitle != null) {
                System.out.println("Random Song Title for year " + year + ": " + randomSongTitle);
                // TODO: 음악을 재생하는 코드를 추가해야 합니다.
            } else {
                System.out.println("No songs found for year " + year);
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}
