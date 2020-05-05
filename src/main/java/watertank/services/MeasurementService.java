package watertank.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import watertank.dtos.MeasurementDTO;
import watertank.models.Measurement;

import java.util.List;
import java.util.Set;

public interface MeasurementService {
    MeasurementDTO saveMeasurement(MeasurementDTO measurementDTO);

    Set<MeasurementDTO> findAllMeasurements();

    MeasurementDTO findById(Long id);

    Set<MeasurementDTO> findLatestXRecords(Long latestXRecords);
}
