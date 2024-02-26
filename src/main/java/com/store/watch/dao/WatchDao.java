package com.store.watch.dao;

import com.store.watch.dto.Watch;

import java.util.List;
import java.util.Set;

public interface WatchDao {
    List<Watch> getWatchesList(Set<String> keys);
}
