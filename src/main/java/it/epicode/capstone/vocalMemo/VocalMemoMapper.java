package it.epicode.capstone.vocalMemo;

import org.springframework.stereotype.Component;

@Component
public class VocalMemoMapper {
    public VocalMemoResponse toVocalMemoResponse(VocalMemo vocalMemo, String audioUrl) {
        return new VocalMemoResponse(
                vocalMemo.getId(),
                vocalMemo.getNomeRegistrazione(),
                vocalMemo.getDataInserimento(),
                vocalMemo.getUser().getId(),
                vocalMemo.getPlaylist() != null ? vocalMemo.getPlaylist().getId() : null,
                audioUrl
        );
    }
}

