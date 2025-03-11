package it.epicode.capstone.vocalMemo;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.playlists.PlaylistRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/vocalmemo")
@PreAuthorize("hasRole('ROLE_USER')")
public class VocalMemoController {

    private final VocalMemoService service;
    private final VocalMemoRepository vocalMemoRepository;
    private final PlaylistRepository playlistRepository; // Repository per le playlist
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)

    public VocalMemoResponse save(@Valid @RequestBody VocalMemoRequest request,
                                  @AuthenticationPrincipal AppUser user) {
        return service.save(request, user);
    }

//    @PostMapping("")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @ResponseStatus(HttpStatus.CREATED)
//    public VocalMemoResponse save(@Valid @RequestBody VocalMemoRequest request,
//                                  @AuthenticationPrincipal AppUser user) {
//        Playlist playlist = null;
//
//        if (request.getPlaylistId() != null) {
//            playlist = playlistRepository.findById(request.getPlaylistId())
//                    .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata, ID " + request.getPlaylistId()));
//        }
//
//        return service.save(request, playlist, user);
//    }


   @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
   public List<VocalMemo> findAll() {
       return service.findAll();
    }




    @GetMapping("/{playlistId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<VocalMemoResponse> getVocalMemo(@PathVariable Long playlistId) {
        VocalMemo vocalMemo = service.findByPlaylistId(playlistId);
        if (vocalMemo != null) {
            VocalMemoResponse response = new VocalMemoResponse(
                    vocalMemo.getId(),
                    vocalMemo.getNomeRegistrazione(),
                    vocalMemo.getUser().getId(),
                    vocalMemo.getPlaylist().getId(),
                    vocalMemo.getUrl()
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public VocalMemo update(@PathVariable Long id, @Valid @RequestBody VocalMemoRequest request)  {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)  {
        service.delete(id);
    }
}

