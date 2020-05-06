package com.foo.dao;

import com.foo.domain.Employee;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public boolean create(Employee employee) {
        return sqlSession.insert("insertEmployee", employee) > 0;
    }

    @Override
    public boolean update(Employee employee) {
        return sqlSession.update("updateEmployeeLevel", employee) > 0;
    }

    @Override
    public boolean delete(Employee employee) {
        return sqlSession.delete("deleteEmployeeById", employee) > 0;
    }

    @Override
    public Employee getById(Integer id) {
        return sqlSession.selectOne("selectEmployeeById", id);
    }

    @Override
    public List<Employee> getAllByLevel(Integer level) {
        return sqlSession.selectList("selectEmployeeByLevel", level);
    }
}
