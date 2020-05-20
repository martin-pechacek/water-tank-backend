package watertank.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "measurement_history")
public class Measurement {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "water_level_distance")
    private Integer waterLevelDistance;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("waterLevelDistance=").append(waterLevelDistance);
        sb.append('}');
        return sb.toString();
    }
}
