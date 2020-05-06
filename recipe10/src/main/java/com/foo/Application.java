package com.foo;

import com.foo.config.AppConfig;
import com.foo.dao.EmployeeDao;
import com.foo.domain.Employee;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        EmployeeDao employeeDao = context.getBean(EmployeeDao.class);

        Employee batman = new Employee(7, "Bruce", "Wayne", 100);
        boolean created = employeeDao.create(batman);
        assert created;

        Employee byId = employeeDao.getById(7);
        System.out.println("Get employee by id : " + byId);
        assert batman.equals(byId);

        Employee superman = new Employee(16, "Clark", "Kent", 500);
        created = employeeDao.create(superman);
        assert created;

        List<Employee> allByLevel = employeeDao.getAllByLevel(500);
        System.out.println("All 500 level employees :" + allByLevel);

        assert allByLevel.size() == 1;
        assert allByLevel.get(0).getId() == 16;

        batman.setLevel(500);
        boolean updated = employeeDao.update(batman);
        assert updated;

        allByLevel = employeeDao.getAllByLevel(500);
        System.out.println("All 500 level employees :" + allByLevel);

        assert allByLevel.size() == 2;

        employeeDao.delete(batman);
        employeeDao.delete(superman);
        assert employeeDao.getById(7) == null;
    }
}
