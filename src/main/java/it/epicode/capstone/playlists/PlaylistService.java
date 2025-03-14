package it.epicode.capstone.playlists;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.authentication.AppUserRepository;
import it.epicode.capstone.vocalMemo.VocalMemo;
import it.epicode.capstone.vocalMemo.VocalMemoRepository;
import it.epicode.capstone.youtube.AddVideoToPlaylistRequest;
import it.epicode.capstone.youtube.ModifyVideoRequest;
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
        playlist.setVocalMemo(request.getVocalMemo());
        playlist = repository.save(playlist);

        PlaylistResponse response = new PlaylistResponse();
        response.setId(playlist.getId());
        response.setNomePlaylist(playlist.getNomePlaylist());
        response.setYoutubeUrls(playlist.getYoutubeUrls());
        response.setUrl(playlist.getVocalMemo().getUrl());
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
    public void delete(Long playlistId) {
        Playlist playlist = repository.findById(playlistId).get();
        if (playlist.getVocalMemo() != null) {
            vocalMemoRepository.delete(playlist.getVocalMemo());
        }
        repository.delete(playlist);
    }
      /////////////////////////////////////////////////////////////////////////////////////////////

    public List<PlaylistResponse> findAllByUser(AppUser user) {
        List<Playlist> playlists = repository.findAllByUser(user);
        List<PlaylistResponse> playlistResponses = new ArrayList<>();
        for (Playlist playlist : playlists) {
            PlaylistResponse playlistResponse = new PlaylistResponse();
            playlistResponse.setId(playlist.getId());
            playlistResponse.setNomePlaylist(playlist.getNomePlaylist());
            playlistResponse.setYoutubeUrls(playlist.getYoutubeUrls());
            if (playlist.getVocalMemo() != null) {
                playlistResponse.setUrl(playlist.getVocalMemo().getUrl());
            }
            playlistResponses.add(playlistResponse);
        }
        return playlistResponses;
    }

    public PlaylistResponse findById(Long id) {
        Playlist playlist = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata, ID " + id));
        VocalMemo vocalMemo = vocalMemoRepository.findByPlaylistId(id);
        PlaylistResponse response = new PlaylistResponse();
        response.setId(playlist.getId());
        response.setNomePlaylist(playlist.getNomePlaylist());
        response.setYoutubeUrls(playlist.getYoutubeUrls());

        if (vocalMemo != null) {
            response.setUrl(vocalMemo.getUrl());
        } else {response.setUrl(null);}
        return response;
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

    public Playlist save(Playlist playlist) {
        return repository.save(playlist);
    }

    public PlaylistResponse modifyVideoInPlaylist(@Valid ModifyVideoRequest request) {
        Playlist playlist = repository.findById(request.getPlaylistId())
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata, ID " + request.getPlaylistId()));

        if ("add".equalsIgnoreCase(request.getAction())) {
            if (playlist.getYoutubeUrls() == null) {
                playlist.setYoutubeUrls(new ArrayList<>());
            }
            playlist.getYoutubeUrls().add(request.getYoutubeUrl());
        } else if ("remove".equalsIgnoreCase(request.getAction())) {
            if (playlist.getYoutubeUrls() != null && playlist.getYoutubeUrls().contains(request.getYoutubeUrl())) {
                playlist.getYoutubeUrls().remove(request.getYoutubeUrl());
            } else {
                throw new EntityNotFoundException("Il video non è presente nella playlist.");
            }
        } else {
            throw new IllegalArgumentException("Azione non valida, deve essere 'add' o 'remove'.");
        }
        repository.save(playlist);

        PlaylistResponse response = new PlaylistResponse();
        response.setId(playlist.getId());
        response.setNomePlaylist(playlist.getNomePlaylist());
        response.setYoutubeUrls(playlist.getYoutubeUrls());

        return response;
    }




}