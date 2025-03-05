package it.epicode.capstone.playlists;

import it.epicode.capstone.vocalMemo.VocalMemoMapper;
import it.epicode.capstone.vocalMemo.VocalMemoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlaylistMapper {

    private final VocalMemoMapper vocalMemoMapper;

    public PlaylistResponse toPlaylistResponse(Playlist playlist) {
        // Mappa i vocal memo della playlist in una lista di VocalMemoResponse
        List<VocalMemoResponse> vocalMemoResponses = playlist.getVocalMemos() != null
                ? playlist.getVocalMemos().stream()
                .map(memo -> vocalMemoMapper.toVocalMemoResponse(memo))  // Usa 'memo' qui, non 'vocalmemo'
                .collect(Collectors.toList())  // Chiudi correttamente il collect
                : new ArrayList<>();  // Cambia null con una lista vuota per evitare possibili NPE

        // Restituisci la PlaylistResponse con i dati corretti
        return new PlaylistResponse(
                playlist.getId(),
                playlist.getNomePlaylist(),
                playlist.getYoutubeUrls(),
                vocalMemoResponses
        );
    }
}
