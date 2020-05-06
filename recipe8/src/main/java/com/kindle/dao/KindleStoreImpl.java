package com.kindle.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Repository
@Profile("noPartialCartCheckout")
public class KindleStoreImpl extends JdbcDaoSupport implements KindleStore {

    @Autowired
    public KindleStoreImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    @Transactional
    public void purchase(String isbn, String userName) {
        purchaseDaoActions(isbn, userName);
    }
}
