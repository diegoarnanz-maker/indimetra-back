package indimetra.modelo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import indimetra.modelo.entity.base.BaseEntityCreated;

@Entity
@Table(name = "cortometrajes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cortometraje extends BaseEntityCreated {

    @Column(unique = true, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String technique;

    @Column(name = "release_year", nullable = false)
    private int releaseYear;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false, length = 50)
    private String language;

    @Column(precision = 3, scale = 1)
    private BigDecimal rating;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @PrePersist
    protected void onCreate() {
        if (this.language == null) {
            this.language = "Spanish";
        }
    }
}
