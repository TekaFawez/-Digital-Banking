package com.fawez.ebankingbackend.services;

import com.fawez.ebankingbackend.entities.BankAccount;
import com.fawez.ebankingbackend.entities.CurrentAccount;
import com.fawez.ebankingbackend.entities.SavingAccount;
import com.fawez.ebankingbackend.repositories.BankAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankService {
    private final BankAccountRepository bankAccountRepository;

    public BankService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }
    public void consulter(){
        BankAccount bankAccount=
                bankAccountRepository.findById("11fe28ad-9510-48eb-86ba-8123c415c15d").orElse(null);
        if(bankAccount!=null) {
            System.out.println("*****************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Over Draft=>" + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Rate=>" + ((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperations().forEach(op -> {
                System.out.println(op.getType() + "\t" + op.getOperationDate() + "\t" + op.getAmount());
            });
        }
    }
}
