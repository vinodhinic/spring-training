package com.kindle.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Profile("partialCartCheckout")
@Repository
public class KindleStoreImpl2 extends JdbcDaoSupport implements KindleStore {

    @Autowired
    public KindleStoreImpl2(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void purchase(String isbn, String userName) {
        purchaseDaoActions(isbn, userName);
    }
}
