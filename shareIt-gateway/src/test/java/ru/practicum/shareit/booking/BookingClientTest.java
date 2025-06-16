package ru.practicum.shareit.booking;
/*
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@SpringBootTest
//@ContextConfiguration(classes = BookingClient.class)
@SpringJUnitConfig( {BookingClient.class})
class BookingClientTest {
    @Autowired
    BookingClient bookingClient;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createBooking_whenBookItemRequestDtoValid() throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(1L, now, now.plusDays(2));

        ResponseEntity<Object> response = bookingClient.createBooking(1L, bookItemRequestDto);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(response.getBody(), objectMapper.writeValueAsString(bookItemRequestDto));


    }

    @Test
    void getBookings() {
    }

    @Test
    void getAllBookingsByStatus() {
    }

    @Test
    void getAllBookingsByStatusForOwner() {
    }

    @Test
    void getBooking() {
    }

    @Test
    void getAllBookingsByItemIdAndStatus() {
    }

    @Test
    void getAllBookingsFromBooker() {
    }

    @Test
    void updateBooking() {
    }

    @Test
    void deleteBooking() {
    }
}*/


/*
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.model.ValidationException;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingClientTest {

    @Mock
    private RestTemplateBuilder builder;

    @InjectMocks
    private BookingClient bookingClient;

    @Spy
    @InjectMocks
    private BookingClient bookingClientSpy;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        bookingClientSpy = Mockito.spy(new BookingClient("http://localhost:9090", new RestTemplateBuilder()));
    }

    @Test
    void createBooking_shouldThrowValidationException_whenEndBeforeStart() {
        BookItemRequestDto invalidDto = new BookItemRequestDto();
        LocalDateTime now = LocalDateTime.now();
        invalidDto.setStart(now.plusDays(2));
        invalidDto.setEnd(now.plusDays(1));

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingClientSpy.createBooking(1L, invalidDto)
        );

        assertEquals("Окончание бронирования вещи не может быть раньше её начала " +
                "или совпадать с ней.", ex.getMessage());
    }

    @Test
    void createBooking_shouldCallPost_whenValidDto() {
        BookItemRequestDto dto = new BookItemRequestDto();
        LocalDateTime now = LocalDateTime.now();
        dto.setStart(now.plusDays(1));
        dto.setEnd(now.plusDays(2));

        doReturn(ResponseEntity.ok().build())
                .when(bookingClientSpy).post(anyString(), anyLong(), any());

        ResponseEntity<Object> response = bookingClientSpy.createBooking(1L, dto);

        verify(bookingClientSpy, times(1))
                .post("", 1L, dto);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getBookings_shouldCallGetWithParams() {
        doReturn(ResponseEntity.ok().build())
                .when(bookingClientSpy).get(anyString(), anyLong(), any(Map.class));

        ResponseEntity<Object> response = bookingClientSpy.getBookings(2L, BookingState.WAITING, 0, 10);

        verify(bookingClientSpy, times(1)).get(
                "?state={state}&from={from}&size={size}",
                2L,
                Map.of("state", "WAITING", "from", 0, "size", 10)
        );

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void updateBooking_shouldCallPatchWithParams() {
        long bookingId = 3L;
        long userId = 4L;
        boolean approved = true;

        doReturn(ResponseEntity.ok().build())
                .when(bookingClientSpy).patch(anyString(), anyLong());

        ResponseEntity<Object> response = bookingClientSpy.updateBooking(bookingId, userId, approved);

        verify(bookingClientSpy, times(1))
                .patch("/" + bookingId + "?approved=" + approved, userId);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void deleteBooking_shouldCallDeleteWithCorrectParams() {
        long userId = 5L;
        long bookingId = 6L;

        doReturn(ResponseEntity.ok().build())
                .when(bookingClientSpy).delete(anyString(), anyLong());

        ResponseEntity<Object> response = bookingClientSpy.deleteBooking(userId, bookingId);

        verify(bookingClientSpy).delete("/" + bookingId, userId);

        assertEquals(200, response.getStatusCode().value());
    }
}*/

