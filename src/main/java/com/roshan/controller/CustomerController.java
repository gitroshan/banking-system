package com.roshan.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.roshan.domain.Account;
import com.roshan.domain.Customer;
import com.roshan.dto.AccountBalanceDto;
import com.roshan.dto.AccountDto;
import com.roshan.dto.BalanceTransferDto;
import com.roshan.dto.CustomerAccountDto;
import com.roshan.dto.CustomerDto;
import com.roshan.dto.GainInterestDto;
import com.roshan.dto.JointCustomerDto;
import com.roshan.dto.JointExistingCustomerDto;
import com.roshan.dto.SavedCustomerDto;
import com.roshan.exception.AccountNotFoundException;
import com.roshan.exception.CustomerNotFoundException;
import com.roshan.exception.InsufficientFundsException;
import com.roshan.repository.AccountRepository;
import com.roshan.repository.CustomerRepository;

@RestController
public class CustomerController {

    private @Autowired CustomerRepository customerRepository;

    private @Autowired AccountRepository accountRepository;

    @PostMapping("/customers/account")
    public ResponseEntity<Object> createCustomerAccount(@RequestBody CustomerDto customerDto) {

        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setPassportNumber(customerDto.getPassportNumber());

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        Set<Customer> customers = new HashSet<>();
        customers.add(customer);
        account.setCustomers(customers);
        customer.getAccounts().add(account);

        Customer savedCustomer = this.customerRepository.save(customer);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCustomer.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @PostMapping("/customers")
    public SavedCustomerDto createCustomer(@RequestBody CustomerDto customerDto) {

        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setPassportNumber(customerDto.getPassportNumber());

        Customer savedCustomer = this.customerRepository.save(customer);

        return new SavedCustomerDto(savedCustomer);

    }

    @PostMapping("/customers/{customerId}/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccountForCustomer(@PathVariable long customerId) {

        Optional<Customer> customerOptional = this.customerRepository.findById(Long.valueOf(customerId));

        if (!customerOptional.isPresent())
            throw new CustomerNotFoundException("id-" + customerId);

        Customer customer = customerOptional.get();

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        Set<Customer> customers = new HashSet<>();
        customers.add(customer);
        account.setCustomers(customers);
        customer.getAccounts().add(account);

        this.customerRepository.save(customer);

    }

    @PutMapping("/accounts/increase")
    public void increaseBalance(@RequestBody AccountBalanceDto accountBalance) {

        Optional<Customer> customerOptional = this.customerRepository
                .findById(Long.valueOf(accountBalance.getCustomerId()));

        if (!customerOptional.isPresent()) {
            throw new CustomerNotFoundException("id-" + accountBalance.getCustomerId());
        }

        Customer customer = customerOptional.get();
        Set<Account> accounts = customer.getAccounts();
        Account customerAccount = null;
        for (Account account : accounts) {
            if (account.getId().equals(Long.valueOf(accountBalance.getAccountId()))) {
                customerAccount = account;
                break;
            }
        }

        if (customerAccount == null) {
            throw new AccountNotFoundException("id-" + accountBalance.getAccountId());
        }

        customerAccount.setBalance(customerAccount.getBalance().add(accountBalance.getAmount()));

        this.accountRepository.save(customerAccount);
    }

    @PutMapping("/accounts/decrease")
    public void decreaseBalance(@RequestBody AccountBalanceDto accountBalance) {

        Optional<Customer> customerOptional = this.customerRepository
                .findById(Long.valueOf(accountBalance.getCustomerId()));

        if (!customerOptional.isPresent()) {
            throw new CustomerNotFoundException("id-" + accountBalance.getCustomerId());
        }

        Customer customer = customerOptional.get();
        Set<Account> accounts = customer.getAccounts();
        Account customerAccount = null;
        for (Account account : accounts) {
            if (account.getId().equals(Long.valueOf(accountBalance.getAccountId()))) {
                customerAccount = account;
                break;
            }
        }

        if (customerAccount == null) {
            throw new AccountNotFoundException("id-" + accountBalance.getAccountId());
        }

        BigDecimal balance = customerAccount.getBalance().subtract(accountBalance.getAmount());
        if (balance.compareTo(BigDecimal.ZERO) == -1) {
            throw new InsufficientFundsException("There is no enough funds.");
        }
        customerAccount.setBalance(balance);

        this.accountRepository.save(customerAccount);
    }

    @PostMapping("/customers/account/joint")
    @ResponseStatus(HttpStatus.CREATED)
    public void createJointAccount(@RequestBody JointCustomerDto jointCustomerDto) {

        List<CustomerDto> customerDtos = jointCustomerDto.getCustomers();
        if (CollectionUtils.isEmpty(customerDtos)) {
            throw new IllegalArgumentException("Please provide customers.");
        }

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setJoined(true);

        Set<Customer> customers = new HashSet<>();
        for (CustomerDto customerDto : customerDtos) {

            Customer customer = new Customer();
            customer.setName(customerDto.getName());
            customer.setPassportNumber(customerDto.getPassportNumber());
            customer.getAccounts().add(account);

            customers.add(customer);

        }

        account.setCustomers(customers);
        this.accountRepository.save(account);

    }

    @PostMapping("/customers/existing/account/joint")
    @ResponseStatus(HttpStatus.CREATED)
    public void createJointAccountForExistingCustomers(@RequestBody JointExistingCustomerDto jointCustomerDto) {

        List<Long> customerIds = jointCustomerDto.getCustomers();
        if (CollectionUtils.isEmpty(customerIds)) {
            throw new IllegalArgumentException("Please provide customers.");
        }

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setJoined(true);

        Set<Customer> customers = new HashSet<>();
        for (Long customerId : customerIds) {

            Optional<Customer> customerOptional = this.customerRepository.findById(customerId);

            if (!customerOptional.isPresent())
                throw new CustomerNotFoundException("id-" + customerId);

            Customer customer = customerOptional.get();
            customer.getAccounts().add(account);

            customers.add(customer);

        }

        account.setCustomers(customers);
        this.accountRepository.save(account);

    }

    @PutMapping("/accounts/transfer")
    public void tranferFund(@RequestBody BalanceTransferDto balanceTransfer) {

        Optional<Customer> customerOptional = this.customerRepository
                .findById(Long.valueOf(balanceTransfer.getCustomerId()));

        if (!customerOptional.isPresent()) {
            throw new CustomerNotFoundException("id-" + balanceTransfer.getCustomerId());
        }

        Customer customer = customerOptional.get();
        Set<Account> accounts = customer.getAccounts();
        Account fromAccount = null;
        Account toAccount = null;
        for (Account account : accounts) {
            if (account.getId().equals(Long.valueOf(balanceTransfer.getFromAccountId()))) {
                fromAccount = account;
                break;
            }
        }

        if (fromAccount == null) {
            throw new AccountNotFoundException("id-" + balanceTransfer.getFromAccountId());
        }

        List<Account> allAccounts = this.accountRepository.findAll();
        for (Account account : allAccounts) {
            if (account.getId().equals(Long.valueOf(balanceTransfer.getToAccountId()))) {
                toAccount = account;
                break;
            }
        }

        if (toAccount == null) {
            throw new AccountNotFoundException("id-" + balanceTransfer.getToAccountId());
        }

        BigDecimal fromBalance = fromAccount.getBalance().subtract(balanceTransfer.getAmount());
        if (fromBalance.compareTo(BigDecimal.ZERO) == -1) {
            throw new InsufficientFundsException("There is no enough funds.");
        }

        fromAccount.setBalance(fromBalance);
        this.accountRepository.save(fromAccount);

        BigDecimal toBalance = toAccount.getBalance().add(balanceTransfer.getAmount());

        toAccount.setBalance(toBalance);
        this.accountRepository.save(toAccount);

    }

    @PostMapping("/accounts/interest")
    public void gainInterest(@RequestBody GainInterestDto gainInterestDto) {

        Optional<Customer> customerOptional = this.customerRepository
                .findById(Long.valueOf(gainInterestDto.getCustomerId()));

        if (!customerOptional.isPresent()) {
            throw new CustomerNotFoundException("id-" + gainInterestDto.getCustomerId());
        }

        Customer customer = customerOptional.get();
        Set<Account> accounts = customer.getAccounts();
        Account account = null;

        for (Account customerAccount : accounts) {
            if (customerAccount.getId().equals(Long.valueOf(gainInterestDto.getAccountId()))) {
                account = customerAccount;
                break;
            }
        }

        if (account == null) {
            throw new AccountNotFoundException("id-" + gainInterestDto.getAccountId());
        }

        // TODO check the interest is already taken
        BigDecimal interestRate = getInterestRate();
        int noOfMonths = gainInterestDto.getNoOfMonths();

        BigDecimal interest = account.getBalance().multiply(interestRate).divide(BigDecimal.valueOf(100))
                .multiply(BigDecimal.valueOf(noOfMonths));

        account.setBalance(account.getBalance().add(interest));
        this.accountRepository.save(account);

    }

    /**
     * Gets the interest rate.
     *
     * @return the interest rate
     */
    private BigDecimal getInterestRate() {
        // TODO This should be taken from teh database, because this can be changed everytime.
        return BigDecimal.TEN;

    }

    private static String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }

