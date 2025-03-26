package it.epicode.capstone.vocalMemo;

import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.cloudinary.CloudinaryService;
import it.epicode.capstone.playlists.PlaylistRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/vocalmemo")
@PreAuthorize("hasRole('ROLE_USER')")
public class VocalMemoController {
    private final VocalMemoService service;
    private final VocalMemoRepository vocalMemoRepository;
    private final PlaylistRepository playlistRepository;
    private final CloudinaryService cloudinaryService;


    @PostMapping("/upload-diary")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)

    public VocalMemoResponse saveDiaryEntry(@RequestParam("file")MultipartFile file,

                                            @AuthenticationPrincipal AppUser user) {
        try{
            String audioUrl = cloudinaryService.uploadDiaryEntryToCloudinary(file);
            VocalMemo vocalMemo = new VocalMemo();
            vocalMemo.setUrl(audioUrl);
            vocalMemo.setUser(user);

            vocalMemo.setDataRegistrazione(LocalDate.now());


            return service.saveDiaryEntry(audioUrl, user);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il salvataggio dell'audio.", e);
        }
    }

    @GetMapping("/diary-entries")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<VocalMemoResponse> findAllDiaryEntries(@AuthenticationPrincipal AppUser user) {
        return service.findAllDiaryEntries(user);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public VocalMemo update(@PathVariable Long id, @Valid @RequestBody VocalMemoRequest request)  {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)  {
        service.delete(id);
    }
}





