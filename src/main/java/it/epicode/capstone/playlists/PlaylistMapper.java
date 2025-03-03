package it.epicode.capstone.playlists;

import it.epicode.capstone.vocalMemo.VocalMemoMapper;
import it.epicode.capstone.vocalMemo.VocalMemoResponse;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
//mapper per il dato che voglio in response, non voglio l'oggetto intero
@Component
@RequiredArgsConstructor
public class PlaylistMapper {

    private final VocalMemoMapper vocalMemoMapper;

    public PlaylistResponse toPlaylistResponse(Playlist playlist) {
        List<VocalMemoResponse> vocalMemoResponses = playlist.getVocalMemo() != null
                ? playlist.getVocalMemo().stream()
                .map(memo -> vocalMemoMapper.toVocalMemoResponse(memo, "http://localhost:8080/api/memo-vocali/" + memo.getId() + "/audio"))
                .collect(Collectors.toList())
                : null;

        return new PlaylistResponse(
                playlist.getId(),
                playlist.getNomePlaylist(),
                playlist.getYoutubeUrls(),
                vocalMemoResponses
        );
    }

}

