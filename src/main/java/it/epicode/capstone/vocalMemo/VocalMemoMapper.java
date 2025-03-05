package it.epicode.capstone.vocalMemo;

import org.springframework.stereotype.Component;

//mapper per il dato che voglio in response, non voglio l'oggetto intero

@Component
public class VocalMemoMapper {
    public VocalMemoResponse toVocalMemoResponse(VocalMemo vocalMemo) {
        return new VocalMemoResponse(
                vocalMemo.getId(),
                vocalMemo.getNomeRegistrazione(),
                vocalMemo.getDataInserimento(),
                vocalMemo.getUser().getId(),
                vocalMemo.getPlaylist() != null ? vocalMemo.getPlaylist().getId() : null,
                vocalMemo.getRegistrazione()
        );
    }
}

