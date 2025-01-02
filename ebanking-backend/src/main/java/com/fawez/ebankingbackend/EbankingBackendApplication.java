package com.fawez.ebankingbackend;

import com.fawez.ebankingbackend.entities.AccountOperation;
import com.fawez.ebankingbackend.entities.CurrentAccount;
import com.fawez.ebankingbackend.entities.Customer;
import com.fawez.ebankingbackend.entities.SavingAccount;
import com.fawez.ebankingbackend.enums.AccountStatus;
import com.fawez.ebankingbackend.enums.OperationType;
import com.fawez.ebankingbackend.repositories.AccountOperationRepository;
import com.fawez.ebankingbackend.repositories.BankAccountRepository;
import com.fawez.ebankingbackend.repositories.CustomerRepository;
import com.fawez.ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}
@Bean
CommandLineRunner commandLineRunner (BankService bankService){
		return args -> {
			bankService.consulter();

		};
}
	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){
		return args -> {
			Stream.of("Fawez","Ali","Yasmina").forEach(
					name->{

						Customer customer=new Customer();
						customer.setName(name);
						customer.setEmail(name+"@gmail.com");
						customerRepository.save(customer);

					});
			customerRepository.findAll().forEach(customer -> {
				CurrentAccount currentAccount=new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*9000);
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setOverDraft(9000);
				currentAccount.setCustomer(customer);

				bankAccountRepository.save(currentAccount);
			});
			customerRepository.findAll().forEach(customer -> {
				SavingAccount savingAccount=new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*9000);
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setInterestRate(5.5);
				savingAccount.setCustomer(customer);

				bankAccountRepository.save(savingAccount);
			});
			bankAccountRepository.findAll().forEach(op->{
				for (int i = 0; i <10 ; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random() * 12000);
					accountOperation.setBankAccount(op);
					accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
					accountOperationRepository.save(accountOperation);
				}
			});

		};
	}


}
