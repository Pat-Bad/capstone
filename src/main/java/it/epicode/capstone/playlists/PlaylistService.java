package it.epicode.capstone.playlists;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.authentication.AppUserRepository;
import it.epicode.capstone.vocalMemo.VocalMemo;
import it.epicode.capstone.vocalMemo.VocalMemoMapper;
import it.epicode.capstone.vocalMemo.VocalMemoRepository;
import it.epicode.capstone.vocalMemo.VocalMemoResponse;
import it.epicode.capstone.youtube.AddVideoToPlaylistRequest;
import it.epicode.capstone.youtube.RemoveVideoRequest;
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

@Service
@Validated
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository repository;
    private final VocalMemoRepository vocalMemoRepository;
    private final AppUserRepository userRepository;
    private final PlaylistMapper playlistMapper;
    private final VocalMemoMapper vocalMemoMapper;

    // POST
    public PlaylistResponse save(@Valid PlaylistRequest request, @AuthenticationPrincipal AppUser user) {
        Playlist playlist = new Playlist();
        playlist.setNomePlaylist(request.getNomePlaylist());  // Copia manualmente il nome
        playlist.setYoutubeUrls(request.getYoutubeUrls());  // Copia gli URL di YouTube
        playlist.setVocalMemos(new ArrayList<>() );  // Evita problemi di riferimento null

        AppUser persistedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato nel database!"));
        playlist.setUser(persistedUser);

        repository.save(playlist);
        return playlistMapper.toPlaylistResponse(playlist);
    }

    // PUT
    public PlaylistResponse update(Long id, @Valid PlaylistRequest request) {
        Playlist playlist = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));
        BeanUtils.copyProperties(request, playlist);
        repository.save(playlist);
        return playlistMapper.toPlaylistResponse(playlist);
    }

    // DELETE
    public void delete(Long id) {
        Playlist playlist = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));
        repository.delete(playlist);
    }

    // GET by ID
    public PlaylistResponse findById(Long id) {
        Playlist playlist = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));

        List<VocalMemoResponse> memoResponses = (playlist.getVocalMemos() != null)
                ? playlist.getVocalMemos().stream()
                .map(memo -> vocalMemoMapper.toVocalMemoResponse(memo))  // Passa solo vocalMemo
                .toList()
                : new ArrayList<>();

        return new PlaylistResponse(
                playlist.getId(),
                playlist.getNomePlaylist(),
                playlist.getYoutubeUrls(),
                memoResponses
        );
    }

    // GET all
    public List<PlaylistResponse> findAll() {
        return repository.findAll().stream()
                .map(playlist -> new PlaylistResponse(
                        playlist.getId(),
                        playlist.getNomePlaylist(),
                        playlist.getYoutubeUrls(),
                        playlist.getVocalMemos() != null
                                ? playlist.getVocalMemos().stream()
                                .map(memo -> vocalMemoMapper.toVocalMemoResponse(memo))  // Passa solo vocalMemo
                                .toList()
                                : new ArrayList<>()
                )).toList();
    }

    // Aggiungi memo vocale a playlist
    public PlaylistResponse addVocalMemoToPlaylist(Long playlistId, Long memoId) {
        Playlist playlist = repository.findById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));
        VocalMemo memo = vocalMemoRepository.findById(memoId)
                .orElseThrow(() -> new EntityNotFoundException("Memo vocale non trovato"));

        if (playlist.getVocalMemos() == null) {
            playlist.setVocalMemos(new ArrayList<>());
        }
        playlist.getVocalMemos().add(memo);
        repository.save(playlist);
        return playlistMapper.toPlaylistResponse(playlist);
    }

    // Rimuovi memo vocale da playlist
    public PlaylistResponse removeVocalMemoFromPlaylist(Long playlistId, Long memoId) {
        Playlist playlist = repository.findById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));

        VocalMemo memo = vocalMemoRepository.findById(memoId)
                .orElseThrow(() -> new EntityNotFoundException("Memo vocale non trovato"));

        if (playlist.getVocalMemos() != null && playlist.getVocalMemos().contains(memo)) {
            playlist.getVocalMemos().remove(memo);
            repository.save(playlist);
        }
        return playlistMapper.toPlaylistResponse(playlist);
    }

    // Aggiungi video a playlist
    public PlaylistResponse addVideoToPlaylist(AddVideoToPlaylistRequest request) {
        Playlist playlist = repository.findById(request.getPlaylistId())
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));

        playlist.getYoutubeUrls().add(request.getVideoUrl());
        repository.save(playlist);

        List<VocalMemoResponse> memoResponses = playlist.getVocalMemos().stream()
                .map(memo -> vocalMemoMapper.toVocalMemoResponse(memo))  // Passa solo vocalMemo
                .toList();

        return new PlaylistResponse(
                playlist.getId(),
                playlist.getNomePlaylist(),
                playlist.getYoutubeUrls(),
                memoResponses
        );
    }

    // Rimuovi video da playlist
    public PlaylistResponse removeVideoFromPlaylist(@Valid RemoveVideoRequest request) {
        Playlist playlist = repository.findById(request.getPlaylistId())
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));

        playlist.getYoutubeUrls().remove(request.getVideoUrl());
        repository.save(playlist);

        List<VocalMemoResponse> memoResponses = playlist.getVocalMemos().stream()
                .map(memo -> vocalMemoMapper.toVocalMemoResponse(memo))  // Passa solo vocalMemo
                .toList();

        return new PlaylistResponse(
                playlist.getId(),
                playlist.getNomePlaylist(),
                playlist.getYoutubeUrls(),
                memoResponses
        );
    }

    // Ottieni playlist con contenuto (memo vocali)
    @Transactional
    public PlaylistResponse getPlaylistWithContent(Long id) {
        Playlist playlist = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));

        List<VocalMemoResponse> memoResponses = playlist.getVocalMemos().stream()
                .map(memo -> vocalMemoMapper.toVocalMemoResponse(memo))  // Passa solo vocalMemo
                .toList();

        return new PlaylistResponse(
                playlist.getId(),
                playlist.getNomePlaylist(),
                playlist.getYoutubeUrls(),
                memoResponses
        );
    }

    // Ottieni tutte le playlist per un utente
    public List<PlaylistResponse> findAllByUser(@AuthenticationPrincipal AppUser user) {
        if (user == null) {
            throw new RuntimeException("Utente non autenticato!");
        }

        return repository.findAllByUser(user).stream()
                .map(playlist -> new PlaylistResponse(
                        playlist.getId(),
                        playlist.getNomePlaylist(),
                        playlist.getYoutubeUrls(),
                        playlist.getVocalMemos() != null
                                ? playlist.getVocalMemos().stream()
                                .map(memo -> vocalMemoMapper.toVocalMemoResponse(memo))  // Passa solo vocalMemo
                                .toList()
                                : new ArrayList<>()
                )).toList();
    }
}
