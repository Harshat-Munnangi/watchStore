package com.store.watch.daoImpl;

import com.store.watch.dao.WatchDao;
import com.store.watch.dto.Watch;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class WatchDaoImpl implements WatchDao {

    private final List<Watch> dbWatchList = List.of(
            new Watch("001", "Rolex", 100, 3, 200),
            new Watch("002", "Michael Kors", 80, 2, 120),
            new Watch("003", "Swatch", 50),
            new Watch("004", "Casio", 30)
    );

    @Override
    public List<Watch> getWatchesList(Set<String> keys) {
        return dbWatchList.stream().filter(watch -> keys.contains(watch.id())).toList();
    }
}
