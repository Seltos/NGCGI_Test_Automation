package com.enquero.api.driscolls.pojo;

public class employeeDetails {

    private String name;
    private String salary;
    private String age;

    public employeeDetails() {
    }

    public employeeDetails(String name, String salary, String age) {
        this.name = name;
        this.salary = salary;
        this.age = age;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setSalary(String salary){
        this.salary = salary;
    }
    public String getSalary(){
        return this.salary;
    }
    public void setAge(String age){
        this.age = age;
    }
    public String getAge(){
        return this.age;
    }

    public String toString() {
        return "My emp details is a " + this.name + " " + this.salary + " " + this.age;
    }
}
