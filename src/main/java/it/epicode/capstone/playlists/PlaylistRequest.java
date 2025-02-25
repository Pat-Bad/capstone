package it.epicode.capstone.playlists;

import it.epicode.capstone.vocalMemo.VocalMemo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistRequest {
    private String nomePlaylist;
    private List<String> youtubeUrls;
    private List<Long> vocalMemoIds; //ID VocalMemo vocalMemo;
}
