package it.epicode.capstone.vocalMemo;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.authentication.AppUserRepository;
import it.epicode.capstone.playlists.Playlist;
import it.epicode.capstone.playlists.PlaylistRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class VocalMemoService {
    private final VocalMemoRepository vocalMemoRepository;
    private final AppUserRepository userRepository;
    private final PlaylistRepository playlistRepository;

//POST

    public VocalMemoResponse save(@Valid @RequestBody VocalMemoRequest request,
                                  @AuthenticationPrincipal AppUser user) {

        VocalMemo vocalMemo = new VocalMemo();
        vocalMemo.setUrl(request.getUrl());
        vocalMemo.setUser(user);
        Playlist playlist = playlistRepository.findById(request.getPlaylistId())
                .orElseThrow(() -> new EntityNotFoundException("Playlist non trovata, ID " + request.getPlaylistId()));
        vocalMemo.setPlaylist(playlist);
        vocalMemo.setNomeRegistrazione(LocalDateTime.now().toString());

        VocalMemo savedVocalMemo = vocalMemoRepository.save(vocalMemo);

        return new VocalMemoResponse(savedVocalMemo.getId(), savedVocalMemo.getNomeRegistrazione(), savedVocalMemo.getDataRegistrazione(),
                savedVocalMemo.getUser().getId(), savedVocalMemo.getPlaylist().getId(), savedVocalMemo.getUrl());
    }


    //GET
    public VocalMemo findById(Long id) {
        return vocalMemoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VocalMemo non trovato con id: " + id));
    }

    public Page<Playlist> findAllByUser(AppUser user, Pageable pageable) {
        return playlistRepository.findAllByUser(user, pageable);
    }

    //PUT
    public VocalMemo update(Long id, @Valid VocalMemoRequest request) {
        VocalMemo vocalMemo = findById(id);
        BeanUtils.copyProperties(request, vocalMemo);
        VocalMemo updatedVocalMemo = vocalMemoRepository.save(vocalMemo);
        return updatedVocalMemo;
    }

    //DELETE
    public void delete(Long id) {
        VocalMemo vocalMemo = findById(id);
        vocalMemoRepository.delete(vocalMemo);
    }

    public Page<VocalMemoResponse> findByUser(AppUser user, Pageable pageable) {
        return vocalMemoRepository.findByUser(user, pageable);
    }

    public VocalMemo findByPlaylistId(Long playlistId) {
        return vocalMemoRepository.findByPlaylistId(playlistId);
    }

    public VocalMemo save(VocalMemo vocalMemo) {
        return vocalMemoRepository.save(vocalMemo);
    }

    public VocalMemoResponse saveDiaryEntry(String audioUrl, AppUser user) {
        try {

            VocalMemo vocalMemo = new VocalMemo();
            vocalMemo.setUrl(audioUrl);
            vocalMemo.setUser(user);
            vocalMemo.setNomeRegistrazione(vocalMemo.getDataRegistrazione().toString());

            VocalMemo savedVocalMemo = vocalMemoRepository.save(vocalMemo);

            return new VocalMemoResponse(
                    savedVocalMemo.getId(),
                    savedVocalMemo.getNomeRegistrazione(),
                    savedVocalMemo.getDataRegistrazione(),
                    savedVocalMemo.getUser().getId(),
                    savedVocalMemo.getPlaylist() != null ? savedVocalMemo.getPlaylist().getId() : null,
                    savedVocalMemo.getUrl()
            );
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il salvataggio del memo vocale.", e);
        }
    }


        public Page<VocalMemoResponse> findAllDiaryEntries(AppUser user, Pageable pageable) {
            Page<VocalMemoResponse> allEntries = vocalMemoRepository.findByUser(user, pageable);

            // stream per filtrare e mappare in lista
            List<VocalMemoResponse> filteredEntries = allEntries.getContent().stream()
                    .filter(v -> v.getUrl().contains("/diary/"))
                    .collect(Collectors.toList());

            return new PageImpl<>(filteredEntries, pageable, filteredEntries.size());
        }
}





