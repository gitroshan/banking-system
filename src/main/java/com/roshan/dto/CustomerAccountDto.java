package com.roshan.dto;

import java.util.ArrayList;
import java.util.List;

public class CustomerAccountDto {

    private Long id;

    private String name;

    private String passportNumber;

    private List<AccountDto> accountDtos = new ArrayList<>();

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

    public List<AccountDto> getAccountDtos() {
        return this.accountDtos;
    }

    public void setAccountDtos(List<AccountDto> accountDtos) {
        this.accountDtos = accountDtos;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CustomerAccountDto [id=" + this.id + ", name=" + this.name + ", passportNumber=" + this.passportNumber
                + ", accountDtos=" + this.accountDtos + "]";
    }

}
