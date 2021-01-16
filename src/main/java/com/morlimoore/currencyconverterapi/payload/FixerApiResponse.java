package com.morlimoore.currencyconverterapi.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class FixerApiResponse {

    private Boolean success;
    private Map<String, String> symbols;
}
