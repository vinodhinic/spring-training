package com.kindle.dao;

public interface KindleStore {
    void increaseStock(String isbn, int stock, long secondsToSleepBeforeCommitOrRollback, boolean fail);

    int checkStock(String isbn, long secondsToSleep);

}
