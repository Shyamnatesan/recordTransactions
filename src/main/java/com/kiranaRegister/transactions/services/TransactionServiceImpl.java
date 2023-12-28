package com.kiranaRegister.transactions.services;


import com.kiranaRegister.transactions.dto.ExchangeRateResponse;
import com.kiranaRegister.transactions.dto.TransactionRequest;
import com.kiranaRegister.transactions.entity.Transaction;
import com.kiranaRegister.transactions.helpers.RestTemplateSingleton;
import com.kiranaRegister.transactions.repository.TransactionRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
public class TransactionServiceImpl implements TransactionService{

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private RestTemplateSingleton restTemplateSingleton;

    private static final String EXCHANGE_RATE_API_URL = "https://api.fxratesapi.com/latest";
    private static final String IND_CURRENCY = "INR";
    private static final String USD_CURRENCY = "USD";





    private Double checkCurrency(String currency, Double amount){
        if (currency.equals(IND_CURRENCY) || currency.equals(USD_CURRENCY)) {
            return amount;
        } else {
            Double exchangeRate = getExchangeRate(currency);
            return amount / exchangeRate;
        }
    }



    private Double getExchangeRate(String currency){
        ExchangeRateResponse rateResponse;
        try{
            rateResponse = restTemplateSingleton.getInstance().getForObject(EXCHANGE_RATE_API_URL, ExchangeRateResponse.class);
        }catch (Exception e){
            throw new RuntimeException("Failed to fetch exchange rate");
        }

        if(rateResponse != null){
            Double rate = rateResponse.getRates().get(currency);
            String conversion = rateResponse.getBase();
            Double conversionBase = rateResponse.getRates().get(conversion);
            return rate / conversionBase;
        }else{
            throw new RuntimeException("Invalid response or exchange rate API request was not successful");
        }
    }


    @Override
    @Async
    public void recordTransactions(TransactionRequest transactionRequest) {
        String currentThread = Thread.currentThread().getName();
        System.out.println(currentThread + " in recordTransactions");
        String currency = transactionRequest.getCurrency();
        Double amountToSave = checkCurrency(currency, transactionRequest.getAmount());

        transactionRequest.setAmount(amountToSave);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionRequest.getTransactionType());
        transaction.setTransactionDate(transactionRequest.getTransactionDate());
        transaction.setAmount(amountToSave);
        if(!currency.equals(IND_CURRENCY)){
            transaction.setCurrency(USD_CURRENCY);
        }else{
            transaction.setCurrency(IND_CURRENCY);
        }

        try{
            transactionRepository.save(transaction);
        }catch (Exception e){
            throw new RuntimeException("Error saving transaction to the database: " + e.getMessage());
        }
    }


    @Override
    public CompletableFuture<List<Transaction>> getTransactionsGroupedByDate() {
        logger.info("Recorded transaction for request ID: {} in thread: {}", Thread.currentThread().getName());
        System.out.println("hello");
        LocalDate date = LocalDate.now();
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.plusDays(1).atStartOfDay();
        List<Transaction> result = transactionRepository.findByTransactionDateBetween(startDate, endDate);

        return CompletableFuture.completedFuture(result);

    }

}






