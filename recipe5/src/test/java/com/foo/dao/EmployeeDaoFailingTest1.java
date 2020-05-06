package com.foo.dao;

import com.foo.domain.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class EmployeeDaoFailingTest1 {

    /*
    Run this test class first - you will observe that both tests are succeeded.
    Run this again - you will see that select test is failing. Understand why it is failing -> hint : queryForObject works only on primary key
     */
    @Autowired
    private EmployeeDao employeeDao;

    @Test
    public void testInsert() {
        boolean b = employeeDao.create(new Employee(90, "Harry", "Potter", 900));
        assertTrue(b);
    }

    @Test
    public void testSelect() {
        Employee byId = employeeDao.getById(90);
        assertNotNull(byId);
    }
}
