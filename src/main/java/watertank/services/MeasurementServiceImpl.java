package watertank.services;


import org.springframework.stereotype.Service;
import watertank.dtos.MeasurementDTO;
import watertank.dtos.mappers.MeasurementMapper;
import watertank.enums.Distance;
import watertank.models.Measurement;
import watertank.repositories.MeasurementRepository;
import watertank.exceptions.MeasurementException;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class MeasurementServiceImpl  implements MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final MeasurementMapper mapper;

    public MeasurementServiceImpl(MeasurementRepository measurementRepository, MeasurementMapper mapper) {
        this.measurementRepository = measurementRepository;
        this.mapper = mapper;
    }

    @Override
    public MeasurementDTO saveMeasurement(final MeasurementDTO measurementDTO, final String deviceId) {
        measurementDTO.setTankFullness(calculateTankFullness(measurementDTO.getWaterLevelDistance()));
        Measurement measurement = mapper.measurementDtoToMeasurement(measurementDTO);
        measurement.setDevice(deviceId);
        return mapper.measurementToMeasurementDto(measurementRepository.save(measurement));
    }

    @Override
    public List<MeasurementDTO> findAllMeasurements() {
        List<Measurement> measurements = measurementRepository.findAll();

        return measurements.stream()
                .map(mapper::measurementToMeasurementDto)
                .toList();

    }

    @Override
    public MeasurementDTO findById(Long id) {
        return measurementRepository.findById(id)
                .map(mapper::measurementToMeasurementDto)
                .orElseThrow(() ->  MeasurementException.NOT_FOUND);
    }

    @Override
    public List<MeasurementDTO> findLatestRecords(Long count) {
        ArrayList<Measurement> measurements = new ArrayList<>(measurementRepository.findAll());

        Collections.reverse(measurements);

        return measurements.stream()
                .limit(count)
                .map(mapper::measurementToMeasurementDto)
                .toList();
    }

    @Override
    public List<MeasurementDTO> getDailyMedians() {
        List<MeasurementDTO> subMeasurementList = new ArrayList<>();
        List<MeasurementDTO> dailyMedians = new ArrayList<>();

        List<MeasurementDTO> measurements = new ArrayList<>(measurementRepository.findAll())
                .stream()
                .map(mapper::measurementToMeasurementDto)
                .toList();

        measurements.forEach(measurement -> {
            LocalDate measurementDate = measurement.getCreatedAt().toInstant().atOffset(ZoneOffset.UTC).toLocalDate();
            LocalDate firstDateInSubset = !subMeasurementList.isEmpty()
                    ? subMeasurementList.get(0).getCreatedAt().toInstant().atOffset(ZoneOffset.UTC).toLocalDate()
                    : measurementDate; // first measurement in day

            // Have all measurements from one day
            if(measurementDate.compareTo(firstDateInSubset) != 0) {
                Collections.sort(subMeasurementList, MeasurementDTO.compareByTankFulness);

                // approximately median - exact median is not necessary
                dailyMedians.add(subMeasurementList.get(subMeasurementList.size()/2));

                subMeasurementList.clear();
            }

            subMeasurementList.add(measurement);
        });

        return dailyMedians;
    }

    private Integer calculateTankFullness(Integer waterLevelDistance) {
        double tankFullness = 100 - (double)calculateWaterDepth(waterLevelDistance) / (double) Distance.SPILLWAY.getDistance() * 100;
        tankFullness = tankFullness > 100 ? 100 : tankFullness;
        return Math.toIntExact(Math.round(tankFullness));
    }

    // Since we get the distance from the sensor to the water, we have to convert it
    // to the distance from the bottom of the tank to the water surface.
    private Integer calculateWaterDepth(Integer waterLevelDistance){
        return waterLevelDistance - Distance.maxWaterLevel();
    }
}
