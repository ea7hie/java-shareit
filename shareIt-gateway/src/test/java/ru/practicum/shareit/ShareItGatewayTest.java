package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = ShareItGateway.class)
@ActiveProfiles("test")
class ShareItGatewayTest {

    @Test
    void contextLoads() {
    }

}