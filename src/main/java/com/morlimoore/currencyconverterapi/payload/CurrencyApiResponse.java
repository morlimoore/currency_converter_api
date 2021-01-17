package com.morlimoore.currencyconverterapi.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CurrencyApiResponse {

    private Map<String, Map<String, String>> results;
}
