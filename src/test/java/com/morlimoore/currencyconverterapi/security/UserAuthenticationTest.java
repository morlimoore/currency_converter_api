package com.morlimoore.currencyconverterapi.security;

import com.morlimoore.currencyconverterapi.CurrencyconverterapiApplication;
import com.morlimoore.currencyconverterapi.DTOs.LoginRequestDTO;
import com.morlimoore.currencyconverterapi.DTOs.SignupRequestDTO;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CurrencyconverterapiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserAuthenticationTest {

    private RequestSpecification spec;
    private RequestSpecification request = given();
    private final String CONTEXT_PATH = "api/v1/auth";

    @Value("${local.server.port}")
    private int port;

    @BeforeAll
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @Order(1)
    @DisplayName("Can ping application")
    public void basicPingTest() {
        given()
                .contentType("application/json")
                .when().post(CONTEXT_PATH + "/signup")
                .then().statusCode(400);
    }

    @Test
    @Order(2)
    @DisplayName("User can sign up")
    public void signupUserTest() {
        SignupRequestDTO signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setEmail("test@gmail.com");
        signupRequestDTO.setMainCurrency("NGN");
        signupRequestDTO.setUsername("test");
        signupRequestDTO.setPassword("password");

        given()
                .contentType("application/json")
                .body(signupRequestDTO)
                .when().post(CONTEXT_PATH + "/signup")
                .then().statusCode(201)
                .and()
                .body("status", equalTo("CREATED"))
                .body("message", equalTo("SUCCESS"));
    }

    @Test
    @Order(3)
    @DisplayName("User can sign in")
    public void signInUserTest() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("test");
        loginRequestDTO.setPassword("password");

        given()
                .contentType("application/json")
                .body(loginRequestDTO)
                .when().post(CONTEXT_PATH + "/signin")
                .then().statusCode(200)
                .and()
                .body("status", equalTo("OK"))
                .body("message", equalTo("SUCCESS"));
    }
}
