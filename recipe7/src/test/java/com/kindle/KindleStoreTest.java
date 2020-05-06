package com.kindle;

import com.kindle.config.AppConfig;
import com.kindle.config.TransactionalAppConfig;
import com.kindle.dao.KindleDao;
import com.kindle.dao.KindleStore;
import com.kindle.domain.AmazonPay;
import com.kindle.domain.Book;
import com.kindle.domain.BookStock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("kindleStoreWithTransactionalAnnotation")
@ContextConfiguration(classes = {TransactionalAppConfig.class, AppConfig.class})
public class KindleStoreTest {

    @Autowired
    private KindleDao kindleDao;

    @Autowired
    private KindleStore kindleStore;

    @Before
    public void setup() {
        kindleDao.insertBook(new Book("A0", "Harry Potter", 900));
        kindleDao.insertBookStock(new BookStock("A0", 1));
        kindleDao.insertAccount(new AmazonPay("Hermoine", 100));
        kindleDao.insertAccount(new AmazonPay("Ron", 2000));
    }

    @Test
    public void testIfStockIsRolledBackForInsufficientBalances() {
        boolean fail = false;
        try {
            kindleStore.purchase("A0", "Hermoine");
        } catch (Throwable e) {
            fail = true;
        }

        if (!fail) {
            Assert.fail("Expected the previous purchase to fail due to insufficient balance");
        }

        List<BookStock> bookStocks = kindleDao.selectAllBookStocks().stream().filter(e -> e.getIsbn().equals("A0")).collect(Collectors.toList());
        assertNotNull(bookStocks);
        assertEquals(1, bookStocks.size());
        assertEquals(1, bookStocks.get(0).getStock().intValue());

        List<AmazonPay> hermoineAccount = kindleDao.selectAllAmazonPay().stream().filter(a -> a.getUserName().equals("Hermoine")).collect(Collectors.toList());
        assertNotNull(hermoineAccount);
        assertEquals(1, hermoineAccount.size());
        assertEquals(100, hermoineAccount.get(0).getBalance().intValue());

        kindleDao.deleteAllBookAndStock();
        kindleDao.deleteAllAccounts();
    }

    /**
     * You already know from recipe5 @Transactional rollsback when annotated with a test method/test class.
     * Why have I annotated @Transactional only for this test?
     * You will understand this more at the next recipes involving propagation and isolation. But the reason why
     * testIfStockIsRolledBackForInsufficientBalances is not annotated with @Transactional (we are clearing the data explicitly using deleteAll apis)
     * is because :
     * The Transactions at KindleStore where we rollback when there is a check constraint violation, rolls back the transaction initiated by
     * the test context.
     * Test -> begins transaction
     * KindleStore -> rollsback in this case since the test data is such that to create check constraint violation
     * so the selectAllBookStocks will fail with "ERROR: current transaction is aborted, commands ignored until end of transaction block"
     * <p>
     * Don't worry about it if this is not clear. We will have more recipes to understand this concept better.
     * So what is the right way? Use @Transactional at test or clear it ourselves? - IMO, just delete it yourself.
     * In Trig we use embedded database and dirties context which creates a new embedded db for every test.
     * Trig is not DB heavy so it works for us.
     */
    @Test
    @Transactional
    public void testIfStockAndAmazonPayBalancesAreDecrementedForSuccessfulPurchase() {
        kindleStore.purchase("A0", "Ron");
        List<BookStock> bookStocks = kindleDao.selectAllBookStocks().stream().filter(e -> e.getIsbn().equals("A0")).collect(Collectors.toList());
        assertNotNull(bookStocks);
        assertEquals(1, bookStocks.size());
        assertEquals(0, bookStocks.get(0).getStock().intValue());

        List<AmazonPay> ronAccount = kindleDao.selectAllAmazonPay().stream().filter(a -> a.getUserName().equals("Ron")).collect(Collectors.toList());
        assertNotNull(ronAccount);
        assertEquals(1, ronAccount.size());
        assertEquals(1100, ronAccount.get(0).getBalance().intValue());
    }
}
