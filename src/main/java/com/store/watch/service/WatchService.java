package com.store.watch.service;

import java.util.List;

public interface WatchService {
    int checkoutWatches(List<String> watchIds);
}
