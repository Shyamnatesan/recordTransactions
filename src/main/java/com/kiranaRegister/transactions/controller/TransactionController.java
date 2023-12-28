package com.kiranaRegister.transactions.controller;


import com.kiranaRegister.transactions.dto.TransactionRequest;
import com.kiranaRegister.transactions.entity.Transaction;
import com.kiranaRegister.transactions.services.TransactionService;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

import java.util.concurrent.CompletableFuture;



@RestController
@RequestMapping("/kirana")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/recordTransaction")
    public ResponseEntity<String> recordTransaction(@RequestBody TransactionRequest transactionRequest){
        String threadName = Thread.currentThread().getName();
        transactionService.recordTransactions(transactionRequest);
        return new ResponseEntity<>("Transaction processing started asynchronously.", HttpStatus.OK);
    }

    @GetMapping("/getTransactions")
    public CompletableFuture<ResponseEntity<List<Transaction>>> getTransactions() {
        CompletableFuture<List<Transaction>> transactionsFuture = transactionService.getTransactionsGroupedByDate();
        System.out.println(Thread.currentThread().getName() + " offloaded");
        
        return transactionsFuture.thenApply(transactions -> 
            new ResponseEntity<>(transactions, HttpStatus.OK))
            .exceptionally(ex -> 
            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        
    }
}
    

