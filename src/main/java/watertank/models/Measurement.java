package watertank.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Table(name = "measurement_history")
public class Measurement {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "water_level_distance")
    @NotNull(message = "waterLevelDistance may not be null")
    @PositiveOrZero(message = "waterLevelDistance should be positive or 0")
    @Max(400)
    private Integer waterLevelDistance;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date createdAt;

    public Measurement() {
    }

    public Long getId() {
        return id;
    }

    public int getWaterLevelDistance() {
        return waterLevelDistance;
    }

    public void setWaterLevelDistance(final int waterLevelDistance) {
        this.waterLevelDistance = waterLevelDistance;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("waterLevelDistance=").append(waterLevelDistance);
        sb.append('}');
        return sb.toString();
    }
}
