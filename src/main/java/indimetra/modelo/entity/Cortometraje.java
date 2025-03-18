package indimetra.modelo.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cortometrajes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cortometraje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
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
    private Double rating;

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

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.language == null) {
            this.language = "Spanish";
        }
        this.createdAt = new Date();
    }
}
