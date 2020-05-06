package com.kindle.dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseKindleStore extends JdbcDaoSupport implements KindleStore {

    private static final String SQL_INCREASE_STOCK = "UPDATE book_stock SET stock = stock + ? WHERE isbn = ?";
    private static final String SQL_SELECT_STOCK = "SELECT stock FROM book_stock WHERE isbn = ?";

    protected void sleep(String threadName, long seconds) {
        System.out.println(threadName + " - Sleeping for " + seconds + " seconds");

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
        }

        System.out.println(threadName + " - Wake up");
    }

    @Override
    @Transactional
    public void increaseStock(String isbn, int stock, long secondsToSleepBeforeCommitOrRollback, boolean fail) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " - Prepare to increase book stock");

        getJdbcTemplate().update(SQL_INCREASE_STOCK, stock, isbn);

        System.out.println(threadName + " - Book stock increased by " + stock);
        sleep(threadName, secondsToSleepBeforeCommitOrRollback);

        if (fail) {
            System.out.println(threadName + " - Book stock rolled back");
            throw new RuntimeException("Increased by mistake");
        }
    }

    protected int checkStockActions(String isbn, long secondsToSleep) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " - Prepare to check book stock");

        int stock = getJdbcTemplate().queryForObject(SQL_SELECT_STOCK, Integer.class, isbn);

        System.out.println(threadName + " - Book stock is " + stock);
        sleep(threadName, secondsToSleep);
        System.out.println(threadName + " - Book stock is " + getJdbcTemplate().queryForObject(SQL_SELECT_STOCK, Integer.class, isbn));
        return stock;
    }
}
