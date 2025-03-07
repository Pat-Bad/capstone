package it.epicode.capstone.playlists;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistRequest {
    @NotBlank(message="Dai un nome alla tua playlist")
    private String nomePlaylist;
    private List<String> youtubeUrls;
}