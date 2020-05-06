package com.kindle.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

@Profile("kindleStoreWithTransactionManagerAPI")
@Repository
public class TransactionalKindleStore extends JdbcDaoSupport implements KindleStore {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    public TransactionalKindleStore(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void purchase(String isbn, String userName) {
        /*
        Before you start a new transaction, you have to specify the transaction attributes in a transaction definition
        object of type TransactionDefinition. For this example, you can simply create an instance
         of DefaultTransactionDefinition to use the default transaction attributes.
        */
        TransactionDefinition definition = new DefaultTransactionDefinition();
        /*
        Once you have a transaction definition, you can ask the transaction manager to start a new transaction with that definition
        by calling the getTransaction() method. Then, it will return a TransactionStatus object to keep track of the transaction status.
        If all the statements execute successfully, you ask the transaction manager to commit this transaction by passing
        in the transaction status. and to roll back the transaction when any exception is caught
         */
        TransactionStatus status = platformTransactionManager.getTransaction(definition);

        try {
            purchaseDaoActions(isbn, userName);
            platformTransactionManager.commit(status);
        } catch (Throwable e) {
            platformTransactionManager.rollback(status);
            throw e;
        }
    }
}
