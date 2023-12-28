package com.kiranaRegister.transactions.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.kiranaRegister.transactions.dto.TransactionRequest;
import com.kiranaRegister.transactions.entity.Transaction;


public interface TransactionService {


   void recordTransactions(TransactionRequest transactionRequest);
   CompletableFuture<List<Transaction>> getTransactionsGroupedByDate();
}
