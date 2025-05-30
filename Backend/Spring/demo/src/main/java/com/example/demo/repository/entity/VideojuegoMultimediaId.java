package com.example.demo.repository.entity;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class VideojuegoMultimediaId implements Serializable {
    private Long videojuegoId;
    private Long multimediaId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof VideojuegoMultimediaId))
            return false;
        VideojuegoMultimediaId that = (VideojuegoMultimediaId) o;
        return Objects.equals(videojuegoId, that.videojuegoId) &&
                Objects.equals(multimediaId, that.multimediaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videojuegoId, multimediaId);
    }
}
