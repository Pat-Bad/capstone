package it.epicode.capstone.vocalMemo;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.authentication.AppUserRepository;
import it.epicode.capstone.playlists.Playlist;
import it.epicode.capstone.playlists.PlaylistRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class VocalMemoService {
    private final VocalMemoRepository vocalMemoRepository;
    private final AppUserRepository userRepository;
    private final PlaylistRepository playlistRepository;

//POST

    public VocalMemoResponse save(@Valid @RequestBody VocalMemoRequest request,
                                  @AuthenticationPrincipal AppUser user) {
        // Crea un oggetto VocalMemo
        VocalMemo vocalMemo = new VocalMemo();
        vocalMemo.setUrl(request.getUrl());
        vocalMemo.setUser(user);
        Playlist playlist = playlistRepository.findById(request.getPlaylistId())
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata, ID " + request.getPlaylistId()));
        vocalMemo.setPlaylist(playlist);
        vocalMemo.setNomeRegistrazione("Memo di " + user.getUsername());

        // Salva l'oggetto nel database
        VocalMemo savedVocalMemo = vocalMemoRepository.save(vocalMemo);

        return new VocalMemoResponse(savedVocalMemo.getId(), savedVocalMemo.getNomeRegistrazione(), savedVocalMemo.getUser().getId(),
                savedVocalMemo.getPlaylist().getId(), savedVocalMemo.getUrl());
    }


    //GET
    public VocalMemo findById(Long id) {
        return vocalMemoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VocalMemo non trovato con id: " + id));
    }

    public List<Playlist> findAllByUser(AppUser user) {
        return playlistRepository.findAllByUser(user);
    }

    //PUT
    public VocalMemo update(Long id, @Valid VocalMemoRequest request) {
        VocalMemo vocalMemo = findById(id);
        BeanUtils.copyProperties(request, vocalMemo);
        VocalMemo updatedVocalMemo = vocalMemoRepository.save(vocalMemo);
        return updatedVocalMemo;
    }

    //DELETE
    public void delete(Long id) {
        VocalMemo vocalMemo = findById(id);
        vocalMemoRepository.delete(vocalMemo);
    }

    public List<VocalMemo> findAll() {
        return vocalMemoRepository.findAll();
    }

    public VocalMemo findByPlaylistId(Long playlistId) {
        return vocalMemoRepository.findByPlaylistId(playlistId);
    }
}

