package it.epicode.capstone.playlists;

import it.epicode.capstone.vocalMemo.VocalMemo;
import it.epicode.capstone.vocalMemo.VocalMemoResponse;
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
}