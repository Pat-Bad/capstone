package it.epicode.capstone.youtube;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YouTubeConfig {

    @Bean
    public YouTube youTube() {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        return new YouTube.Builder(transport, jsonFactory, request -> {})
                .setApplicationName("PlaylistVocaliApp")
                .build();
    }
}
