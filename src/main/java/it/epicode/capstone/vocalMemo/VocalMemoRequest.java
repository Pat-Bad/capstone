package it.epicode.capstone.vocalMemo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VocalMemoRequest {

    @NotNull(message="Ricordati di inserire la registrazione")
    private String url;
    private Long playlistId;
    private Long userId;
}
