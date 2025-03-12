package com.fawez.ebankingbackend.mapper;

import com.fawez.ebankingbackend.dtos.AccountOperationDTO;
import com.fawez.ebankingbackend.dtos.CurrentBankAccountDTO;
import com.fawez.ebankingbackend.dtos.CustomerDTO;
import com.fawez.ebankingbackend.dtos.SavingBankAccountDTO;
import com.fawez.ebankingbackend.entities.AccountOperation;
import com.fawez.ebankingbackend.entities.CurrentAccount;
import com.fawez.ebankingbackend.entities.Customer;
import com.fawez.ebankingbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImp {
    public Customer toCustomer(CustomerDTO customerDTO){

        return Customer.builder()
                .id(customerDTO.getId())
                .email(customerDTO.getEmail())
                .name(customerDTO.getName())
                .build();
    }
    public CustomerDTO fromCustomer(Customer customer){
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
               customer.getEmail()
        );
    }
    public SavingAccount toSavingAccount(SavingBankAccountDTO savingBankAccountDTO){
        return SavingAccount.builder()
                .id(savingBankAccountDTO.getId())
                .balance(savingBankAccountDTO.getBalance())
                .createdAt(savingBankAccountDTO.getCreatedAt())
                .status(savingBankAccountDTO.getStatus())
                .customer(toCustomer(savingBankAccountDTO.getCustomerDTO()))
                .interestRate(savingBankAccountDTO.getInterestRate())
                .build();
    }

    public SavingBankAccountDTO fromSavingAccount(SavingAccount savingAccount){
        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO(
                savingAccount.getId(),
                savingAccount.getBalance(),
                savingAccount.getCreatedAt(),
                savingAccount.getStatus(),
                fromCustomer(savingAccount.getCustomer()),
                savingAccount.getInterestRate()
        );
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDTO;
    }
    public CurrentAccount toCurrentAccount(CurrentBankAccountDTO currentBankAccountDTO){
        return CurrentAccount.builder()
                .id(currentBankAccountDTO.getId())
                .balance(currentBankAccountDTO.getBalance())
                .createdAt(currentBankAccountDTO.getCreatedAt())
                .overDraft(currentBankAccountDTO.getOverDraft())
                .status(currentBankAccountDTO.getStatus())
                .customer(toCustomer(currentBankAccountDTO.getCustomerDTO()) )
                .build();
    }

    public CurrentBankAccountDTO fromCurrentAccount(CurrentAccount currentAccount){
        CurrentBankAccountDTO currentBankAccountDTO=new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccountDTO;
    }
    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return accountOperationDTO;

    }

}
