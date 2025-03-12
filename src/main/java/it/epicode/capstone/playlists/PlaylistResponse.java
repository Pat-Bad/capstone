package it.epicode.capstone.playlists;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.capstone.vocalMemo.VocalMemo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistResponse {
    private Long id;
    private String nomePlaylist;
    private List<String> youtubeUrls;
    private String url;
}