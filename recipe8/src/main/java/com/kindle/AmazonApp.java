package com.kindle;

import com.kindle.config.AppConfig;
import com.kindle.dao.KindleCart;
import com.kindle.dao.KindleDao;
import com.kindle.domain.AmazonPay;
import com.kindle.domain.Book;
import com.kindle.domain.BookStock;
import com.sun.tools.javac.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class AmazonApp {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("partialCartCheckout");
        context.register(AppConfig.class);
        context.refresh();
        testScenario(context);
        context.destroy();

        context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("noPartialCartCheckout");
        context.register(AppConfig.class);
        context.refresh();
        testScenario(context);
        context.destroy();
    }

    private static void testScenario(ApplicationContext context) {
        // setup data
        KindleDao kindleDao = context.getBean(KindleDao.class);
        kindleDao.insertBook(new Book("A0", "Harry Potter", 900));
        kindleDao.insertBook(new Book("A1", "Hunger Games", 800));
        kindleDao.insertBookStock(new BookStock("A0", 2));
        kindleDao.insertBookStock(new BookStock("A1", 1));
        kindleDao.insertAccount(new AmazonPay("Ron", 1000));
        System.out.println("Testing for profile :" + Arrays.toString(context.getEnvironment().getActiveProfiles()));
        System.out.println("**********Before**********");
        System.out.println("Account details : " + kindleDao.selectAllAmazonPay());
        System.out.println("Book Stock details : " + kindleDao.selectAllBookStocks());

        KindleCart kindleCart = context.getBean(KindleCart.class);

        try {
            kindleCart.checkout(List.of("A0", "A1"), "Ron");
        } catch (Throwable e) {
            // e.printStackTrace();
        }

        System.out.println("**********After**********");
        System.out.println("Account details : " + kindleDao.selectAllAmazonPay());
        System.out.println("Book Stock details : " + kindleDao.selectAllBookStocks());

        // clear data
        kindleDao.deleteAllBookAndStock();
        kindleDao.deleteAllAccounts();
    }
}
