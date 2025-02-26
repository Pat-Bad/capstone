package it.epicode.capstone.playlists;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.vocalMemo.VocalMemo;
import it.epicode.capstone.vocalMemo.VocalMemoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
    private final PlaylistMapper playlistMapper;

//POST
public PlaylistResponse save(@Valid PlaylistRequest request) {
    Playlist playlist = new Playlist();
    BeanUtils.copyProperties(request, playlist);
    repository.save(playlist);
    return playlistMapper.toPlaylistResponse(playlist);
}

//PUT
public PlaylistResponse update(Long id, @Valid PlaylistRequest request) {
    Playlist playlist = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));
    BeanUtils.copyProperties(request,playlist);
    repository.save(playlist);
    return playlistMapper.toPlaylistResponse(playlist);
}

//DELETE
public void delete(Long id) {
    Playlist playlist = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));
    repository.delete(playlist);
    }


public PlaylistResponse findById(Long id) {
    Playlist playlist = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));
    return new PlaylistResponse(
                playlist.getId(),
                playlist.getNomePlaylist(),
                playlist.getYoutubeUrls(),
                playlist.getVocalMemo() != null ? playlist.getVocalMemo().stream().map(VocalMemo::getId).toList() : null);
    }

public List<PlaylistResponse> findAll() {
    return repository.findAll().stream()
            .map(playlist -> new PlaylistResponse(playlist.getId(),
                        playlist.getNomePlaylist(),
                        playlist.getYoutubeUrls(),
                playlist.getVocalMemo() != null ? playlist.getVocalMemo().stream().map(VocalMemo::getId).toList() : null))
                .toList();
    }

public PlaylistResponse addVocalMemoToPlaylist(Long playlistId, Long memoId) {
        Playlist playlist = repository.findById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));
        VocalMemo memo = vocalMemoRepository.findById(memoId)
                .orElseThrow(() -> new EntityNotFoundException("Memo vocale non trovato"));

        if (playlist.getVocalMemo() == null) {
            playlist.setVocalMemo(new ArrayList<>());
        }
        playlist.getVocalMemo().add(memo);
        repository.save(playlist);
        return playlistMapper.toPlaylistResponse(playlist);
    }

public PlaylistResponse removeVocalMemoFromPlaylist(Long playlistId, Long memoId) {
        Playlist playlist = repository.findById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));

        VocalMemo memo = vocalMemoRepository.findById(memoId)
                .orElseThrow(() -> new EntityNotFoundException("Memo vocale non trovato"));

        if (playlist.getVocalMemo() != null && playlist.getVocalMemo().contains(memo)) {
            playlist.getVocalMemo().remove(memo);
            repository.save(playlist);
        }
        return playlistMapper.toPlaylistResponse(playlist);
    }
}


