package com.store.watch.controller;

import com.store.watch.service.WatchService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

}
