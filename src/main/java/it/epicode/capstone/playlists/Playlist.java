package it.epicode.capstone.playlists;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.vocalMemo.VocalMemo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomePlaylist;

    @ElementCollection //Relazione onetomany per tipi simple, non entità a sé. Crea la tabella con solo gli url
    @CollectionTable(name = "playlist_youtube_urls", joinColumns = @JoinColumn(name = "playlist_id"))
    @Column(name = "youtube_url")
    private List<String> youtubeUrls = new ArrayList<>(); //inizializzo per evitare nullpointer

    @OneToOne (fetch = FetchType.LAZY, mappedBy = "playlist")
    private VocalMemo vocalMemo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;
}