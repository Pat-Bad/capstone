package it.epicode.capstone.youtube;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyVideoRequest {
    private Long playlistId;
    private String youtubeUrl;
    private String action;
}
