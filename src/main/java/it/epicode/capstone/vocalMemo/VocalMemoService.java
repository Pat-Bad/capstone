package it.epicode.capstone.vocalMemo;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.authentication.AppUserRepository;
import it.epicode.capstone.playlists.Playlist;
import it.epicode.capstone.playlists.PlaylistRepository;
import it.epicode.capstone.playlists.PlaylistResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.util.List;


@Service
@Validated
@RequiredArgsConstructor
public class VocalMemoService {
    private final VocalMemoRepository vocalMemoRepository;
    private final AppUserRepository userRepository;
    private final PlaylistRepository playlistRepository;

//POST
@Transactional
public VocalMemoResponse save(@Valid VocalMemoRequest request, Long playlistId, @AuthenticationPrincipal AppUser user) {
//creo la vocalmemo
    VocalMemo vocalMemo = new VocalMemo();
    vocalMemo.setNomeRegistrazione(request.getNomeRegistrazione());
    vocalMemo.setUser(user);
    //devo trovare la playlist con l'id
    Playlist playlist = playlistRepository.findById(playlistId)
            .orElseThrow(() -> new EntityNotFoundException("Playlist not found with id: " + playlistId));
    vocalMemo.setPlaylist(playlist);
    //salvo la vocalmemo
    VocalMemo savedVocalMemo = vocalMemoRepository.save(vocalMemo);
    //creo la response


   VocalMemoResponse response = new VocalMemoResponse();

    response.setId(vocalMemo.getId());
    response.setNomeRegistrazione(vocalMemo.getNomeRegistrazione());
    response.setDataInserimento(vocalMemo.getDataInserimento());
    response.setUserId(vocalMemo.getUser().getId());
    response.setPlaylistId(vocalMemo.getPlaylist().getId());
    return response;
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
        List<VocalMemo> vocalMemos = vocalMemoRepository.findAll();
        return vocalMemos;
    }
}

