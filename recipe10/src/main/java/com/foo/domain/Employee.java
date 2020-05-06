package com.foo.domain;

import java.util.Objects;

public class Employee {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer level;

    public Employee() {
        super();
    }

    public Employee(Integer id, String firstName, String lastName, Integer level) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.level = level;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) &&
                Objects.equals(firstName, employee.firstName) &&
                Objects.equals(lastName, employee.lastName) &&
                Objects.equals(level, employee.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, level);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", level=" + level +
                '}';
    }
}
