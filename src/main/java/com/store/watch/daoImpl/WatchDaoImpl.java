package com.store.watch.daoImpl;

import com.store.watch.dao.WatchDao;
import com.store.watch.dto.Watch;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WatchDaoImpl implements WatchDao {

    @Override
    public List<Watch> getAllWatches() {
        return List.of(
                new Watch("001", "Rolex", 100, 3, 200),
                new Watch("002", "Michael Kors", 80, 2, 120),
                new Watch("003", "Swatch", 50),
                new Watch("004", "Casio", 30)
        );
    }
}
