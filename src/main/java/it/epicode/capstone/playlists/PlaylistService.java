package it.epicode.capstone.playlists;

import it.epicode.capstone.authentication.AppUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository repository;

    //POST
public PlaylistResponse createPlaylist(@Valid PlaylistRequest request) {
    Playlist playlist = new Playlist();
    BeanUtils.copyProperties(request, playlist);
    repository.save(playlist);
    PlaylistResponse response = new PlaylistResponse();
    response.setId(playlist.getId());
    return response;
}
}


