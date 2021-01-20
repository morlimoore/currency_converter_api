package com.morlimoore.currencyconverterapi.security;

import com.morlimoore.currencyconverterapi.CurrencyconverterapiApplication;
import com.morlimoore.currencyconverterapi.DTOs.LoginRequestDTO;
import com.morlimoore.currencyconverterapi.DTOs.SignupRequestDTO;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CurrencyconverterapiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
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

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
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

        given(this.spec)
                .accept("application/json")
                .contentType("application/json")
                .body(signupRequestDTO)
                .filter(document("User can sign up", responseFields(
                        fieldWithPath("status").type(JsonFieldType.STRING).description("The response status"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("The response message"),
                        fieldWithPath("time").type(JsonFieldType.STRING).description("The response time"),
                        fieldWithPath("result").type(JsonFieldType.STRING).description("The response result")
                )))
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

        given(this.spec)
                .accept("application/json")
                .contentType("application/json")
                .body(loginRequestDTO)
                .filter(document("User can sign in", responseFields(
                        fieldWithPath("status").type(JsonFieldType.STRING).description("The response status"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("The response message"),
                        fieldWithPath("time").type(JsonFieldType.STRING).description("The response time"),
                        fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("The user id"),
                        fieldWithPath("result.username").type(JsonFieldType.STRING).description("The username"),
                        fieldWithPath("result.email").type(JsonFieldType.STRING).description("The user email"),
                        fieldWithPath("result.role").type(JsonFieldType.STRING).description("The user role"),
                        fieldWithPath("result.type").type(JsonFieldType.STRING).description("The authentication type"),
                        fieldWithPath("result.token").type(JsonFieldType.STRING).description("The authentication token")
                )))
                .when().post(CONTEXT_PATH + "/signin")
                .then().statusCode(200)
                .and()
                .body("status", equalTo("OK"))
                .body("message", equalTo("SUCCESS"));
    }
}
