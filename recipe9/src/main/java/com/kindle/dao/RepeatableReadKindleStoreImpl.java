package com.kindle.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Profile("repeatableRead")
@Repository
public class RepeatableReadKindleStoreImpl extends BaseKindleStore implements KindleStore {

    @Autowired
    public RepeatableReadKindleStoreImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public int checkStock(String isbn, long secondsToSleep) {
        return super.checkStockActions(isbn, secondsToSleep);
    }
}
