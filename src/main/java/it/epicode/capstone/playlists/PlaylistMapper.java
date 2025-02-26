package it.epicode.capstone.playlists;

import it.epicode.capstone.vocalMemo.VocalMemo;
import org.springframework.stereotype.Component;

@Component
public class PlaylistMapper {
    public PlaylistResponse toPlaylistResponse(Playlist playlist) {
        return new PlaylistResponse(
                playlist.getId(),
                playlist.getNomePlaylist(),
                playlist.getYoutubeUrls(),
                playlist.getVocalMemo().stream().map(VocalMemo::getId).toList()
        );
    }
}