package com.raksit.example.loyalty.transaction.repository;

import com.raksit.example.loyalty.transaction.entity.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, UUID> {

}
