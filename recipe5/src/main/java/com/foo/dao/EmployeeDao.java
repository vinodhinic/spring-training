package com.foo.dao;

import com.foo.domain.Employee;

import java.util.List;

public interface EmployeeDao {
    boolean create(Employee employee);

    boolean update(Employee employee);

    boolean delete(Employee employee);

    Employee getById(Integer id);

    List<Employee> getAllByLevel(Integer level);

    boolean create(List<Employee> employees);

    boolean delete(List<Employee> employees);
}