    @GetMapping("/customers")
    public List<CustomerAccountDto> retrieveAllCustomers() {
        List<CustomerAccountDto> customerAccountDtos = new ArrayList<>();
        List<Customer> customers = this.customerRepository.findAll();

        if (customers != null) {
            for (Customer customer : customers) {
                CustomerAccountDto customerAccountDto = new CustomerAccountDto();

                customerAccountDto.setId(customer.getId());
                customerAccountDto.setName(customer.getName());
                customerAccountDto.setPassportNumber(customer.getPassportNumber());
                for (Account account : customer.getAccounts()) {
                    AccountDto accountDto = new AccountDto();
                    accountDto.setAccountNumber(account.getAccountNumber());
                    accountDto.setBalance(account.getBalance());
                    accountDto.setJoined(account.isJoined());
                    customerAccountDto.getAccountDtos().add(accountDto);
                }
                customerAccountDtos.add(customerAccountDto);
            }
        }
        return customerAccountDtos;
    }

    @GetMapping("/customers/{id}")
    public Customer retrieveCustomer(@PathVariable long id) {
        Optional<Customer> customer = this.customerRepository.findById(Long.valueOf(id));

        if (!customer.isPresent())
            throw new CustomerNotFoundException("id-" + id);

        return customer.get();
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable long id) {
        this.customerRepository.deleteById(Long.valueOf(id));
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Object> updateCustomer(@RequestBody Customer customer, @PathVariable long id) {

        Optional<Customer> customerOptional = this.customerRepository.findById(Long.valueOf(id));

        if (!customerOptional.isPresent())
            return ResponseEntity.notFound().build();

        customer.setId(Long.valueOf(id));

        this.customerRepository.save(customer);

        return ResponseEntity.noContent().build();
    }

}
