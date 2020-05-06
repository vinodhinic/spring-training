package com.kindle.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Profile("kindleStoreWithTransactionTemplate")
@Repository
public class AnotherTransactionalKindleStore extends JdbcDaoSupport implements KindleStore {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    public AnotherTransactionalKindleStore(DataSource dataSource) {
        setDataSource(dataSource);
    }


    @Override
    public void purchase(String isbn, String userName) {

        // Here we have no value to return so TransactionCallbackWithoutResult is fine.

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            /*
            If doInTransactionWithoutResult() throws an unchecked exception (RuntimeException and DataAccessException fall into this category)
             or if you explicitly called status.setRollbackOnly(), the transaction will be rolled back.
             Otherwise, it will be committed after the callback object completes.

             This way we don't need to start, roll back, or commit the transaction ourselves anymore. TransactionTemplate takes care of it.
             */
            protected void doInTransactionWithoutResult(
                    TransactionStatus status) {
                purchaseDaoActions(isbn, userName);
            }
        });

    }
}
