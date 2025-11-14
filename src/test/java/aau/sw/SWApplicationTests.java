package aau.sw;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
// No @MockBean usage; test beans provided by TestBeansConfig under test profile.

@ActiveProfiles("test")
@SpringBootTest(classes = {SWApplication.class, aau.sw.config.TestBeansConfig.class, aau.sw.config.TestMongoConfig.class})
class SWApplicationTests {

    // Provide minimal mocks so the application context can start without MongoDB or S3
    // Bean mocks supplied by TestBeansConfig.

    @Test
    void contextLoads() {}
}
