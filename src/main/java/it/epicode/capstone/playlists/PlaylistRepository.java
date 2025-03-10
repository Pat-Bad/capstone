package it.epicode.capstone.playlists;
import it.epicode.capstone.authentication.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> { @Query("SELECT p FROM Playlist p WHERE p.user = :user")
List<Playlist> findAllByUser(@Param("user") AppUser user);
}

