package it.epicode.capstone.cloudinary;

import com.cloudinary.Cloudinary;
import it.epicode.capstone.authentication.AppUser;
import it.epicode.capstone.vocalMemo.VocalMemo;
import it.epicode.capstone.vocalMemo.VocalMemoResponse;
import it.epicode.capstone.vocalMemo.VocalMemoService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cloudinary")
public class CloudinaryController {
    private final Cloudinary cloudinary;
    private final VocalMemoService vocalMemoService;

    @PostMapping(path = "/upload-audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadToCloudinary(@RequestParam("file") MultipartFile file) {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(),
                    Cloudinary.asMap(
                            "resource_type", "auto", //tipo di file auto per audio
                            "folder", "registrazioni"));

            String url = result.get("secure_url").toString();
            System.out.println(url);
            return ResponseEntity.ok(url); // ritorna l'url (essenziale per me)

        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel caricamento del file: " + e.getMessage());
        }
    }

    @PostMapping(path = "/upload-diary", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> saveDiaryEntry(@RequestParam("file") MultipartFile file) {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(),
                    Cloudinary.asMap(
                            "resource_type", "auto",
                            "folder", "diary")); //folder diverso!

        String url = result.get("secure_url").toString();
        System.out.println(url);
        return ResponseEntity.ok(url);

    }
        catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel caricamento del file: " + e.getMessage());
    }}}