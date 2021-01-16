package com.morlimoore.currencyconverterapi.service.impl;

import com.morlimoore.currencyconverterapi.payload.FixerApiResponse;
import com.morlimoore.currencyconverterapi.service.FixerApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
public class FixerApiServiceImpl implements FixerApiService {

    @Autowired
    WebClient webClient;

    @Value("${application.fixerApi.accessKey}")
    private String accessKey;

    @Override
    public FixerApiResponse getAvailableCurrencies() {
        return webClient
                .get()
                .uri("/symbols?access_key=" + accessKey)
                .retrieve()
                .bodyToMono(FixerApiResponse.class)
                .block(Duration.ofSeconds(15));
    }
}
