package com.store.watch.controller;

import com.store.watch.dto.CheckoutResponse;
import com.store.watch.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WatchController {

    private final WatchService watchService;

    @Autowired
    public WatchController(WatchService watchService) {
        this.watchService = watchService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkoutWatches(@RequestBody List<String> watchIds) {
        List<String> newWatchIds = watchIds.stream().map(String::trim).toList();
        int totalPrice = watchService.checkoutWatches(newWatchIds);
        return ResponseEntity.ok(new CheckoutResponse(totalPrice));
    }
}
