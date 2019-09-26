package com.roshan.dto;

import java.util.ArrayList;
import java.util.List;

public class JointCustomerDto {

    private List<CustomerDto> customers = new ArrayList<>();

    public List<CustomerDto> getCustomers() {
        return this.customers;
    }

    public void setCustomers(List<CustomerDto> customers) {
        this.customers = customers;
    }

}
