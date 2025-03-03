package it.epicode.capstone.vocalMemo;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.playlists.Playlist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    @Lob //consigliato dal prof. BYTEA ottimale per postgres, per salvare dati binari(multimedia file)
    @Column(columnDefinition = "BYTEA")
    private byte[] registrazione;

    // Dato binario per la registrazione vocale
@CreationTimestamp
    private LocalDate dataInserimento;
    private String nomeRegistrazione;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = true)
    private Playlist playlist;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    public byte[] getRegistrazione() {
        return registrazione;
    }
}
