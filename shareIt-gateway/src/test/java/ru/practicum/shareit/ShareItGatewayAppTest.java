package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = ShareItGatewayApp.class)
@ActiveProfiles("test")
class ShareItGatewayAppTest {

    @Test
    void contextLoads() {
    }

}