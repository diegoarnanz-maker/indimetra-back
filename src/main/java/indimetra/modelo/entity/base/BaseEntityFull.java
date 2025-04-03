package indimetra.modelo.entity.base;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntityFull extends BaseEntity {

    @Column(name = "is_active", nullable = false)
    protected Boolean isActive = true;

    @Column(name = "is_deleted", nullable = false)
    protected Boolean isDeleted = false;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}