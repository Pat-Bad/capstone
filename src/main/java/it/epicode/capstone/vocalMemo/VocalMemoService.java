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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class VocalMemoService {

    private final VocalMemoRepository vocalMemoRepository;
    private final AppUserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final VocalMemoMapper vocalMemoMapper;

    // POST - Crea un nuovo memo vocale
    @Transactional
    public VocalMemoResponse save(@Valid VocalMemoRequest request, Long playlistId, @AuthenticationPrincipal AppUser user) {
        // Crea un nuovo VocalMemo
        VocalMemo vocalMemo = new VocalMemo();
        vocalMemo.setNomeRegistrazione(request.getNomeRegistrazione());
        vocalMemo.setRegistrazione(request.getRegistrazione());  // byte array
        vocalMemo.setDataInserimento(request.getDataInserimento());
        vocalMemo.setUser(user);  // Associa l'utente

        // Se viene passato un playlistId, recupera la playlist e associala al memo
        if (playlistId != null) {
            Playlist playlist = playlistRepository.findById(playlistId)
                    .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));
            vocalMemo.setPlaylist(playlist);  // Associa la playlist al vocal memo

            // Assicurati che la lista dei VocalMemo esista nella Playlist
            if (playlist.getVocalMemos() == null) {
                playlist.setVocalMemos(new ArrayList<>());  // Se la lista è vuota, creala
            }
            playlist.getVocalMemos().add(vocalMemo);  // Aggiungi il vocal memo alla playlist
        }

        // Salva il nuovo vocal memo e la playlist aggiornata
        vocalMemoRepository.save(vocalMemo);

        // Se la playlist è stata modificata, salvala
        if (playlistId != null) {
            playlistRepository.save(vocalMemo.getPlaylist());
        }

        return vocalMemoMapper.toVocalMemoResponse(vocalMemo);
    }


    // PUT - Aggiorna un memo vocale esistente
    public VocalMemoResponse updateVocalMemo(Long id, @Valid VocalMemoRequest request) {
        VocalMemo vocalMemo = vocalMemoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Memo vocale non trovato"));

        BeanUtils.copyProperties(request, vocalMemo);
        vocalMemoRepository.save(vocalMemo);
        return vocalMemoMapper.toVocalMemoResponse(vocalMemo);
    }

    // DELETE - Elimina un memo vocale
    public void deleteVocalMemo(Long id) {
        VocalMemo vocalMemo = vocalMemoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Memo vocale non trovato"));
        vocalMemoRepository.delete(vocalMemo);
    }

    // GET - Trova un memo vocale per ID
    public byte[] getVocalMemoById(Long id) {
        VocalMemo vocalMemo = vocalMemoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Memo vocale non trovato"));

        return vocalMemo.getRegistrazione();
    }

    // GET - Trova tutti i memo vocali di un utente
    public List<VocalMemoResponse> getAllVocalMemosByUser(@AuthenticationPrincipal AppUser user) {
        List<VocalMemo> vocalMemos = vocalMemoRepository.findByUser(user);
        List<VocalMemoResponse> responses = new ArrayList<>();

        for (VocalMemo vocalMemo : vocalMemos) {
            responses.add(vocalMemoMapper.toVocalMemoResponse(vocalMemo));
        }

        return responses;
    }

    // GET - Trova tutti i memo vocali associati a una playlist
    public List<VocalMemoResponse> getAllVocalMemosByPlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));

        List<VocalMemoResponse> responses = new ArrayList<>();

        // Itera su tutti i VocalMemo associati alla Playlist
        for (VocalMemo vocalMemo : playlist.getVocalMemos()) {
            // Aggiungi il VocalMemo mappato alla lista di risposte
            responses.add(vocalMemoMapper.toVocalMemoResponse(vocalMemo));
        }

        return responses;
    }





    // Rimuovi un memo vocale da una playlist
    @Transactional
    public PlaylistResponse removeVocalMemoFromPlaylist(Long playlistId, Long memoId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));
        VocalMemo vocalMemo = vocalMemoRepository.findById(memoId)
                .orElseThrow(() -> new EntityNotFoundException("Memo vocale non trovato"));

        if (playlist.getVocalMemos() != null && playlist.getVocalMemos().contains(vocalMemo)) {
            playlist.getVocalMemos().remove(vocalMemo);
            playlistRepository.save(playlist);
        }

        return new PlaylistResponse(
                playlist.getId(),
                playlist.getNomePlaylist(),
                playlist.getYoutubeUrls(),
                playlist.getVocalMemos() != null
                        ? playlist.getVocalMemos().stream()
                        .map(vocalMemoMapper::toVocalMemoResponse)
                        .toList()
                        : new ArrayList<>()
        );
    }

    public List<VocalMemoResponse> findAllByUser(@AuthenticationPrincipal AppUser user) {
        return vocalMemoRepository.findAllByUser(user).stream()
                .map(vocalMemoMapper::toVocalMemoResponse)
                .collect(Collectors.toList());
    }
}
