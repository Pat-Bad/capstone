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

    private String url;
@CreationTimestamp
private LocalDate dataRegistrazione = LocalDate.now();

    private String nomeRegistrazione;

    @OneToOne
    @JoinColumn(name = "playlist_id", nullable = true)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
}
