package com.fawez.ebankingbackend.web;

import com.fawez.ebankingbackend.dtos.AccountHistoryDTO;
import com.fawez.ebankingbackend.dtos.AccountOperationDTO;
import com.fawez.ebankingbackend.dtos.BankAccountDTO;
import com.fawez.ebankingbackend.entities.AccountOperation;
import com.fawez.ebankingbackend.entities.BankAccount;
import com.fawez.ebankingbackend.exception.BankAccountNotFoundException;
import com.fawez.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountRestAPI {
    private final BankAccountService bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
@GetMapping("/accounts/{bankId}")
    public BankAccountDTO getBankAccount(@PathVariable("bankId") String bankId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(bankId);
}
@GetMapping("/accounts")
   public List<BankAccountDTO> getBankAccountList(){
        return  bankAccountService.bankAccountList();
    }
    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable("accountId") String accountId){
        return bankAccountService.accountHistory(accountId);
    }
    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable("accountId") String accountId,
                                               @RequestParam(name = "page",defaultValue = "0") int page ,
                                               @RequestParam(name = "size",defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }
}
