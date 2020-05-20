package watertank.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import watertank.dtos.MeasurementDTO;
import watertank.services.MeasurementService;
import watertank.exceptions.NotFoundException;

import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static watertank.controllers.AbstractController.jsonAsString;

class ExceptionHandlerControllerTest {

    @Mock
    MeasurementService measurementService;

    @InjectMocks
    MeasurementController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    void notFoundException() throws Exception {
        Long id = 1L;

        when(measurementService.findById(id)).thenThrow(NotFoundException.class);

        mockMvc.perform(get(MeasurementController.BASE_URI + "/" + id)
                        .header("Device-ID", "test-device"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.error", equalTo("Not Found")));
    }

    @Test
    void handleMethodArgumentNotValid() throws Exception {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setId(1L);
        measurementDTO.setWaterLevelDistance(100);
        measurementDTO.setTankFullness(50);
        measurementDTO.setCreatedAt(new Date());

        mockMvc.perform(post(MeasurementController.BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonAsString(measurementDTO))
                    .header("Device-ID", "test-device"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.messages[0]", equalTo("tankFullness must not be sent")))
                .andExpect(jsonPath("$.messages[1]", equalTo("id must not be sent")))
                .andExpect(jsonPath("$.messages[2]", equalTo("createdAt must not be sent")));
    }

    @Test
    void handleMethodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(get(MeasurementController.BASE_URI)
                            .param("last", "dasdsadsa"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.messages[0]", equalTo("Parameter 'last' must be Long type")));
    }
}