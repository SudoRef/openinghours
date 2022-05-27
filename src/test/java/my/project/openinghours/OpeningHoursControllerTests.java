package my.project.openinghours;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OpeningHoursControllerTests {


    @Autowired
    MockMvc mockMvc;
    @LocalServerPort
    int randomServerPort;

    @Test
    void sendGetRequest_AndReceiveCorrectSchedule() throws Exception {

        String goodRequest = TestUtils.OK_REQUEST;
        MvcResult mvcResult = mockMvc.perform(get("/opening-hours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodRequest))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(TestUtils.OK_RESPONSE, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void sendGetRequest_withNullData_AndReceiveInvalidArgumentException() throws Exception {
        String nullValueRequest = TestUtils.BROKEN_REQUEST_NULL_VALUE;
        MvcResult mvcResult = mockMvc.perform(get("/opening-hours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nullValueRequest))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Invalid type format. Type cannot be null, negative or bigger than 86399"));
    }

    @Test
    void sendGetRequest_withBrokenData_AndReceiveInvalidArgumentException1() throws Exception {
        String brokenRequest = TestUtils.BROKEN_REQUEST_NEGATIVE_VALUE;
        MvcResult mvcResult = mockMvc.perform(get("/opening-hours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brokenRequest))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Invalid type format. Type cannot be null, negative or bigger than 86399"));
    }

    @Test
    void sendGetRequest_withBrokenData_AndReceiveInvalidArgumentException2() throws Exception {
        String brokenRequest = TestUtils.BROKEN_REQUEST_TOO_BIG_VALUE;
        MvcResult mvcResult = mockMvc.perform(get("/opening-hours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brokenRequest))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Invalid type format. Type cannot be null, negative or bigger than 86399"));
    }

    @Test
    void sendGetRequest_withBrokenData_AndReceiveInvalidArgumentException3() throws Exception {
        String brokenRequest = TestUtils.BROKEN_REQUEST_MIXED_UP_VALUE;
        MvcResult mvcResult = mockMvc.perform(get("/opening-hours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brokenRequest))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Invalid type format. Previous value cannot be later than next one"));
    }

    @Test
    void sendGetRequest_Receive404() throws Exception {
        mockMvc.perform(get("/asdf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.OK_REQUEST))
                .andExpect(status().isNotFound());

    }

    @Test
    void sendGetRequest_Receive405() throws Exception {
        mockMvc.perform(post("/opening-hours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.OK_REQUEST))
                .andExpect(status().isMethodNotAllowed());

    }
}
