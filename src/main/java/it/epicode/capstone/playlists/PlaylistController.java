package it.epicode.capstone.playlists;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final PlaylistRepository playlistRepository;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistResponse createPlaylist(@RequestBody PlaylistRequest request) {
        return playlistService.createPlaylist(request);
    }
}
