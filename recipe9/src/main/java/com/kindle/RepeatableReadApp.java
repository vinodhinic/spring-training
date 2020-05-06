package com.kindle;

import com.kindle.config.AppConfig;
import com.kindle.dao.KindleDao;
import com.kindle.dao.KindleStore;
import com.kindle.domain.Book;
import com.kindle.domain.BookStock;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RepeatableReadApp {

    public static void main(String[] args) throws InterruptedException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("repeatableRead");
        context.register(AppConfig.class);
        context.refresh();
        testScenario(context);

    }

    private static void testScenario(AnnotationConfigApplicationContext context) throws InterruptedException {
        KindleDao kindleDao = context.getBean(KindleDao.class);
        kindleDao.deleteAllBookAndStock();
        kindleDao.insertBook(new Book("A0", "Harry Potter", 900));
        kindleDao.insertBookStock(new BookStock("A0", 0));

        final KindleStore kindleStore = context.getBean(KindleStore.class);

        Thread increaseStockThread = new Thread(() -> {
            try {
                kindleStore.increaseStock("A0", 5, 2, false);
            } catch (RuntimeException e) {
            }
        }, "INCREASE-STOCK");

        Thread checkStockThread = new Thread(() -> {
            kindleStore.checkStock("A0", 15);
        }, "CHECK-STOCK");

        checkStockThread.start(); // checks the stock - reads 0 - when it wakes up increaseStock would have updated the stock
        // now if it reads again - it will see the same 0 (change it to read committed - it would read 5)

        Thread.sleep(5000);

        increaseStockThread.start();

        increaseStockThread.join();
        checkStockThread.join();

        System.out.println("Final : Book stocks :" + kindleDao.selectAllBookStocks());
    }
}
