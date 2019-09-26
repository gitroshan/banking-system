package com.roshan.dto;

import java.util.ArrayList;
import java.util.List;

public class JointExistingCustomerDto {

    private List<Long> customers = new ArrayList<>();

    public List<Long> getCustomers() {
        return this.customers;
    }

    public void setCustomers(List<Long> customers) {
        this.customers = customers;
    }

}
