package it.epicode.capstone.playlists;

import it.epicode.capstone.vocalMemo.VocalMemo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistRequest {
    @NotBlank(message="Dai un nome alla tua playlist")
    private String nomePlaylist;
    List<String> youtubeUrls = new ArrayList<>();
    private LocalDate dataRegistrazione;
    VocalMemo vocalMemo;

}