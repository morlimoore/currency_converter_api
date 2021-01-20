package com.morlimoore.currencyconverterapi;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class CurrencyconverterapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyconverterapiApplication.class, args);
	}

	@Value("${application.currencyApi.baseUrl}")
	private String baseUrl;

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	@Bean
	public WebClient getWebClient() {
		return WebClient.create(baseUrl);
	}
}
