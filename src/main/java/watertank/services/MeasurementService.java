package watertank.services;

import watertank.dtos.MeasurementDTO;

import java.util.List;
import java.util.Set;

public interface MeasurementService {
    MeasurementDTO saveMeasurement(MeasurementDTO measurementDTO);

    List<MeasurementDTO> findAllMeasurements();

    MeasurementDTO findById(Long id);

    List<MeasurementDTO> findLatestXRecords(Long latestXRecords);
}
