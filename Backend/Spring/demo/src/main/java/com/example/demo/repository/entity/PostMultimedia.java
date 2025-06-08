package com.example.demo.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts_multimedia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostMultimedia {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private PostMultimediaId id;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("multimediaId")
    @JoinColumn(name = "multimedia_id")
    private Multimedia multimedia;
}
