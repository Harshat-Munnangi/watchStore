package com.store.watch.controller;

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
        assertEquals("{\"price\":234}", response.getBody());
    }

    @Test
    void shouldCheckoutWithEmptyWatchIds() {
        when(watchService.checkoutWatches(emptyList())).thenReturn(0);
        ResponseEntity<?> response = watchController.checkoutWatches(emptyList());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"price\":0}", response.getBody());
    }

    @Test
    void shouldNotCheckoutInvalidWatchIds() {
        List<String> watchIds = Arrays.asList("001", "005");
        doThrow(new WatchNotFoundException("Watch with ID 005 not found")).when(watchService).checkoutWatches(watchIds);
        ResponseEntity<?> response = watchController.checkoutWatches(watchIds);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"Watch with ID 005 not found\"}", response.getBody());
    }

    @Test
    void shouldNotCheckoutDueToUnexpectedError() {
        doThrow(new RuntimeException("Something went wrong")).when(watchService).checkoutWatches(anyList());
        ResponseEntity<?> response = watchController.checkoutWatches(Arrays.asList("213", "2343"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("{\"message\":\"Something went wrong\"}", response.getBody());
    }

    @Test
    void shouldCheckoutWithWhiteSpacedWatchIds() {
        when(watchService.checkoutWatches(Arrays.asList("001", "002", "003", "002", "001"))).thenReturn(370);
        ResponseEntity<?> response = watchController.checkoutWatches(Arrays.asList(" 001", "002 ", " 003 ", "002", "001"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"price\":370}", response.getBody());
    }
}
