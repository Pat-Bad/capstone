package it.epicode.capstone.youtube;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YouTubeVideoResponse {
    private String videoId;
    private String titolo;
    private String url;
}