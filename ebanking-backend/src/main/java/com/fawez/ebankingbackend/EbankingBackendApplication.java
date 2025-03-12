package com.fawez.ebankingbackend;

import com.fawez.ebankingbackend.dtos.BankAccountDTO;
import com.fawez.ebankingbackend.dtos.CurrentBankAccountDTO;
import com.fawez.ebankingbackend.dtos.CustomerDTO;
import com.fawez.ebankingbackend.dtos.SavingBankAccountDTO;
import com.fawez.ebankingbackend.entities.*;
import com.fawez.ebankingbackend.enums.AccountStatus;
import com.fawez.ebankingbackend.enums.OperationType;
import com.fawez.ebankingbackend.exception.BalanceNotSufficientException;
import com.fawez.ebankingbackend.exception.BankAccountNotFoundException;
import com.fawez.ebankingbackend.exception.CustomerNotFoundException;
import com.fawez.ebankingbackend.repositories.AccountOperationRepository;
import com.fawez.ebankingbackend.repositories.BankAccountRepository;
import com.fawez.ebankingbackend.repositories.CustomerRepository;
import com.fawez.ebankingbackend.services.BankAccountService;
import com.fawez.ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}

	//@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
		return args -> {
			Stream.of("Hassan", "Imane", "Mohamed").forEach(name -> {
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name + "@gmail.com");
				bankAccountService.saveCustomer(customer);
			});
			bankAccountService.listCustomers().forEach(customer -> {
				try {
					bankAccountService.saveCurrentbankAccount(Math.random() * 90000, customer.getId(), 9000);
					bankAccountService.saveSavingbankAccount(Math.random() * 120000, customer.getId(), 5.5);

				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
			});
			List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
			for (BankAccountDTO bankAccount : bankAccounts) {
				for (int i = 0; i < 10; i++) {
					String accountId;
					if (bankAccount instanceof SavingBankAccountDTO) {
						accountId = ((SavingBankAccountDTO) bankAccount).getId();
					} else {
						accountId = ((CurrentBankAccountDTO) bankAccount).getId();
					}
                    try {
                        bankAccountService.credit(accountId, 10000 + Math.random() * 120000, "Credit");
                    } catch (BalanceNotSufficientException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        bankAccountService.debit(accountId, 1000 + Math.random() * 9000, "Debit");
                    } catch (BalanceNotSufficientException e) {
                        throw new RuntimeException(e);
                    }
                }
			}
		};
	}
}