/*
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;

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
}*/
/*

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.model.ValidationException;
import org.springframework.boot.web.client.RestTemplateBuilder.*;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


//@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@SpringBootTest
//@ContextConfiguration(classes = BookingClient.class)
@SpringJUnitConfig( {BookingClient.class})
class BookingClientTest {

    @Autowired
    BookingClient bookingClient;

    @Autowired
    ObjectMapper objectMapper;

    //@Autowired
   // private RestTemplate restTemplate;
    private RestTemplate restTemplate;


   /* @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BookingClient bookingClient;

    @Captor
    ArgumentCaptor<String> pathCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingClient = new BookingClient("http://localhost", builderMock());
    }*/

    // Мокаем RestTemplateBuilder вручную
   /* private RestTemplateBuilder builderMock() {
        var builder = mock(org.springframework.boot.web.client.RestTemplateBuilder.class);
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory((Class<? extends ClientHttpRequestFactory>) any())).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);
        return builder;
    }

    @Test
    void createBooking_shouldThrowValidationException_whenEndBeforeStart() {
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now()
        );

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingClient.createBooking(1L, dto)
        );

        assertEquals("Окончание бронирования вещи не может быть раньше её начала или совпадать с ней.",
                exception.getMessage());
    }

    @Test
    void createBooking_shouldCallPost_whenValidDates() {
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2)
        );

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("OK");

        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.POST),
                any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingClient.createBooking(1L, dto);

        assertEquals(expectedResponse, response);
    }

    @Test
    void getBookings_shouldCallGetWithCorrectParams() {
        ResponseEntity<Object> expected = ResponseEntity.ok("result");

        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.GET),
                any(), eq(Object.class), anyMap()))
                .thenReturn(expected);

        ResponseEntity<Object> response = bookingClient.getBookings(1L, BookingState.ALL, 0, 10);

        assertEquals(expected, response);
    }

    @Test
    void updateBooking_shouldCallPatchWithCorrectParams() {
        ResponseEntity<Object> expected = ResponseEntity.ok("patched");

        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.PATCH),
                any(), eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = bookingClient.updateBooking(5L, 1L, true);

        assertEquals(expected, response);
    }

    @Test
    void deleteBooking_shouldCallDeleteWithCorrectParams() {
        ResponseEntity<Object> expected = ResponseEntity.noContent().build();

        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.DELETE),
                any(), eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = bookingClient.deleteBooking(1L, 99L);

        assertEquals(expected, response);
    }
}
*/

/*
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.model.ValidationException;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingClientSimpleTest {

    private RestTemplate restTemplate;
    private BookingClient bookingClient;

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);

        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);

        bookingClient = new BookingClient("http://test-server", builder);
    }

    @Test
    void createBooking_shouldCallPostAndReturnResponse() {
        // arrange
        LocalDateTime start = LocalDateTime.now().plusMinutes(10);
        LocalDateTime end = start.plusHours(2);
        BookItemRequestDto dto = new BookItemRequestDto(99L, start, end);

        ResponseEntity<Object> mockResponse = new ResponseEntity<>("created!", HttpStatus.OK);
        when(restTemplate.exchange(eq(""), HttpMethod.POST, any(), eq(Object.class)))
                .thenReturn(mockResponse);

        // act
        ResponseEntity<Object> response = bookingClient.createBooking(1L, dto);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("created!", response.getBody());
    }

    @Test
    void createBooking_shouldThrowValidationException_ifEndBeforeStart() {
        BookItemRequestDto dto = new BookItemRequestDto(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(1));
        assertThrows(ValidationException.class, () -> bookingClient.createBooking(1L, dto));
    }

    @Test
    void getBookings_shouldBuildCorrectParams() {
        ResponseEntity<Object> responseEntity = new ResponseEntity<>("result", HttpStatus.OK);

        when(restTemplate.exchange(
                eq("?state={state}&from={from}&size={size}"),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class),
                anyMap()
        )).thenReturn(responseEntity);

        ResponseEntity<Object> result = bookingClient.getBookings(5L, BookingState.FUTURE, 0, 5);
        assertEquals("result", result.getBody());
    }

    @Test
    void updateBooking_shouldCallPatch() {
        when(restTemplate.exchange(
                eq("/42?approved=true"),
                eq(HttpMethod.PATCH),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok("updated"));

        ResponseEntity<Object> response = bookingClient.updateBooking(42L, 10L, true);
        assertEquals("updated", response.getBody());
    }

    @Test
    void deleteBooking_shouldCallDelete() {
        when(restTemplate.exchange(
                eq("/3"),
                eq(HttpMethod.DELETE),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok("deleted"));

        ResponseEntity<Object> response = bookingClient.deleteBooking(1L, 3L);
        assertEquals("deleted", response.getBody());
    }
}*/
/*
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class BookingClientIntegrationStyleTest {

    private BookingClient bookingClient;
    private MockRestServiceServer mockServer;

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .requestFactory(this::clientHttpRequestFactory)
                .build();
    }

    @BeforeEach
    void setUp() {
        var restTemplate = new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:9090/bookings"))
                .requestFactory(HttpComponentsClientHttpRequestFactory)
                .build();

        mockServer = MockRestServiceServer.createServer(restTemplate);

        bookingClient = new BookingClient("http://localhost:8080", new RestTemplateBuilder() {
            @Override
            public RestTemplate build() {
                return restTemplate;
            }
        });
    }

    @Test
    void shouldSendGetBookingsRequestWithParams() {
        mockServer.expect(once(),
                        requestTo("http://localhost:8080/bookings?state=ALL&from=0&size=10"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", "123"))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = bookingClient.getBookings(123L, BookingState.ALL, 0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("[]", response.getBody());

        mockServer.verify();
    }

    @Test
    void shouldSendValidPostRequest() {
        LocalDateTime start = LocalDateTime.now().plusMinutes(5);
        LocalDateTime end = start.plusHours(1);
        BookItemRequestDto dto = new BookItemRequestDto(7L, start, end);

        mockServer.expect(once(),
                        requestTo("http://localhost:8080/bookings"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-Sharer-User-Id", "321"))
                .andRespond(withSuccess("{\"status\":\"ok\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = bookingClient.createBooking(321L, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("status"));

        mockServer.verify();
    }
}*/