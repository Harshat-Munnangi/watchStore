package com.store.watch.serviceImpl;

import com.store.watch.service.WatchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchServiceImpl implements WatchService {

    @Override
    public int checkoutWatches(List<String> watchIds) {
        return 0;
    }
}
