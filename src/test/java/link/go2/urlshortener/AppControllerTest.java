package link.go2.urlshortener;

import com.fasterxml.jackson.databind.ObjectMapper;
import link.go2.urlshortener.entities.UrlEntity;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class AppControllerTest {
    @Autowired private MockMvc mockMvc;

    @Test
    void shold_ReturnStatus200AndJsonResult_when_getRequestCorrect() throws Exception {
        mockMvc.perform(get("/api/v1?url=https://go2.link/YqqWN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("https://google.com"));
    }

    @Test
    void shold_ReturnStatus400_when_emptyRequestParam() throws Exception {
        mockMvc.perform(get("/api/v1?url="))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shold_ReturnStatus400_when_nullRequestParam() throws Exception {
        mockMvc.perform(get("/api/v1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shold_ReturnStatus400_when_alienUrl() throws Exception {
        mockMvc.perform(get("/api/v1?url=https://alien.com"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shold_ReturnStatus200AndJsonResult_when_postRequestCorrect() throws Exception {
        UrlEntity body = new UrlEntity();
        body.setOriginalUrl("https://site.com");

        mockMvc.perform(post("/api/v1/url/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", Matchers.startsWith("https://go2.link/")));
    }

    @Test
    void shold_ReturnStatus400_when_postUrlNull() throws Exception {
        UrlEntity body = new UrlEntity();

        mockMvc.perform(post("/api/v1/url/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shold_ReturnStatus400_when_postInvalidUrl() throws Exception {
        UrlEntity body = new UrlEntity();
        body.setOriginalUrl("Invalid/url");

        mockMvc.perform(post("/api/v1/url/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shold_ReturnStatus400_when_postInvalidProtocol() throws Exception {
        UrlEntity body = new UrlEntity();
        body.setOriginalUrl("invalid://site.com");

        mockMvc.perform(post("/api/v1/url/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                )
                .andExpect(status().isBadRequest());
    }
}
