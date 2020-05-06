package com.kindle;

import com.kindle.config.AppConfig;
import com.kindle.dao.KindleDao;
import com.kindle.dao.KindleStore;
import com.kindle.domain.AmazonPay;
import com.kindle.domain.Book;
import com.kindle.domain.BookStock;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class AmazonApp {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("kindleStoreWithTransactionManagerAPI");
        context.register(AppConfig.class);
        context.refresh();
        testScenario(context);
        context.destroy();

        context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("kindleStoreWithTransactionTemplate");
        context.register(AppConfig.class);
        context.refresh();
        testScenario(context);
        context.destroy();

        context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("kindleStoreWithTransactionalAnnotation");
        context.register(AppConfig.class);
        context.refresh();
        testScenario(context);
        context.destroy();
    }

    private static void testScenario(AnnotationConfigApplicationContext context) {

        // setup data
        KindleDao kindleDao = context.getBean(KindleDao.class);
        kindleDao.insertBook(new Book("A0", "Harry Potter", 900));
        kindleDao.insertBookStock(new BookStock("A0", 1));
        kindleDao.insertAccount(new AmazonPay("Hermoine", 100));
        kindleDao.insertAccount(new AmazonPay("Ron", 2000));
        System.out.println("Testing for profile :" + Arrays.toString(context.getEnvironment().getActiveProfiles()));
        System.out.println("**********Before**********");
        System.out.println("Account details : " + kindleDao.selectAllAmazonPay());
        System.out.println("Book Stock details : " + kindleDao.selectAllBookStocks());

        KindleStore kindleStore = context.getBean(KindleStore.class);
        try {
            kindleStore.purchase("A0", "Hermoine");
        } catch (Throwable e) {
            // e.printStackTrace();
        }

        kindleStore.purchase("A0", "Ron");
        System.out.println("**********After**********");
        System.out.println("Account details : " + kindleDao.selectAllAmazonPay());
        System.out.println("Book Stock details : " + kindleDao.selectAllBookStocks());
        // clear data
        kindleDao.deleteAllBookAndStock();
        kindleDao.deleteAllAccounts();
    }
}
