package com.kindle;

import com.kindle.config.AppConfig;
import com.kindle.dao.KindleCart;
import com.kindle.dao.KindleDao;
import com.kindle.domain.AmazonPay;
import com.kindle.domain.Book;
import com.kindle.domain.BookStock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("partialCartCheckout")
@ContextConfiguration(classes = {AppConfig.class})
public class PartialCheckoutKindleCartTest {

    @Autowired
    private KindleDao kindleDao;

    @Autowired
    private KindleCart kindleCart;

    @Before
    public void setup() {
        kindleDao.insertBook(new Book("A0", "Harry Potter", 900));
        kindleDao.insertBook(new Book("A1", "Hunger Games", 800));
        kindleDao.insertBookStock(new BookStock("A0", 2));
        kindleDao.insertBookStock(new BookStock("A1", 1));
        kindleDao.insertAccount(new AmazonPay("Ron", 1000));
    }

    @Test
    public void test() {
        try {
            kindleCart.checkout(com.sun.tools.javac.util.List.of("A0", "A1"), "Ron");
        } catch (Throwable e) {
            // e.printStackTrace();
        }

        Map<String, Integer> isbnToStock = kindleDao.selectAllBookStocks()
                .stream().collect(Collectors.toMap(BookStock::getIsbn, BookStock::getStock));
        assertEquals(1, isbnToStock.get("A0").intValue());
        assertEquals(1, isbnToStock.get("A1").intValue());

        List<AmazonPay> account = kindleDao.selectAllAmazonPay().stream()
                .filter(a -> a.getUserName().equals("Ron")).collect(Collectors.toList());
        assertNotNull(account);
        assertEquals(1, account.size());
        assertEquals(1000 - 900, account.get(0).getBalance().intValue());

        kindleDao.deleteAllBookAndStock();
        kindleDao.deleteAllAccounts();
    }
}
