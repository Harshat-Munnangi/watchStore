package com.store.watch.serviceImpl;

import com.store.watch.dao.WatchDao;
import com.store.watch.dto.Watch;
import com.store.watch.exception.CheckoutCalculationException;
import com.store.watch.exception.EmptyWatchListException;
import com.store.watch.exception.WatchNotFoundException;
import com.store.watch.service.WatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WatchServiceImpl implements WatchService {

    private static final Logger log = LoggerFactory.getLogger(WatchServiceImpl.class);

    private final WatchDao watchDao;

    @Autowired
    public WatchServiceImpl(WatchDao watchDao) {
        this.watchDao = watchDao;
    }

    @Override
    public int checkoutWatches(List<String> watchIds) throws WatchNotFoundException {
        log.info("Received request to checkout watches with IDs: {}", watchIds);

        if (watchIds.isEmpty()) {
            log.warn("Received an empty list of watch IDs. No watches to checkout.");
            throw new EmptyWatchListException("No watches to checkout");
        }

        Map<String, Integer> watchIdCountMap = new HashMap<>();
        watchIds.stream()
                .map(String::trim)
                .forEach(watchId -> watchIdCountMap.put(watchId, watchIdCountMap.getOrDefault(watchId, 0) + 1));

        List<Watch> availableWatchList = validateAndFetchWatchList(watchIdCountMap.keySet());

        return availableWatchList
                .parallelStream()
                .map(watch -> watch.calculateCost(watchIdCountMap.getOrDefault(watch.id(), 0)))
                .reduce(Integer::sum)
                .orElseThrow(() -> {
                    log.error("Unexpected error calculating total cost for checkout: {}", watchIds);
                    return new CheckoutCalculationException("Unable to checkout watches");
                });
    }

    private List<Watch> validateAndFetchWatchList(Set<String> watchIdCountMapKeys) {
        List<Watch> availableWatchList = watchDao.getWatchesList(watchIdCountMapKeys);
        validateWatchIds(availableWatchList, watchIdCountMapKeys);
        return availableWatchList;
    }

    private void validateWatchIds(List<Watch> availableWatchList, Set<String> watchIdCountMapKeys) {
        Set<String> availableWatchIds = availableWatchList.stream().map(Watch::id).collect(Collectors.toSet()); // converted due to better performance
        String invalidWatchIds = watchIdCountMapKeys.stream()
                .filter(watchId -> !availableWatchIds.contains(watchId))
                .collect(Collectors.joining(", "));

        if (!invalidWatchIds.isEmpty()) {
            log.warn("Invalid watch IDs found: {}", invalidWatchIds);
            throw new WatchNotFoundException("Watch with ID/s (" + invalidWatchIds + ") not found");
        }
    }
}
