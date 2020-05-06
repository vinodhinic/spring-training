package com.kindle.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Profile("readCommitted")
@Repository
public class ReadCommittedKindleStoreImpl extends BaseKindleStore implements KindleStore {

    @Autowired
    public ReadCommittedKindleStoreImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int checkStock(String isbn, long secondsToSleep) {
        return super.checkStockActions(isbn, secondsToSleep);
    }
}
