package com.foo.dao;

import com.foo.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create employee table under public schema :
 * create table employee (id int, first_name varchar(50), last_name varchar(50), level int);
 */
@Repository
public class EmployeeDaoImpl extends NamedParameterJdbcDaoSupport implements EmployeeDao {

    private static final String SQL_INSERT_EMPLOYEE_BETTER_WAY =
            "INSERT INTO employee (id, first_name, last_name, level) VALUES (:id, :firstName, :lastName, :level)";
    private final String SQL_INSERT_EMPLOYEE = "insert into employee(id, first_name, last_name, level) values(?,?,?,?)";
    private final String SQL_SELECT_EMPLOYEE_BY_ID = "select * from employee where id = ?";
    private final String SQL_UPDATE_EMPLOYEE_BY_ID =
            "update employee set first_name = ?, last_name = ?, level  = ? where id = ?";
    private final String SQL_DELETE_EMPLOYEE_BY_ID_BETTER_WAY = "delete from employee where id = :id";
    private final String SQL_DELETE_EMPLOYEE_BY_ID = "delete from employee where id = ?";
    private final String SQL_SELECT_EMPLOYEE_BY_LEVEL = "select * from employee where level = ?";

    @Autowired
    public EmployeeDaoImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }


    // APPROACH : 1
   /* @Override
    public boolean create(Employee employee) {
        return getJdbcTemplate().update(SQL_INSERT_EMPLOYEE, employee.getId(), employee.getFirstName(), employee.getLastName(),
                employee.getLevel()) > 0;
    }*/

    // APPROACH : 2
   /*
    @Override
    public boolean create(Employee employee) {
        return getNamedParameterJdbcTemplate().update(SQL_INSERT_EMPLOYEE_BETTER_WAY, paramMap(employee)) > 0;
    } */

    // APPROACH : 3
    /*@Override
    public boolean create(Employee employee) {
        SqlParameterSource parameterSource =
                new MapSqlParameterSource(paramMap(employee));
        return getNamedParameterJdbcTemplate().update(SQL_INSERT_EMPLOYEE_BETTER_WAY, parameterSource) > 0;
    }*/

    // APPROACH : 4
    @Override
    public boolean create(Employee employee) {
        // note that there is no paramMap() call here.
        SqlParameterSource parameterSource =
                new BeanPropertySqlParameterSource(employee);
        return getNamedParameterJdbcTemplate().update(SQL_INSERT_EMPLOYEE_BETTER_WAY, parameterSource) > 0;
    }

    private Map<String, Object> paramMap(Employee employee) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", employee.getId());
        parameters.put("firstName", employee.getFirstName());
        parameters.put("lastName", employee.getLastName());
        parameters.put("level", employee.getLevel());
        return parameters;
    }

    @Override
    public boolean update(Employee employee) {
        // Mind the order of parameters
        return getJdbcTemplate().update(SQL_UPDATE_EMPLOYEE_BY_ID, employee.getFirstName(), employee.getLastName(),
                employee.getLevel(), employee.getId()) > 0;
    }

    @Override
    public boolean delete(Employee employee) {
        return getJdbcTemplate().update(SQL_DELETE_EMPLOYEE_BY_ID, employee.getId()) > 0;
    }

    @Override
    public Employee getById(Integer id) {
        return getJdbcTemplate().queryForObject(SQL_SELECT_EMPLOYEE_BY_ID, new Object[]{id}, new EmployeeMapper());
    }

    @Override
    public List<Employee> getAllByLevel(Integer level) {
        return getJdbcTemplate().query(SQL_SELECT_EMPLOYEE_BY_LEVEL, new Object[]{level}, new EmployeeMapper());
    }

    @Override
    public boolean create(List<Employee> employees) {
        SqlParameterSource[] sources = employees.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        return getNamedParameterJdbcTemplate().batchUpdate(SQL_INSERT_EMPLOYEE_BETTER_WAY, sources).length == employees.size();
    }

    @Override
    public boolean delete(List<Employee> employees) {
        SqlParameterSource[] sources = employees.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        return getNamedParameterJdbcTemplate().batchUpdate(SQL_DELETE_EMPLOYEE_BY_ID_BETTER_WAY, sources).length == employees.size();
    }
}
