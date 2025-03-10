package it.epicode.capstone.youtube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YouTubeService {
    private final YouTube youtube;

    @Value("${youtube.api.key}")
    private String apiKey;

    public List<YouTubeVideoResponse> searchVideos(String query) {

        try {
            YouTube.Search.List search = youtube.search().list("id,snippet");
            search.setKey(apiKey);
            search.setQ(query);
            search.setType("video");
            search.setMaxResults(10L);
            search.setFields("items(id/videoId,snippet/title)");

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResults = searchResponse.getItems();

            if (searchResults == null || searchResults.isEmpty()) {
                return List.of(); // controllo per evitare NullPointerException, torna lista vuota
            }
            //streammo i risultati con map per avere una lista
            return searchResults.stream()
                    .map(result -> new YouTubeVideoResponse(
                            result.getId().getVideoId(),
                            result.getSnippet().getTitle(),
                            "https://www.youtube.com/watch?v=" + result.getId().getVideoId()
                    ))
                    .collect(Collectors.toList());
        }

        catch (Exception e) {throw new RuntimeException("Errore con l'API di YouTube: " + e.getMessage(), e);}
    }
}
