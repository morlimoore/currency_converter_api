package com.morlimoore.currencyconverterapi.service;

import com.morlimoore.currencyconverterapi.payload.FixerApiResponse;

public interface FixerApiService {

    FixerApiResponse getAvailableCurrencies();
}
