package com.morlimoore.currencyconverterapi.service.impl;

import com.morlimoore.currencyconverterapi.payload.CurrencyApiResponse;
import com.morlimoore.currencyconverterapi.service.CurrencyApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
public class CurrencyApiServiceImpl implements CurrencyApiService {

    @Autowired
    WebClient webClient;

    @Value("${application.currencyApi.accessKey}")
    private String accessKey;

    @Override
    public CurrencyApiResponse getAvailableCurrencies() {
        return webClient
                .get()
                .uri("/currencies?apiKey=" + accessKey)
                .retrieve()
                .bodyToMono(CurrencyApiResponse.class)
                .block(Duration.ofSeconds(15));
    }

    @Override
    public Double getConversionRate(String baseCurrency, String givenCurrency) {
        String res = webClient
                .get()
                .uri("/convert?q=" + givenCurrency + "_" + baseCurrency + "&compact=ultra&apiKey=" + accessKey)
                .retrieve()
                .bodyToMono(Object.class)
                .block(Duration.ofSeconds(15)).toString();
        Double answer = Double.parseDouble(res.substring(res.indexOf("=") + 1, res.length() - 1));
        return answer;
    }
}
