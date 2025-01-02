package com.fawez.ebankingbackend.repositories;

import com.fawez.ebankingbackend.entities.BankAccount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
