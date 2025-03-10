package it.epicode.capstone.youtube;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YouTubeVideoResponse {
    private String videoId;
    private String titolo;
    private String url;
}