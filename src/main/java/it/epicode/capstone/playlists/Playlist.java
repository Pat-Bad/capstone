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

    @ElementCollection
    @CollectionTable(name = "playlist_youtube_urls", joinColumns = @JoinColumn(name = "playlist_id"))
    @Column(name = "youtube_url")
    private List<String> youtubeUrls = new ArrayList<>();  // Lista di URL dei video YouTube creata grazie a
    // elementcollection

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VocalMemo> vocalMemos = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;
}