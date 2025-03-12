package com.fawez.ebankingbackend.repositories;

import com.fawez.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(exported = false)
public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
