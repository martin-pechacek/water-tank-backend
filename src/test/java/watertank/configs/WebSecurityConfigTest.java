package watertank.configs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import watertank.controllers.MeasurementController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class WebSecurityConfigTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user")
    public void testAuthorizedAccess() throws Exception {
        mockMvc.perform(get(MeasurementController.BASE_URI + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get(MeasurementController.BASE_URI + "/1"))
                .andExpect(status().isUnauthorized());
    }
}
