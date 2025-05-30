package com.example.demo.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts_multimedia")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PostMultimedia {
    @EmbeddedId
    private PostMultimediaId id;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @MapsId("multimediaId")
    @JoinColumn(name = "multimedia_id")
    private Multimedia multimedia;
}

