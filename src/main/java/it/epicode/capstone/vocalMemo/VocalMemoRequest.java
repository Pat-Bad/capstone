package it.epicode.capstone.vocalMemo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VocalMemoRequest {
    @NotNull(message="Ricordati di inserire la registrazione")
    private byte[] registrazione;

    @NotBlank(message="Dai un titolo alla registrazione")
    private String nomeRegistrazione;

    private LocalDate dataInserimento;
}
