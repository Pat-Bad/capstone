package it.epicode.capstone.authentication;

import it.epicode.capstone.playlists.Playlist;
import it.epicode.capstone.playlists.PlaylistResponse;
import it.epicode.capstone.vocalMemo.VocalMemo;
import it.epicode.capstone.vocalMemo.VocalMemoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
   private Long id;  // Aggiungi l'id
    private String username;
   private  String email;





}
