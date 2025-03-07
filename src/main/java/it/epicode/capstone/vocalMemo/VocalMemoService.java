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
                                  Playlist playlist,
                                  @AuthenticationPrincipal AppUser user) {
        VocalMemo vocalMemo = new VocalMemo();
        vocalMemo.setUser(user);
        vocalMemo.setPlaylist(playlist);
        vocalMemo.setUrl(request.getUrl()); // Aggiunto per salvare il file audio
        vocalMemo.setNomeRegistrazione("Memo di " + user.getUsername()); // Nome predefinito

        VocalMemo savedVocalMemo = vocalMemoRepository.save(vocalMemo);

        return new VocalMemoResponse(
                savedVocalMemo.getId(),
                savedVocalMemo.getNomeRegistrazione(),
                savedVocalMemo.getDataInserimento(),
                savedVocalMemo.getUser().getId(),
                savedVocalMemo.getPlaylist() != null ? savedVocalMemo.getPlaylist().getId() : null // Ora gestisce il caso null
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

