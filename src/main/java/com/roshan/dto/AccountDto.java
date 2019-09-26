package com.roshan.dto;

import java.math.BigDecimal;

public class AccountDto {

    private String accountNumber;

    private BigDecimal balance = BigDecimal.ZERO;

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

    public boolean isJoined() {
        return this.joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    @Override
    public String toString() {
        return "AccountDto [accountNumber=" + this.accountNumber + ", balance=" + this.balance + ", joined="
                + this.joined + "]";
    }

}
