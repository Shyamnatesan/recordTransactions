package com.kiranaRegister.transactions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class ExchangeRateResponse {
    @JsonProperty("base")
    private String base;
    @JsonProperty("rates")
    private Map<String, Double> rates;
    public String getBase(){
        return base;
    }
    public Map<String, Double> getRates() {
        return rates;
    }

}
