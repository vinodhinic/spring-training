package com.kindle;

import com.kindle.config.AppConfig;
import com.kindle.dao.KindleDao;
import com.kindle.dao.KindleStore;
import com.kindle.domain.Book;
import com.kindle.domain.BookStock;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ReadCommittedApp {
    public static void main(String[] args) throws InterruptedException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("readCommitted");
        context.register(AppConfig.class);
        context.refresh();

        testScenarioWhereIncreaseStockFails(context);
        testScenarioWhereIncreaseStockSucceeds(context);
        context.destroy();
    }

    private static void testScenarioWhereIncreaseStockFails(AnnotationConfigApplicationContext context) throws InterruptedException {
        KindleDao kindleDao = context.getBean(KindleDao.class);
        kindleDao.deleteAllBookAndStock();
        kindleDao.insertBook(new Book("A0", "Harry Potter", 900));
        kindleDao.insertBookStock(new BookStock("A0", 0));

        final KindleStore kindleStore = context.getBean(KindleStore.class);

        Thread increaseStockThread = new Thread(() -> {
            try {
                kindleStore.increaseStock("A0", 5, 10, true);
            } catch (RuntimeException e) {
            }
        }, "INCREASE-STOCK");

        Thread checkStockThread = new Thread(() -> {
            kindleStore.checkStock("A0", 10);
        }, "CHECK-STOCK");

        increaseStockThread.start(); // first increase the stock which increases stock, sleeps for 10 seconds and then rollsback

        Thread.sleep(5000);

        checkStockThread.start(); // check the stock 5 seconds later, by this stocks would have been increased but this will still read 0
        // when it wakes up, increaseStock would have rolled back the transaction. It still reads 0.

        increaseStockThread.join();
        checkStockThread.join();

        System.out.println(kindleDao.selectAllBookStocks());
    }

    private static void testScenarioWhereIncreaseStockSucceeds(AnnotationConfigApplicationContext context) throws InterruptedException {
        KindleDao kindleDao = context.getBean(KindleDao.class);
        kindleDao.deleteAllBookAndStock();
        kindleDao.insertBook(new Book("A0", "Harry Potter", 900));
        kindleDao.insertBookStock(new BookStock("A0", 0));

        final KindleStore kindleStore = context.getBean(KindleStore.class);

        Thread increaseStockThread = new Thread(() -> {
            try {
                kindleStore.increaseStock("A0", 5, 10, false);
            } catch (RuntimeException e) {
            }
        }, "INCREASE-STOCK");

        Thread checkStockThread = new Thread(() -> {
            kindleStore.checkStock("A0", 10);
        }, "CHECK-STOCK");

        increaseStockThread.start(); // first increase the stock which increases stock, sleeps for 10 seconds and then rollsback

        Thread.sleep(5000);

        checkStockThread.start(); // check the stock 5 seconds later, by this stocks would have been increased but this will still read 0
        // when it wakes up, increaseStock would have rolled back the transaction. It still reads 0.

        increaseStockThread.join();
        checkStockThread.join();

        System.out.println("Final : Book stocks:" + kindleDao.selectAllBookStocks());
        System.out.println("*********************************************");
    }
}
