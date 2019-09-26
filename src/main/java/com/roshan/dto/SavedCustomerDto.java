package com.roshan.dto;

import com.roshan.domain.Customer;

public class SavedCustomerDto {

    private long id;

    private String name;

    private String passportNumber;

    public SavedCustomerDto() {
        super();
    }

    public SavedCustomerDto(Customer customer) {
        super();
        this.id = customer.getId().longValue();
        this.name = customer.getName();
        this.passportNumber = customer.getPassportNumber();
    }

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

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
