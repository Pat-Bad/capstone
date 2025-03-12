package it.epicode.capstone.playlists;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    private List<String> youtubeUrls = new ArrayList<>();

    @OneToOne
    @JoinColumn (name = "vocal_memo_id", nullable = true)
    private VocalMemo vocalMemo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private AppUser user;
}