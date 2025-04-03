package indimetra.modelo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import indimetra.modelo.entity.base.BaseEntity;

@Entity
@Table(name = "favorites", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "cortometraje_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "cortometraje_id", nullable = false)
    private Cortometraje cortometraje;

    @Column(name = "added_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedAt;

    @PrePersist
    protected void onCreate() {
        if (this.addedAt == null) {
            this.addedAt = new Date();
        }
    }
}
