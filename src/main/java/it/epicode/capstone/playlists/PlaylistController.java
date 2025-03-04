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

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<PlaylistResponse> findAll(){
        return playlistService.findAll();}

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public PlaylistResponse findById(@PathVariable Long id){
        return playlistService.findById(id);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistResponse save(@RequestBody @Valid PlaylistRequest request,@AuthenticationPrincipal AppUser user){
        return playlistService.save(request, user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public PlaylistResponse update(@PathVariable Long id, @RequestBody @Valid PlaylistRequest request){
        return playlistService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        playlistService.delete(id);
    }

    @PatchMapping("/{playlistId}/add-memo/{memoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public PlaylistResponse addVocalMemoToPlaylist(@PathVariable Long playlistId, @PathVariable Long memoId) {
        return playlistService.addVocalMemoToPlaylist(playlistId, memoId);
    }

    @PatchMapping("/{playlistId}/remove-memo/{memoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public PlaylistResponse removeVocalMemoFromPlaylist(@PathVariable Long playlistId, @PathVariable Long memoId) {
        return playlistService.removeVocalMemoFromPlaylist(playlistId, memoId);
    }

    @PostMapping("/add-video")
    public PlaylistResponse addVideoToPlaylist(@RequestBody @Valid AddVideoToPlaylistRequest request) {
        return playlistService.addVideoToPlaylist(request);
    }

    @DeleteMapping("/remove-video")
    public PlaylistResponse removeVideoFromPlaylist(@RequestBody @Valid RemoveVideoRequest request) {
        return playlistService.removeVideoFromPlaylist(request);
    }

    @GetMapping("/{id}/contenuti")
    public PlaylistResponse getPlaylistWithContent(@PathVariable Long id) {
        return playlistService.getPlaylistWithContent(id);
    }

}