package watertank.dtos.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import watertank.dtos.MeasurementDTO;
import watertank.enums.Distance;
import watertank.models.Measurement;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MeasurementMapperTest {

    MeasurementMapper measurementMapper;

    @BeforeEach
    void setUp() {
        measurementMapper = MeasurementMapper.INSTANCE;
    }

    @Test
    void measurementDTOtoMeasurement() {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setId(1L);
        measurementDTO.setWaterLevelDistance(123);
        measurementDTO.setCreatedAt(new Date());

        Measurement measurement = measurementMapper.measurementDtoToMeasurement(measurementDTO);

        assertEquals(measurement.getId(), measurementDTO.getId());
        assertEquals(measurement.getWaterLevelDistance(), measurementDTO.getWaterLevelDistance() - Distance.maxWaterLevel());
        assertEquals(measurement.getCreatedAt(), measurementDTO.getCreatedAt());
    }

    @Test
    void measurementToMeasurementDTO() {
        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setWaterLevelDistance(13);
        measurement.setCreatedAt(new Date());

        MeasurementDTO measurementDTO = measurementMapper.measurementToMeasurementDto(measurement);

        assertEquals(measurement.getId(), measurementDTO.getId());
        assertEquals(measurement.getWaterLevelDistance(), measurementDTO.getWaterLevelDistance());
        assertEquals(measurement.getCreatedAt(), measurementDTO.getCreatedAt());
        assertEquals(95, measurementDTO.getTankFullness());
    }
}