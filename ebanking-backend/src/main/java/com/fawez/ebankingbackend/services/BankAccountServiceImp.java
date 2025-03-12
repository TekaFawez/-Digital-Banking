package com.fawez.ebankingbackend.services;

import com.fawez.ebankingbackend.dtos.*;
import com.fawez.ebankingbackend.entities.*;
import com.fawez.ebankingbackend.enums.OperationType;
import com.fawez.ebankingbackend.exception.BalanceNotSufficientException;
import com.fawez.ebankingbackend.exception.BankAccountNotFoundException;
import com.fawez.ebankingbackend.exception.CustomerNotFoundException;
import com.fawez.ebankingbackend.mapper.BankAccountMapperImp;
import com.fawez.ebankingbackend.repositories.AccountOperationRepository;
import com.fawez.ebankingbackend.repositories.BankAccountRepository;
import com.fawez.ebankingbackend.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class BankAccountServiceImp implements BankAccountService{
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final AccountOperationRepository accountOperationRepository;
    private final BankAccountMapperImp mapper;

    public BankAccountServiceImp(BankAccountRepository bankAccountRepository, CustomerRepository customerRepository, AccountOperationRepository accountOperationRepository, BankAccountMapperImp mapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.mapper = mapper;
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        log.info("Saving new Customer");

        Customer savedCustomer=customerRepository.save(mapper.toCustomer(customer));
        return mapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentbankAccount(double initialBalance, Long customerId, double overDraft) {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer Not Found");
        }
        CurrentAccount currentAccount=new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        return mapper.fromCurrentAccount(bankAccountRepository.save(currentAccount));

    }

    @Override
    public SavingBankAccountDTO saveSavingbankAccount(double initialBalance, Long customerId, double interestRate) {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer Not Found");
        }
        SavingAccount savingAccount=new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savingAccount1=  bankAccountRepository.save(savingAccount);
        return mapper.fromSavingAccount(savingAccount1);
    }



    @Override
    public List<CustomerDTO> listCustomers() {

        List<Customer> customerList=customerRepository.findAll();
        return customerList.stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {


        BankAccount bankAccount= bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank Account Not Found"));
        if (bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return mapper.fromSavingAccount(savingAccount);
        }else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return mapper.fromCurrentAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));

        if(bankAccount.getBalance()<amount){
            throw new BalanceNotSufficientException("Balance Not sufficient"); }
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"Transfer to"+accountIdDestination);
        credit(accountIdDestination,amount,"transfer from"+accountIdSource);

    }
     @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                return mapper.fromSavingAccount((SavingAccount) bankAccount);
            } else {
                return mapper.fromCurrentAccount((CurrentAccount) bankAccount);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) {
        Customer findCustomer=customerRepository.findById(customerId).orElseThrow(()
          -> new CustomerNotFoundException("Customer Not Found")
        );
        return mapper.fromCustomer(findCustomer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customer) {
        log.info("Saving new Customer");

        Customer savedCustomer=customerRepository.save(mapper.toCustomer(customer));
        return mapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCostumer(Long customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
      List<AccountOperation> accountOperations=  accountOperationRepository. findByBankAccount_Id(accountId);
        return accountOperations.stream().map(mapper::fromAccountOperation).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
     Page<AccountOperation> accountOperations= accountOperationRepository.findByBankAccount_Id(accountId, PageRequest.of(page,size));
     AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
     List<AccountOperationDTO> accountOperationDTOS=accountOperations.stream().map(mapper::fromAccountOperation).collect(Collectors.toList());
     accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
     accountHistoryDTO.setAccountId(bankAccount.getId());
     accountHistoryDTO.setBalance(bankAccount.getBalance());
     accountHistoryDTO.setCurrentPage(page);
     accountHistoryDTO.setPageSize(size);
     accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }
}
