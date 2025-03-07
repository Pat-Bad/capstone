package it.epicode.capstone.vocalMemo;

import it.epicode.capstone.authentication.AppUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/vocalmemo")
@PreAuthorize("hasRole('ROLE_USER')")
public class VocalMemoController {
    private final VocalMemoService service;
    private final VocalMemoRepository vocalMemoRepository;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
@ResponseStatus(HttpStatus.CREATED)
    public VocalMemoResponse save(@Valid @RequestBody VocalMemoRequest request, @RequestParam Long playlistId,
                                  @AuthenticationPrincipal AppUser user)  {
return service.save(request, playlistId, user);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<VocalMemo> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public VocalMemo findById(@PathVariable Long id)  {
        return service.findById(id);
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

    //GET PER AUDIO
    @GetMapping("/{id}/registrazione")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public byte[] getRegistrazione(@PathVariable Long id)  {
        VocalMemo vocalMemo = vocalMemoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VocalMemo non trovato, ID " + id));
        return vocalMemo.getRegistrazione();

    }

    }
