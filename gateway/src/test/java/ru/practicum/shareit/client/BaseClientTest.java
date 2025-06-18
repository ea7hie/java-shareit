package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BaseClientTest {

    @Mock
    private RestTemplate restTemplate;

    private BaseClient baseClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        baseClient = new BaseClient(restTemplate);
    }

    @Test
    void get_shouldCallRestTemplateWithCorrectParams() {
        String path = "/items";
        long userId = 1L;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>("response", HttpStatus.OK);

        when(restTemplate.exchange(
                eq(path),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of())))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = baseClient.get(path, userId, Map.of());

        assertEquals(expectedResponse, response);
        verify(restTemplate, times(1)).exchange(eq(path), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), eq(Map.of()));
    }

    @Test
    void post_shouldCallRestTemplateWithCorrectParams() {
        String path = "/items";
        long userId = 1L;
        String requestBody = "new item";
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>("created", HttpStatus.CREATED);

        when(restTemplate.exchange(
                eq(path),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of())))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = baseClient.post(path, userId, Map.of(), requestBody);

        assertEquals(expectedResponse, response);
        verify(restTemplate).exchange(eq(path), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class), eq(Map.of()));
    }

    @Test
    void makeAndSendRequest_shouldHandleHttpStatusCodeException() {
        String path = "/items";
        long userId = 1L;
        HttpStatusCodeException exception = mock(HttpStatusCodeException.class);

        when(exception.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        when(exception.getResponseBodyAsByteArray()).thenReturn("Not Found".getBytes());

        when(restTemplate.exchange(
                eq(path),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of())))
                .thenThrow(exception);

        ResponseEntity<Object> response = baseClient.get(path, userId, Map.of());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", new String((byte[]) response.getBody()));
    }
}