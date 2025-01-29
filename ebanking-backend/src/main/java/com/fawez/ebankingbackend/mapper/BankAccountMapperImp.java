package com.fawez.ebankingbackend.mapper;

import com.fawez.ebankingbackend.dtos.CustomerDTO;
import com.fawez.ebankingbackend.entities.Customer;
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

}
