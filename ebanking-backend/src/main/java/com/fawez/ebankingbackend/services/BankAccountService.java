package com.fawez.ebankingbackend.services;

import com.fawez.ebankingbackend.dtos.*;
import com.fawez.ebankingbackend.entities.BankAccount;
import com.fawez.ebankingbackend.entities.CurrentAccount;
import com.fawez.ebankingbackend.entities.Customer;
import com.fawez.ebankingbackend.entities.SavingAccount;
import com.fawez.ebankingbackend.exception.BalanceNotSufficientException;
import com.fawez.ebankingbackend.exception.BankAccountNotFoundException;

import java.util.List;

public interface BankAccountService {
     CustomerDTO saveCustomer(CustomerDTO customer);
     CurrentBankAccountDTO saveCurrentbankAccount(double initialBalance, Long customerId, double overDraft);
     SavingBankAccountDTO saveSavingbankAccount(double initialBalance, Long customerId, double interestRate);

     List<CustomerDTO> listCustomers();
     BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
     void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
     void credit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
     void transfer(String accountIdSource,String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;


     List<BankAccountDTO> bankAccountList();

     CustomerDTO getCustomer(Long customerId);

     CustomerDTO updateCustomer(CustomerDTO customer);

     void deleteCostumer(Long customerId);
     List<AccountOperationDTO> accountHistory(String accountId);

     AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
