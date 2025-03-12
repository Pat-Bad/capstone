package it.epicode.capstone.vocalMemo;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.playlists.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VocalMemoRepository extends JpaRepository<VocalMemo, Long> {
    @Query("SELECT new it.epicode.capstone.vocalMemo.VocalMemoResponse(v.id, v.nomeRegistrazione, v" +
            ".dataRegistrazione, v.user.id, v.playlist.id, v.url) " +
            "FROM VocalMemo v WHERE v.user = :user")
    List<VocalMemoResponse> findByUser(@Param("user") AppUser user);

    VocalMemo findByPlaylistId(Long playlistId);
}
