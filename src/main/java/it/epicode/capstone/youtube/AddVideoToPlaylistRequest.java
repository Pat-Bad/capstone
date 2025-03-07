package it.epicode.capstone.youtube;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//request per aggiungere video alla playlist
//serve id e url di youtube

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddVideoToPlaylistRequest {

    @NotNull(message="Inserisci l'id della playlist")
    private Long playlistId;

    @NotNull(message="Inserisci l'url del video")
    private String youtubeUrl;
}
