package com.roshan.dto;

public class CustomerDto {

    private String name;

    private String passportNumber;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassportNumber() {
        return this.passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public String toString() {
        return "CustomerDto [name=" + this.name + ", passportNumber=" + this.passportNumber + "]";
    }

}
