package com.example.demo.repository.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideojuegoIdiomaId implements java.io.Serializable {
    private Long videojuegoId;
    private Long idiomaId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof VideojuegoIdiomaId))
            return false;
        VideojuegoIdiomaId that = (VideojuegoIdiomaId) o;
        return java.util.Objects.equals(videojuegoId, that.videojuegoId) &&
                java.util.Objects.equals(idiomaId, that.idiomaId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(videojuegoId, idiomaId);
    }
}