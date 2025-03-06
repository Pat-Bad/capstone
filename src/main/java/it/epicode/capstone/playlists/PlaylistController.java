package it.epicode.capstone.playlists;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.youtube.AddVideoToPlaylistRequest;
import it.epicode.capstone.youtube.RemoveVideoRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/playlist")
@PreAuthorize("hasRole('ROLE_USER')")
public class PlaylistController {
    private final PlaylistService playlistService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistResponse save(@Valid PlaylistRequest request, @AuthenticationPrincipal AppUser user) {
        return playlistService.save(request, user);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Playlist> findAll() {
        return playlistService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Playlist findById(@PathVariable Long id) {
        return playlistService.findById(id);

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlaylistResponse update(@PathVariable Long id, @Valid PlaylistRequest request) {
        return playlistService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        playlistService.delete(id);
    }

}