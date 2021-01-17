package com.morlimoore.currencyconverterapi.service;

import com.morlimoore.currencyconverterapi.payload.CurrencyApiResponse;

public interface CurrencyApiService {

    CurrencyApiResponse getAvailableCurrencies();

    Double getConversionRate(String baseCurrency, String givenCurrency);
}