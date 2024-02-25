package com.store.watch.daoImpl;

import com.store.watch.dao.WatchDao;
import com.store.watch.dto.Watch;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WatchDaoImpl implements WatchDao {

    @Override
    public List<Watch> getAllWatches() {
        return null;
    }
}
