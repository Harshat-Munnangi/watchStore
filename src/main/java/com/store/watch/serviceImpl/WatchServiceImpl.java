package com.store.watch.serviceImpl;

import com.store.watch.dao.WatchDao;
import com.store.watch.dto.Watch;
import com.store.watch.exception.CheckoutCalculationException;
import com.store.watch.exception.WatchNotFoundException;
import com.store.watch.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WatchServiceImpl implements WatchService {

    private final WatchDao watchDao;

    @Autowired
    public WatchServiceImpl(WatchDao watchDao) {
        this.watchDao = watchDao;
    }

    @Override
    public int checkoutWatches(List<String> watchIds) throws WatchNotFoundException {
        Map<String, Integer> watchCounts = new HashMap<>();
        List<Watch> watches = watchDao.getAllWatches();

        validateWatches(watches, watchIds);
        watchIds.forEach(watchId -> watchCounts.put(watchId, watchCounts.getOrDefault(watchId, 0) + 1));

        return watches
                .parallelStream()
                .map(watch -> watch.calculateCost(watchCounts.getOrDefault(watch.id(), 0)))
                .reduce(Integer::sum)
                .orElseThrow(() -> new CheckoutCalculationException("Unable to checkout watches"));
    }

    private void validateWatches(List<Watch> watches, List<String> watchIds) {
        watchIds.parallelStream()
                .map(String::trim)
                .filter(watchId -> watches.parallelStream().noneMatch((watch) -> watch.id().equals(watchId)))
                .reduce((id1, id2) -> id1 + ", " + id2)
                .ifPresent(watchId -> {
                    throw new WatchNotFoundException("Watch with ID/s (" + watchId + ") not found");
                });
    }
}
