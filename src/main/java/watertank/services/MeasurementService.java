package watertank.services;

import watertank.dtos.MeasurementDTO;

import java.util.List;

public interface MeasurementService {
    MeasurementDTO saveMeasurement(MeasurementDTO measurementDTO, String deviceId);

    List<MeasurementDTO> findAllMeasurements();

    MeasurementDTO findById(Long id);

    List<MeasurementDTO> findLatestRecords(Long latestXRecords);

    List<MeasurementDTO> getDailyMedians();
}
