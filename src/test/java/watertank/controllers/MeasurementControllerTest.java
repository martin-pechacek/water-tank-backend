package watertank.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import watertank.dtos.MeasurementDTO;
import watertank.services.MeasurementService;

import java.util.Date;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static watertank.controllers.AbstractController.jsonAsString;

class MeasurementControllerTest {

    @Mock
    MeasurementService measurementService;

    @InjectMocks
    MeasurementController controller;

    MockMvc mockMvc;

    private static HttpHeaders headers;

    private static final String DEVICE_ID_HEADER = "Device-ID";
    private static final String CORRECT_DEVICE_ID = "test-device";

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.add(DEVICE_ID_HEADER, CORRECT_DEVICE_ID);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void addMeasurement() throws Exception {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setWaterLevelDistance(24);

        MeasurementDTO returnedDTO = new MeasurementDTO();
        returnedDTO.setId(1L);
        returnedDTO.setWaterLevelDistance(13);
        returnedDTO.setTankFullness(95);

        when(measurementService.saveMeasurement(any(MeasurementDTO.class))).thenReturn(returnedDTO);

        mockMvc.perform(
                post(MeasurementController.BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonAsString(measurementDTO))
                    .headers(headers))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",equalTo(1)))
                .andExpect(jsonPath("$.waterLevelDistance",equalTo(13)))
                .andExpect(jsonPath("$.tankFullness",equalTo(95)));

        verify(measurementService, times(1)).saveMeasurement(any(MeasurementDTO.class));
    }

    @Test
    void addMeasurementMissingWaterLevel() throws Exception {
        MeasurementDTO measurementDTO = new MeasurementDTO();

        mockMvc.perform(post(MeasurementController.BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonAsString(measurementDTO))
                    .headers(headers))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addMeasurementWaterLevelBelow0() throws Exception {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setWaterLevelDistance(-1);

        mockMvc.perform(post(MeasurementController.BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonAsString(measurementDTO))
                .headers(headers))
            .andExpect(status().isBadRequest());
    }

    @Test
    void addMeasurementWaterLevelAbove260() throws Exception {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setWaterLevelDistance(261);

        mockMvc.perform(post(MeasurementController.BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonAsString(measurementDTO))
                .headers(headers))
            .andExpect(status().isBadRequest());
    }

    @Test
    void addMesurementWithFilledNullAttributes() throws Exception {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setId(1L);
        measurementDTO.setWaterLevelDistance(100);
        measurementDTO.setTankFullness(50);
        measurementDTO.setCreatedAt(new Date());

        mockMvc.perform(post(MeasurementController.BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonAsString(measurementDTO))
                .header("Device-ID", "test-device"))
            .andExpect(status().isBadRequest());
    }


    @Test
    void getAllMeasurements() throws Exception {
        MeasurementDTO measurement = new MeasurementDTO();
        measurement.setId(1L);

        MeasurementDTO measurement2 = new MeasurementDTO();
        measurement2.setId(2L);

        MeasurementDTO measurement3 = new MeasurementDTO();
        measurement3.setId(3L);

        Set<MeasurementDTO> measurements = Set.of(measurement, measurement2, measurement3);

        when(measurementService.findAllMeasurements()).thenReturn(measurements);

        mockMvc.perform(
                get(MeasurementController.BASE_URI)
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)));

        verify(measurementService, times(1)).findAllMeasurements();
    }

    @Test
    void getLimitedMeasurements() throws Exception {
        MeasurementDTO measurement = new MeasurementDTO();
        measurement.setId(1L);

        Set<MeasurementDTO> measurements = Set.of(measurement);

        when(measurementService.findLatestXRecords(1L)).thenReturn(measurements);

        mockMvc.perform(
                get(MeasurementController.BASE_URI)
                        .headers(headers)
                        .param("last","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)));

        verify(measurementService, times(1)).findLatestXRecords(anyLong());
    }

    @Test
    void getMeasurementById() throws Exception {
        MeasurementDTO measurement = new MeasurementDTO();
        measurement.setId(1L);
        measurement.setWaterLevelDistance(55);
        measurement.setTankFullness(83);

        when(measurementService.findById(anyLong())).thenReturn(measurement);

        mockMvc.perform(
                get(MeasurementController.BASE_URI + "/1")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.waterLevelDistance", equalTo(55)))
                .andExpect(jsonPath("$.tankFullness", equalTo(83)));

        verify(measurementService, times(1)).findById(anyLong());
    }
}
