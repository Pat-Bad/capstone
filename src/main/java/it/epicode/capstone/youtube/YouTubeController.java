package it.epicode.capstone.youtube;

import it.epicode.capstone.playlists.PlaylistResponse;
import it.epicode.capstone.playlists.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/youtube")
@RequiredArgsConstructor
public class YouTubeController {
    private final YouTubeService youTubeService;
    private final PlaylistService playlistService;

    @GetMapping("/search")
    public List<YouTubeVideoResponse> searchVideos(@RequestParam String query){
        return youTubeService.searchVideos(query);
    }

    @PostMapping("/add-video")
    public PlaylistResponse addVideoToPlaylist(@RequestBody @Valid AddVideoToPlaylistRequest request) {
        return playlistService.addVideoToPlaylist(request);
    }

    @DeleteMapping("/remove-video")
    public PlaylistResponse removeVideoFromPlaylist(@RequestBody @Valid RemoveVideoRequest request) {
        return playlistService.removeVideoFromPlaylist(request);
    }

}
