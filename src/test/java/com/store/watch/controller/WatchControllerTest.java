package com.store.watch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.watch.exception.WatchNotFoundException;
import com.store.watch.service.WatchService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WatchControllerTest {

    @Mock
    private WatchService watchService;

    @InjectMocks
    private WatchController watchController;

    @Test
    void shouldCheckoutWithValidWatchIds() {
        List<String> watchIds = Arrays.asList("001", "002", "003");
        when(watchService.checkoutWatches(watchIds)).thenReturn(234);
        ResponseEntity<?> response = watchController.checkoutWatches(watchIds);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"price\":234}", convertObjectToJson(response.getBody()));
    }

    @Test
    void shouldCheckoutWithEmptyWatchIds() {
        when(watchService.checkoutWatches(emptyList())).thenReturn(0);
        ResponseEntity<?> response = watchController.checkoutWatches(emptyList());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"price\":0}", convertObjectToJson(response.getBody()));
    }

    @Test
    void shouldNotCheckoutInvalidWatchIds() {
        List<String> watchIds = Arrays.asList("001", "005");
        doThrow(new WatchNotFoundException("Watch with ID 005 not found")).when(watchService).checkoutWatches(watchIds);
        assertThrows(WatchNotFoundException.class, () -> watchController.checkoutWatches(watchIds), "Watch with ID 005 not found");
    }

    @Test
    void shouldNotCheckoutDueToUnexpectedError() {
        doThrow(new RuntimeException("Something went wrong")).when(watchService).checkoutWatches(anyList());
        assertThrows(RuntimeException.class, () -> watchController.checkoutWatches(Arrays.asList("213", "2343")), "Something went wrong");
    }

    @Test
    void shouldCheckoutWithWhiteSpacedWatchIds() {
        when(watchService.checkoutWatches(Arrays.asList("001", "002", "003", "002", "001"))).thenReturn(370);
        ResponseEntity<?> response = watchController.checkoutWatches(Arrays.asList(" 001", "002 ", " 003 ", "002", "001"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"price\":370}", convertObjectToJson(response.getBody()));
    }

    private String convertObjectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }
}
