package com.tbook.Utils;

import java.util.List;

public class Contacts {
    private Integer id;

    private String name;

    private String department;

    private String duty;

    private String officeadress;

    private List<String> telephonenum;

    public List<String> getTelephonenum() {
        return telephonenum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getOfficeadress() {
        return officeadress;
    }

    public void setOfficeadress(String officeadress) {
        this.officeadress = officeadress;
    }

    public void setTelephonenum(List<String> telephonenum) {
        this.telephonenum = telephonenum;
    }

    @Override
    public String toString() {
        return "TelephoneNums{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", duty='" + duty + '\'' +
                ", officeadress='" + officeadress + '\'' +
                ", telephonenum=" + telephonenum +
                '}';
    }
}
