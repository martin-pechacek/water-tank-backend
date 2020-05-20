package watertank.services;

import watertank.dtos.MeasurementDTO;

import java.util.Set;

public interface MeasurementService {
    MeasurementDTO saveMeasurement(MeasurementDTO measurementDTO);

    Set<MeasurementDTO> findAllMeasurements();

    MeasurementDTO findById(Long id);

    Set<MeasurementDTO> findLatestXRecords(Long latestXRecords);
}
