package watertank.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Null;
import org.hibernate.validator.constraints.Range;

import java.util.Comparator;
import java.util.Date;

@Getter
@Setter
public class MeasurementDTO {

    @Null(message = "id must not be sent")
    private Long id;

    @Max(value = 260, message = "waterLevelDistance must be lower than 260")
    @Range(min = 0, message= "waterLevelDistance may not be empty or null")
    @NotNull(message="Missing waterLevelDistance")
    private Integer waterLevelDistance;

    @Null(message = "createdAt must not be sent")
    private Date createdAt;

    @Null(message = "tankFullness must not be sent")
    private Integer tankFullness;

    public static Comparator<MeasurementDTO> compareByTankFulness = new Comparator<MeasurementDTO>() {
        public int compare(MeasurementDTO m1, MeasurementDTO m2) {
            return m1.getTankFullness().compareTo(m2.getTankFullness());
        }
    };
}
