package it.epicode.capstone.cloudinary;

import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cloudinary")
public class CloudinaryController {
    private final Cloudinary cloudinary;

    @PostMapping(path = "/upload-audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadToCloudinary(@RequestParam("file") MultipartFile file) {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(),
                    Cloudinary.asMap(
                            "resource_type", "auto",  // <-- Specifica il tipo di file
                            "folder", "registrazioni"
                    ));

            String url = result.get("secure_url").toString();
            return ResponseEntity.ok(url); // Ritorna l'URL dell'audio caricato

        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel caricamento del file: " + e.getMessage());
        }
    }
}