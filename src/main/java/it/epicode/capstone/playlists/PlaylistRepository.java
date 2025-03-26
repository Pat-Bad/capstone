package it.epicode.capstone.playlists;
import it.epicode.capstone.authentication.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> { @Query("SELECT p FROM Playlist p WHERE p.user = :user")
Page<Playlist> findAllByUser(@Param("user") AppUser user, Pageable pageable);
}

