package it.epicode.capstone.playlists;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.authentication.AppUserRepository;
import it.epicode.capstone.vocalMemo.VocalMemoRepository;
import it.epicode.capstone.youtube.AddVideoToPlaylistRequest;
import it.epicode.capstone.youtube.RemoveVideoRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    private final AppUserRepository userRepository;

    //POST

    @Transactional
    public PlaylistResponse save(@Valid PlaylistRequest request, AppUser user) {
        Playlist playlist = new Playlist();
        playlist.setNomePlaylist(request.getNomePlaylist());
        playlist.setYoutubeUrls(request.getYoutubeUrls());
        playlist.setUser(user);
        playlist = repository.save(playlist);

        PlaylistResponse response = new PlaylistResponse();
        response.setId(playlist.getId());
        response.setNomePlaylist(playlist.getNomePlaylist());
        response.setYoutubeUrls(playlist.getYoutubeUrls());
        return response;
    }

    //PUT
    @Transactional
    public PlaylistResponse update(Long id, @Valid PlaylistRequest request) {
        Playlist playlist = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));
        BeanUtils.copyProperties(request, playlist);
        repository.save(playlist);

        PlaylistResponse response = new PlaylistResponse();
        response.setId(playlist.getId());
        response.setNomePlaylist(playlist.getNomePlaylist());
        response.setYoutubeUrls(playlist.getYoutubeUrls());

        return response;
    }

    //DELETE
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Playlist> findAll(AppUser user) {
        return repository.findAll();
    }

    public Playlist findById(Long id) {
        Playlist playlist = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata"));
        return playlist;
    }

    public PlaylistResponse addVideoToPlaylist(@Valid AddVideoToPlaylistRequest request) {
        Playlist playlist = repository.findById(request.getPlaylistId())
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata, ID " + request.getPlaylistId()));
        if (playlist.getYoutubeUrls() == null) {
            playlist.setYoutubeUrls(new ArrayList<>());
        }
        playlist.getYoutubeUrls().add(request.getYoutubeUrl());
        repository.save(playlist);

        PlaylistResponse response = new PlaylistResponse();
        response.setId(playlist.getId());
        response.setNomePlaylist(playlist.getNomePlaylist());
        response.setYoutubeUrls(playlist.getYoutubeUrls());

        return response;
    }

    public PlaylistResponse removeVideoFromPlaylist(@Valid RemoveVideoRequest request) {
        Playlist playlist = repository.findById(request.getPlaylistId())
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata, ID " + request.getPlaylistId()));
        playlist.getYoutubeUrls().remove(request.getYoutubeUrl());
        repository.save(playlist);

        PlaylistResponse response = new PlaylistResponse();
        response.setId(playlist.getId());
        response.setNomePlaylist(playlist.getNomePlaylist());
        response.setYoutubeUrls(playlist.getYoutubeUrls());

        return response;
    }
}