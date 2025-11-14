package ca.jrvs.practice.dataStructure.list;

import java.util.Comparator;

public class Employee implements Comparable<Employee> {

    private int id;
    private String name;
    private int age;
    private long salary;

    public Employee() {
    }

    public Employee(int id, String name, int age, long salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    // COMPARABLE: sort using age
    @Override
    public int compareTo(Employee other) {
        return Integer.compare(this.age, other.age);
    }

    //COMPARABLE: sort using salary
//    @Override
//    public int compareTo(Employee other) {
//        return Long.compare(other.salary, this.salary);
//    }

    @Override
    public String toString() {
        return String.format("Employee{id=%d, name='%s', age=%d, salary=%d}", id, name, age, salary);
    }

    // COMPARATOR: sort using age
    public static final Comparator<Employee> AGE =
            Comparator.comparingInt(Employee::getAge);

    // COMPARATOR: sort using salary
    public static final Comparator<Employee> SALARY_DESC =
            Comparator.comparingLong(Employee::getSalary).reversed();

}