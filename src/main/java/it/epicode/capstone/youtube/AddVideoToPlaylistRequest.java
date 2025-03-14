package it.epicode.capstone.youtube;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddVideoToPlaylistRequest {

    @NotNull(message="Inserisci l'id della playlist")
    private Long playlistId;

    @NotNull(message="Inserisci l'url del video")
    private String youtubeUrl;
}
