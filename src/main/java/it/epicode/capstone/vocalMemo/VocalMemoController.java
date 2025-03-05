package it.epicode.capstone.vocalMemo;

import it.epicode.capstone.authentication.AppUser;
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
@RequestMapping("api/memo-vocali")
@PreAuthorize("hasRole('ROLE_USER')")
public class VocalMemoController {
    private final VocalMemoService service;

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<VocalMemoResponse> findAllByUser(@AuthenticationPrincipal AppUser user) {
        return service.findAllByUser(user);
    }

    /*@GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public VocalMemoResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }*/

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VocalMemoResponse> save(@RequestParam("audio") MultipartFile audioFile,
                                                  @RequestParam("nomeRegistrazione") String nomeRegistrazione,
                                                  @RequestParam("playlistId") Long playlistId,
                                                  @AuthenticationPrincipal AppUser user) {
        try {
            // Creazione della richiesta con i dati del file e della registrazione
            VocalMemoRequest request = new VocalMemoRequest();
            request.setRegistrazione(audioFile.getBytes()); // Converte il file audio in byte[]
            request.setNomeRegistrazione(nomeRegistrazione);

            // Salva la registrazione
            VocalMemoResponse response = service.save(request, playlistId, user);

            // Restituisce la risposta con lo stato HTTP 201 (CREATED)
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IOException e) {
            // Stampa lo stack trace completo in caso di errore
            e.printStackTrace();  // Qui viene stampato lo stack trace dell'errore
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public VocalMemoResponse update(@PathVariable Long id, @RequestBody @Valid VocalMemoRequest request) {
        return service.updateVocalMemo(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteVocalMemo(id);}


    @GetMapping("/{id}")  //Questa get restituisce proprio il vocal memo, non i dettagli e basta
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getVocalMemo(@PathVariable Long id) {
        byte[] audioData = service.getVocalMemoById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(audioData);
    }

    @PutMapping("/{vocalMemoId}/playlist/{playlistId}")
    @ResponseStatus(HttpStatus.OK)
    public VocalMemoResponse save(@RequestBody @Valid VocalMemoRequest request,
                                  @PathVariable Long vocalMemoId,
                                  @PathVariable Long playlistId,
                                  @AuthenticationPrincipal AppUser user) {
        return service.save(request, playlistId, user);
    }




}
