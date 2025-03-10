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
        vocalMemo.setUser(user);
        Playlist playlistId = playlistRepository.findById(request.getPlaylistId())
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata, ID " + request.getPlaylistId()));
        vocalMemo.setPlaylist(playlistId);
        vocalMemo.setUrl(request.getUrl());

        // Imposta un nome di registrazione di default
        vocalMemo.setNomeRegistrazione("Memo di " + user.getUsername());

        // Salva l'oggetto nel database
        VocalMemo savedVocalMemo = vocalMemoRepository.save(vocalMemo);

        // Crea e restituisce la risposta
        return new VocalMemoResponse(
                savedVocalMemo.getId(),  // ID generato automaticamente dal DB
                savedVocalMemo.getNomeRegistrazione(),
                savedVocalMemo.getDataInserimento(),
                savedVocalMemo.getUser().getId(),
                savedVocalMemo.getPlaylist().getId()
        );
    }


    //GET
    public VocalMemo findById(Long id) {
        return vocalMemoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VocalMemo non trovato con id: " + id));
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
}

