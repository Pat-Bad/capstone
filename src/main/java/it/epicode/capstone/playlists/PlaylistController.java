package it.epicode.capstone.playlists;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public PlaylistResponse save(@RequestBody @Valid PlaylistRequest request){
        return playlistService.save(request);
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


}
