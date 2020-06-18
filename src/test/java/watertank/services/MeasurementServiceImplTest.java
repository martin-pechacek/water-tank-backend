package watertank.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import watertank.dtos.MeasurementDTO;
import watertank.dtos.mappers.MeasurementMapper;
import watertank.models.Measurement;
import watertank.repositories.MeasurementRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MeasurementServiceImplTest {

    MeasurementService measurementService;

    MeasurementMapper mapper;

    @Mock
    MeasurementRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        mapper = MeasurementMapper.INSTANCE;

        measurementService = new MeasurementServiceImpl(repository, mapper);
    }

    @Test
    void saveMeasurement() {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setWaterLevelDistance(123);

        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setWaterLevelDistance(measurementDTO.getWaterLevelDistance());
        measurement.setCreatedAt(new Date());

        when(repository.save(any(Measurement.class))).thenReturn(measurement);

        MeasurementDTO returnedDTO = measurementService.saveMeasurement(measurementDTO);

        assertEquals(returnedDTO.getWaterLevelDistance(), measurementDTO.getWaterLevelDistance());
        assertNotNull(returnedDTO.getId());
        assertNotNull(returnedDTO.getCreatedAt());
        assertNotNull(returnedDTO.getTankFullness());
        verify(repository, times(1)).save(any(Measurement.class));
    }

    @Test
    void findAllMeasurements() {
        Measurement measurement1 = new Measurement();
        measurement1.setId(1L);
        measurement1.setWaterLevelDistance(20);

        Measurement measurement2 = new Measurement();
        measurement2.setId(1L);
        measurement2.setWaterLevelDistance(20);

        Measurement measurement3 = new Measurement();
        measurement3.setId(1L);
        measurement3.setWaterLevelDistance(20);

        List<Measurement> measurements = List.of(measurement1, measurement2, measurement3);

        when(repository.findAll()).thenReturn(measurements);

        List<MeasurementDTO> returnedMeasurements = measurementService.findAllMeasurements();

        assertEquals(returnedMeasurements.size(),3);
        verify(repository, times(1)).findAll();
    }

    @Test
    void findById() {
        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setWaterLevelDistance(50);

        when(repository.findById(anyLong())).thenReturn(Optional.of(measurement));

        MeasurementDTO returnedDTO = measurementService.findById(1L);

        assertEquals(returnedDTO.getId(), measurement.getId());
        assertEquals(returnedDTO.getWaterLevelDistance(), measurement.getWaterLevelDistance());
        assertNotNull(returnedDTO.getTankFullness());

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void findLatestXRecords() {
        Measurement measurement1 = new Measurement();
        measurement1.setId(1L);
        measurement1.setWaterLevelDistance(20);

        Measurement measurement2 = new Measurement();
        measurement2.setId(2L);
        measurement2.setWaterLevelDistance(30);

        Measurement measurement3 = new Measurement();
        measurement3.setId(3L);
        measurement3.setWaterLevelDistance(40);

        List<Measurement> measurements = List.of(measurement1, measurement2, measurement3);

        when(repository.findAll()).thenReturn(measurements);

        List<MeasurementDTO> returnedMeasurements = measurementService.findLatestXRecords(2L);

        assertEquals(2, returnedMeasurements.size());
    }

    @Test
    void getDailyMedians() {
        Date dayBeforeYesterday = Date.from(LocalDate.now().minusDays(2).atStartOfDay().toInstant(ZoneOffset.UTC));
        Date yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC));
        Date today = Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));

        Measurement measurement1 = new Measurement();
        measurement1.setId(1L);
        measurement1.setCreatedAt(dayBeforeYesterday);
        measurement1.setWaterLevelDistance(20);

        Measurement measurement2 = new Measurement();
        measurement2.setId(2L);
        measurement2.setCreatedAt(dayBeforeYesterday);
        measurement2.setWaterLevelDistance(100);

        Measurement measurement3 = new Measurement();
        measurement3.setId(3L);
        measurement3.setCreatedAt(dayBeforeYesterday);
        measurement3.setWaterLevelDistance(200);

        Measurement measurement4 = new Measurement();
        measurement4.setId(4L);
        measurement4.setCreatedAt(yesterday);
        measurement4.setWaterLevelDistance(140);

        Measurement measurement5 = new Measurement();
        measurement5.setId(5L);
        measurement5.setCreatedAt(yesterday);
        measurement5.setWaterLevelDistance(20);

        Measurement measurement6 = new Measurement();
        measurement6.setId(6L);
        measurement6.setCreatedAt(yesterday);
        measurement6.setWaterLevelDistance(40);

        Measurement measurement7 = new Measurement();
        measurement7.setId(7L);
        measurement7.setCreatedAt(today);
        measurement7.setWaterLevelDistance(140);

        Measurement measurement8 = new Measurement();
        measurement8.setId(8L);
        measurement8.setCreatedAt(today);
        measurement8.setWaterLevelDistance(20);

        Measurement measurement9 = new Measurement();
        measurement9.setId(9L);
        measurement9.setCreatedAt(today);
        measurement9.setWaterLevelDistance(40);

        List<Measurement> measurements = List.of(
                measurement1,
                measurement2,
                measurement3,
                measurement4,
                measurement5,
                measurement6,
                measurement7,
                measurement8,
                measurement9);

        when(repository.findAll()).thenReturn(measurements);

        List<MeasurementDTO> returnedMeasurements = measurementService.getDailyMedians();

        assertEquals(2, returnedMeasurements.size());
        assertEquals(2, returnedMeasurements.get(0).getId());
        assertEquals(6, returnedMeasurements.get(1).getId());
        verify(repository,times(1)).findAll();
    }
}