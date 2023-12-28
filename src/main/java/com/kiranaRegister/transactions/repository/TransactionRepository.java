package com.kiranaRegister.transactions.repository;

import com.kiranaRegister.transactions.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);


}
