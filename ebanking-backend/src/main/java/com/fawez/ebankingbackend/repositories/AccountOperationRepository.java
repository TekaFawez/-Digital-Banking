package com.fawez.ebankingbackend.repositories;

import com.fawez.ebankingbackend.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}
