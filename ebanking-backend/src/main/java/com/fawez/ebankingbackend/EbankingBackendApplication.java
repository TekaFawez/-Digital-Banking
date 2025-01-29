package com.fawez.ebankingbackend;

import com.fawez.ebankingbackend.dtos.CustomerDTO;
import com.fawez.ebankingbackend.entities.*;
import com.fawez.ebankingbackend.enums.AccountStatus;
import com.fawez.ebankingbackend.enums.OperationType;
import com.fawez.ebankingbackend.exception.BalanceNotSufficientException;
import com.fawez.ebankingbackend.exception.BankAccountNotFoundException;
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
CommandLineRunner commandLineRunner (BankAccountService bankService){
		return args -> {
			Stream.of("Sofiene","Med","Imen").forEach(name->{
				CustomerDTO customer=new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				bankService.saveCustomer(customer);
			});
			bankService.listCustomers().forEach(
					cust-> {
						bankService.saveCurrentbankAccount(Math.random() * 9000, cust.getId(), 9000);
						bankService.saveSavingbankAccount(Math.random() * 12000, cust.getId(), 5.5);
					List<BankAccount> bankAccountList= bankService.bankAccountList();
							for(BankAccount bankAccount :bankAccountList){
								for (int i = 0; i < 10; i++) {
                                    try {
                                        bankService.credit(bankAccount.getId(),90000+Math.random()*120000,"Credit");
										bankService.debit(bankAccount.getId(),9000+Math.random()*12000,"Debit");

								} catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
                                       e.printStackTrace();
                                    }

                                }


                            }
						});


					};


		};




}
