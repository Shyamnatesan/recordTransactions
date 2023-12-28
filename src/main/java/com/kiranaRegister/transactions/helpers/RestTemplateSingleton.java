package com.kiranaRegister.transactions.helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateSingleton {
    
    private static final RestTemplate restTemplate = new RestTemplate();

    private RestTemplateSingleton() {}

    public static RestTemplate getInstance() {
    return restTemplate;
    }
}
