package com.store.watch.service;

import com.store.watch.dao.WatchDao;
import com.store.watch.dto.Watch;
import com.store.watch.exception.EmptyWatchListException;
import com.store.watch.exception.WatchNotFoundException;
import com.store.watch.serviceImpl.WatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WatchServiceTest {

    private final List<Watch> watches = List.of(
            new Watch("001", "Rolex", 100, 3, 200),
            new Watch("002", "Michael Kors", 80, 2, 120),
            new Watch("003", "Swatch", 50)
    );

    @Mock
    private WatchDao watchDao;

    @InjectMocks
    private WatchServiceImpl watchService;

    @BeforeEach
    void setUp() {
        when(watchDao.getWatchesList(anySet())).thenReturn(watches);
    }

    @Test
    void shouldCheckoutWithSingleValidWatch() {
        assertEquals(100, watchService.checkoutWatches(List.of("001")));
    }

    @Test
    void shouldNotCheckoutWithEmptyWatchIdsList() {
        EmptyWatchListException exception = assertThrows(EmptyWatchListException.class, () -> watchService.checkoutWatches(emptyList()));
        assertEquals("No watches to checkout", exception.getMessage());
    }

    @Test
    void shouldCheckoutWithValidWatches() {
        List<String> watchIds = Arrays.asList("001", "002", "003");
        int totalPrice = watchService.checkoutWatches(watchIds);
        assertEquals(230, totalPrice);
    }

    @Test
    void shouldNotCheckoutWithSingleInvalidWatch() {
        WatchNotFoundException exception = assertThrows(WatchNotFoundException.class, () -> watchService.checkoutWatches(List.of("0#4")));
        assertTrue(exception.getMessage().contains("0#4"));
    }

    @Test
    void shouldCheckoutWithDiscounts() {
        int totalPrice = watchService.checkoutWatches(Arrays.asList("001", "002", "001", "001", "001", "002", "003"));
        assertEquals(470, totalPrice);
    }

    @Test
    void shouldNotCheckoutWithMixedValidAndInvalidWatchIds() {
        List<String> watchIds = Arrays.asList("001", "002", "005", "002", "0#1", "003");
        WatchNotFoundException exception = assertThrows(WatchNotFoundException.class, () -> watchService.checkoutWatches(watchIds));
        assertTrue(exception.getMessage().contains("005, 0#1"));
    }

    @Test
    void shouldCheckoutWithWhiteSpacedWatchIds() {
        List<String> watchIds = Arrays.asList(" 001", "002 ", " 003 ", "002", "001");
        int totalPrice = watchService.checkoutWatches(watchIds);
        assertEquals(370, totalPrice);
    }
}