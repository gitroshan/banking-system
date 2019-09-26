package com.roshan.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.roshan.domain.Account;
import com.roshan.domain.Customer;
import com.roshan.dto.AccountCustomerDto;
import com.roshan.dto.CustomerDto;
import com.roshan.exception.CustomerNotFoundException;
import com.roshan.repository.AccountRepository;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/accounts")
    public List<AccountCustomerDto> retrieveAllCustomers() {

        List<AccountCustomerDto> accountDtos = new ArrayList<>();
        List<Account> accounts = this.accountRepository.findAll();
        if (accounts != null) {
            for (Account account : accounts) {
                AccountCustomerDto accountDto = new AccountCustomerDto();
                accountDto.setId(account.getId());
                accountDto.setAccountNumber(account.getAccountNumber());
                accountDto.setBalance(account.getBalance());
                accountDto.setJoined(account.isJoined());
                Set<Customer> customers = account.getCustomers();
                for (Customer customer : customers) {
                    CustomerDto customerDto = new CustomerDto();
                    customerDto.setName(customer.getName());
                    customerDto.setPassportNumber(customer.getPassportNumber());
                    accountDto.getCustomers().add(customerDto);

                }
                accountDtos.add(accountDto);
            }
        }

        return accountDtos;
    }

    @GetMapping("/accounts/{id}")
    public Account retrieveCustomer(@PathVariable long id) {
        Optional<Account> customer = this.accountRepository.findById(Long.valueOf(id));

        if (!customer.isPresent())
            throw new CustomerNotFoundException("id-" + id);

        return customer.get();
    }

    @DeleteMapping("/accounts/{id}")
    public void deleteCustomer(@PathVariable long id) {
        this.accountRepository.deleteById(Long.valueOf(id));
    }

    @PostMapping("/accounts")
    public ResponseEntity<Object> createCustomer(@RequestBody Account customer) {
        Account savedCustomer = this.accountRepository.save(customer);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCustomer.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<Object> updateCustomer(@RequestBody Account customer, @PathVariable long id) {

        Optional<Account> customerOptional = this.accountRepository.findById(Long.valueOf(id));

        if (!customerOptional.isPresent())
            return ResponseEntity.notFound().build();

        customer.setId(Long.valueOf(id));

        this.accountRepository.save(customer);

        return ResponseEntity.noContent().build();
    }

}
