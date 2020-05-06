package com.foo.dao;

import com.foo.domain.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@Transactional
public class EmployeeDaoTest {

    @Autowired
    private EmployeeDao employeeDao;

    @Test
    public void testInsert() {
        Employee employee = new Employee(90, "Harry", "Potter", 900);
        boolean b = employeeDao.create(employee);
        assertTrue(b);
        Employee byId = employeeDao.getById(90);
        assertNotNull(byId);
        assertEquals(byId, employee);
    }

}
