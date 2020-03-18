package controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import watertank.WaterTankServer;
import watertank.models.Measurement;
import watertank.utils.JSONUtil;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=WaterTankServer.class)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class MeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    TestRestTemplate template;
    private static String deviceId;
    private static HttpHeaders headers;

    private static final String DEVICE_ID_HEADER = "Device-ID";
    private static final String CORRECT_DEVICE_ID = "test-device";
    private static final String WRONG_DEVICE_ID = "bad-device";

    @BeforeAll
    public static void setUp(){
        headers = new HttpHeaders();
        headers.add(DEVICE_ID_HEADER, CORRECT_DEVICE_ID);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    public void addNewMeasurementSuccessfully() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/measurements")
                                    .headers(headers)
                                    .content("{\"waterLevelDistance\": 23}")
        ).andDo(MockMvcResultHandlers.print())
         .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    public void addWaterLevelLowerThan0() throws Exception {
        this.mockMvc.perform(
            MockMvcRequestBuilders.post("/api/measurements")
                    .headers(headers)
                    .content("{\"waterLevelDistance\": -1}")
            ).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.messages[0]")
                                                    .value("waterLevelDistance should be positive or 0"));
    }


    @Test
    public void missingWithoutWaterLevelParam() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/measurements")
                        .headers(headers)
                        .content("{}")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messages[0]")
                                                .value("waterLevelDistance may not be null"));
    }


    @Test
    public void wrongJsonField() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/measurements")
                        .headers(headers)
                        .content("{\"asdasdasasdWaterLevelDistance\": 100}")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }


    @Test
    public void waterLevelIsNotInt() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/measurements")
                        .headers(headers)
                        .content("{\"waterLevelDistance\":  \"aaaaa\"}")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }


    @Test
    public void missingWaterLevelValue() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/measurements")
                        .headers(headers)
                        .content("{\"waterLevelDistance\":}")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }


    @Test
    public void unauthorizedDevice() throws Exception {
        headers.set(DEVICE_ID_HEADER, WRONG_DEVICE_ID);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/measurements")
                        .headers(headers)
                        .param("waterLevel","5")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        headers.set(DEVICE_ID_HEADER, CORRECT_DEVICE_ID);
    }

    @Test
    public void getAllMeasurements() throws Exception {
        MvcResult result = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/measurements")
                        .headers(headers)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        List<Object> measurementsList = JSONUtil.parseStringToArray(contentAsString);

        Assertions.assertTrue(measurementsList.size() > 0);
    }

    @Test
    public void getLatestMeasurements() throws Exception {
        MvcResult result = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/measurements")
                        .headers(headers)
                        .param("numberOfLatestRecords","1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        List<Object> measurementsList = JSONUtil.parseStringToArray(contentAsString);

        Assertions.assertTrue(measurementsList.size() == 1);
    }

    @Test
    public void getMeasurement() throws Exception {
        MvcResult result = this.mockMvc.perform(
                                        MockMvcRequestBuilders.get("/api/measurements/1/")
                                                .headers(headers)
                                ).andDo(MockMvcResultHandlers.print())
                                        .andExpect(MockMvcResultMatchers.status().isOk())
                                        .andReturn();

        String content = result.getResponse()
                                .getContentAsString();

        content.contains("{\"id\"}");
        content.contains("{\"createdAt\"}");
        content.contains("{\"waterTankDistance\"}");
        content.contains("{\"tankFullness\"}");
    }
}