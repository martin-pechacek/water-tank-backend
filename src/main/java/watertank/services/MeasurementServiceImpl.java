package watertank.services;


import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import watertank.dtos.MeasurementDTO;
import watertank.dtos.mappers.MeasurementMapper;
import watertank.models.Measurement;
import watertank.repositories.MeasurementRepository;
import watertank.utils.JSONUtil;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

@Service
public class MeasurementServiceImpl  implements MeasurementService{

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
    public Set<MeasurementDTO> findAllMeasurements() {
        List<Measurement> measurements = measurementRepository.findAll();

        return measurements.stream()
                .map(mapper::measurementToMeasurementDto)
                .collect(Collectors.toSet());
    }

    @Override
    public MeasurementDTO findById(Long id) {
        Optional<Measurement> measurementOpt = measurementRepository.findById(id);

        if(!measurementOpt.isPresent())
            throw new RuntimeException("Measurement with id " + id + " is not found");

        return mapper.measurementToMeasurementDto(measurementOpt.get());
    }

    @Override
    public Set<MeasurementDTO> findLatestXRecords(Long latestXRecords) {
        ArrayList<Measurement> measurements = new ArrayList<>(measurementRepository.findAll());

        Collections.reverse(measurements);

        return measurements.stream()
                .limit(latestXRecords)
                .map(mapper::measurementToMeasurementDto)
                .collect(Collectors.toSet());
    }
}
