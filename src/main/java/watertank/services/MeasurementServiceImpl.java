package watertank.services;


import org.springframework.stereotype.Service;
import watertank.dtos.MeasurementDTO;
import watertank.dtos.mappers.MeasurementMapper;
import watertank.models.Measurement;
import watertank.repositories.MeasurementRepository;
import watertank.exceptions.MeasurementException;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeasurementServiceImpl  implements MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final MeasurementMapper mapper;

    public MeasurementServiceImpl(MeasurementRepository measurementRepository, MeasurementMapper mapper) {
        this.measurementRepository = measurementRepository;
        this.mapper = mapper;
    }

    @Override
    public MeasurementDTO saveMeasurement(MeasurementDTO measurementDTO) {
        Measurement measurement = measurementRepository.save(mapper.measurementDtoToMeasurement(measurementDTO));
        return mapper.measurementToMeasurementDto(measurement);
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
    public List<MeasurementDTO> findLatestXRecords(Long latestXRecords) {
        ArrayList<Measurement> measurements = new ArrayList<>(measurementRepository.findAll());

        Collections.reverse(measurements);

        return measurements.stream()
                .limit(latestXRecords)
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
}
