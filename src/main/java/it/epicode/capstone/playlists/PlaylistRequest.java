package it.epicode.capstone.playlists;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    //@NotEmpty(message="Ricordati di inserire i link di Youtube")
    private List<String> youtubeUrls;
    private List<Long> vocalMemoIds;
}