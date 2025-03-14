package it.epicode.capstone.playlists;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.cloudinary.CloudinaryService;
import it.epicode.capstone.vocalMemo.VocalMemo;
import it.epicode.capstone.vocalMemo.VocalMemoService;
import it.epicode.capstone.youtube.ModifyVideoRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/playlist")
@PreAuthorize("hasRole('ROLE_USER')")
@CrossOrigin(origins = "http://localhost:5173")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final VocalMemoService vocalMemoService;
    private final CloudinaryService cloudinaryService;


    @GetMapping("/with-audio")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<PlaylistResponse> findAllByUser(@AuthenticationPrincipal AppUser user) {
        return playlistService.findAllByUser(user);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public PlaylistResponse update(@PathVariable Long id, @Valid @RequestBody PlaylistRequest request) {
        return playlistService.update(id, request);
    }

    @PatchMapping("/{id}/modify-video")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public PlaylistResponse modifyVideoInPlaylist(@Valid @RequestBody ModifyVideoRequest request) {
        return playlistService.modifyVideoInPlaylist(request);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        playlistService.delete(id);
    }


    @PostMapping("/with-audio")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> saveWithMemo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("playlistName") String playlistName,
            @RequestParam("youtubeUrls") List<String> youtubeUrls,
            @AuthenticationPrincipal AppUser user) {

        try {
            // Carica l'audio su Cloudinary
            String audioUrl = cloudinaryService.uploadAudioToCloudinary(file);

            Playlist playlist = new Playlist();
            playlist.setNomePlaylist(playlistName);
            playlist.setUser(user);
            playlist.setYoutubeUrls(youtubeUrls);

            Playlist savedPlaylist = playlistService.save(playlist);
            if (savedPlaylist == null) {
                throw new RuntimeException("Errore durante il salvataggio della playlist.");
            }

            // Crea il VocalMemo e associare alla playlist
            VocalMemo vocalMemo = new VocalMemo();
            vocalMemo.setUrl(audioUrl); // URL dell'audio su Cloudinary
            vocalMemo.setPlaylist(savedPlaylist);
            vocalMemo.setUser(user);
            vocalMemo.setNomeRegistrazione("Memo di " + user.getUsername());

            VocalMemo savedVocalMemo = vocalMemoService.save(vocalMemo);
            if (savedVocalMemo == null) {
                throw new RuntimeException("Errore durante il salvataggio del vocal memo.");
            }

            // Associo il VocalMemo alla playlist
            savedPlaylist.setVocalMemo(savedVocalMemo);

            // Salva di nuovo la playlist con il vocal memo associato
            savedPlaylist = playlistService.save(savedPlaylist);

            PlaylistResponse playlistResponse = new PlaylistResponse(
                    savedPlaylist.getId(),
                    savedPlaylist.getNomePlaylist(),
                    savedPlaylist.getYoutubeUrls(),
                    savedVocalMemo.getUrl()  // URL del VocalMemo
            );
            return ResponseEntity.ok(playlistResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il caricamento su Cloudinary: " + e.getMessage());
        }
    }
}
