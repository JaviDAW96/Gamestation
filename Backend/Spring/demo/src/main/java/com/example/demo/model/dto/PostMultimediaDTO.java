package com.example.demo.model.dto;
import com.example.demo.repository.entity.PostMultimedia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostMultimediaDTO {
    private Long postId;
    private Long multimediaId;
    private MultimediaDTO multimedia;
    private String rol;

    public static PostMultimediaDTO fromEntity(PostMultimedia pm) {
        return PostMultimediaDTO.builder()
            .postId(pm.getPost().getId())
            .multimediaId(pm.getMultimedia().getId())
            .multimedia(MultimediaDTO.convertToDTO(pm.getMultimedia()))
            .rol(pm.getRol()) 
            .build();
    }
}
