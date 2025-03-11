package it.epicode.capstone.vocalMemo;

import it.epicode.capstone.authentication.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VocalMemoRepository extends JpaRepository<VocalMemo, Long> {
    List<VocalMemo> findByUser(AppUser user);


    VocalMemo findByPlaylistId(Long playlistId);
}
