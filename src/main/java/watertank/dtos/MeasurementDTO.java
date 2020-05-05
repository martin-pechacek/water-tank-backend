package watertank.dtos;

import lombok.Getter;
import lombok.Setter;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import watertank.enums.Distance;
import watertank.models.Measurement;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

@Getter
@Setter
public class MeasurementDTO {

    @Null
    private Long id;

    @NotNull(message = "waterLevelDistance may not be null")
    @PositiveOrZero(message = "waterLevelDistance should be positive or 0")
    @Max(260)
    private Integer waterLevelDistance;

    @Null
    private Date createdAt;

    @Null
    private Integer tankFullness;
}
