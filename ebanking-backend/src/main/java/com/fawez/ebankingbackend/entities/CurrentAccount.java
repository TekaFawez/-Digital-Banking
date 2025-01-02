package com.fawez.ebankingbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@DiscriminatorValue("CA")
@NoArgsConstructor
@AllArgsConstructor
public class CurrentAccount extends BankAccount{
    private double overDraft;
}
