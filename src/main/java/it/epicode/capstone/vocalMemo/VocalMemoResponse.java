package it.epicode.capstone.vocalMemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VocalMemoResponse {

    private Long id;
    private String nomeRegistrazione;
    private LocalDate dataRegistrazione;
    private Long userId;
    private Long playlistId;
    private String url;
}
