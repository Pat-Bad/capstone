package it.epicode.capstone.vocalMemo;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.playlists.Playlist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "vocal_memo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VocalMemo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob  //LARGE OBJECT
    private byte[] registrazione;  // Dato binario per la registrazione vocale

    private LocalDate dataInserimento;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = true)
    private Playlist playlist;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;
}
