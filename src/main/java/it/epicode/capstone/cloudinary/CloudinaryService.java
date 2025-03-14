package it.epicode.capstone.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "duwwcpahb",
                "api_key", "476383883172833",
                "api_secret", "nal9_XoRsCo7GMp3Wt1sUUMcYQI"));
    }

    public String uploadAudioToCloudinary(MultipartFile file) throws IOException {
        // Carica vocalmemo con playlist
        var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
        return (String) uploadResult.get("url");
    }

    public String uploadDiaryEntryToCloudinary(MultipartFile file) throws IOException {
        // Carica vocalmemo diario
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto", "folder", "diary"));
        return (String) uploadResult.get("secure_url");
    }
}
