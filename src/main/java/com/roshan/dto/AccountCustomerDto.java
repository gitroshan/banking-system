package com.roshan.dto;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class AccountCustomerDto {

    private Long id;

    private String accountNumber;

    private BigDecimal balance = BigDecimal.ZERO;

    private Set<CustomerDto> customers = new HashSet<>();

    private boolean joined = false;

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Set<CustomerDto> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<CustomerDto> customers) {
        this.customers = customers;
    }

    public boolean isJoined() {
        return this.joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AccountCustomerDto [id=" + this.id + ", accountNumber=" + this.accountNumber + ", balance="
                + this.balance + ", customers=" + this.customers + ", joined=" + this.joined + "]";
    }

}
