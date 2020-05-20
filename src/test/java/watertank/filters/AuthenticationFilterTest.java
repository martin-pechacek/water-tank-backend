package watertank.filters;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import watertank.controllers.MeasurementController;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationFilterTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void doFilterInvalidDeviceId() throws Exception {
       mockMvc.perform(get(MeasurementController.BASE_URI)
                                    .header("device-id", "device"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status", equalTo(401)))
                .andExpect(jsonPath("$.error", equalTo("Unauthorized")))
                .andExpect(jsonPath("$.messages[0]", equalTo("Invalid device id")));
    }

    @Test
    void doFilterValidDeviceId() throws Exception {
        mockMvc.perform(get(MeasurementController.BASE_URI)
                .header("device-id", "test-device"))
                .andExpect(status().isOk());
    }
}