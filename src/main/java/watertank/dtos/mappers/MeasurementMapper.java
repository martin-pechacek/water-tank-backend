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
}
