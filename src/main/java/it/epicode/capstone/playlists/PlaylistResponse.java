package it.epicode.capstone.playlists;

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
    private List<Long> vocalMemoIds;
}
