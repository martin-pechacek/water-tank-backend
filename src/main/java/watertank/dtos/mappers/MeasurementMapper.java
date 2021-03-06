package watertank.dtos.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import watertank.dtos.MeasurementDTO;
import watertank.enums.Distance;
import watertank.models.Measurement;

@Mapper(componentModel = "spring")
public interface MeasurementMapper {

    MeasurementMapper INSTANCE = Mappers.getMapper(MeasurementMapper.class);

    Measurement measurementDtoToMeasurement(MeasurementDTO measurementDTO);

    MeasurementDTO measurementToMeasurementDto(Measurement measurement);

    @AfterMapping
    default void calculateTankFullness(Measurement measurement, @MappingTarget MeasurementDTO dto) {
        double tankFullness = 100 - (double) measurement.getWaterLevelDistance() / (double) Distance.SPILLWAY.getDistance() * 100;
        tankFullness = tankFullness > 100 ? 100 : tankFullness;
        dto.setTankFullness((int)Math.round(tankFullness));
    }

    @AfterMapping
    default void calculateRealWaterDistance(MeasurementDTO measurementDTO, @MappingTarget Measurement measurement){
        int measuredWaterLevelDistance = measurementDTO.getWaterLevelDistance();

        measurement.setWaterLevelDistance(measuredWaterLevelDistance - Distance.maxWaterLevel());
    }
}
