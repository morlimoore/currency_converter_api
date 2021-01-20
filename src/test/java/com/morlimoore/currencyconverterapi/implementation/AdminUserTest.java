package com.morlimoore.currencyconverterapi.implementation;

import com.morlimoore.currencyconverterapi.CurrencyconverterapiApplication;
import com.morlimoore.currencyconverterapi.DTOs.*;
import com.morlimoore.currencyconverterapi.entities.User;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.payload.JwtResponse;
import com.morlimoore.currencyconverterapi.service.AuthService;
import com.morlimoore.currencyconverterapi.service.UserService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.morlimoore.currencyconverterapi.util.RoleEnum.ROLE_ADMIN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
public class AdminUserTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    private RequestSpecification spec;
    private RequestSpecification request = given();
    private final String CONTEXT_PATH = "/api/v1";
    private String token = "";
    private String userToken = "";
    private User admin = null;
    private User user = null;

    @Value("${local.server.port}")
    private int port;

    @BeforeAll
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        //Sign in admin user and get adminToken
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("admin");
        loginRequestDTO.setPassword("01234Admin");
        ResponseEntity<ApiResponse<JwtResponse>> res = authService.authenticateUser(loginRequestDTO);
        token = res.getBody().getResult().getToken();
        admin = userService.findUserByUsername("admin");

        //Sign up noob user
        SignupRequestDTO signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setEmail("noob@gmail.com");
        signupRequestDTO.setMainCurrency("NGN");
        signupRequestDTO.setUsername("noob");
        signupRequestDTO.setPassword("password");
        given()
                .contentType("application/json")
                .body(signupRequestDTO)
                .when().post(CONTEXT_PATH + "/auth/signup");
        user = userService.findUserByUsername("noob");

        //Sign in noob user and get the details
        LoginRequestDTO loginRequestDTO2 = new LoginRequestDTO();
        loginRequestDTO2.setUsername("noob");
        loginRequestDTO2.setPassword("password");
        ResponseEntity<ApiResponse<JwtResponse>> res2 = authService.authenticateUser(loginRequestDTO2);
        userToken = res2.getBody().getResult().getToken();
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("Is user role Admin")
    public void isUserAdmin() {
        assertTrue(admin.getRole().equals(ROLE_ADMIN));
    }

    @Test
    @Order(2)
    @DisplayName("Cannot have a wallet")
    public void createWalletTest() {
        CreateWalletDTO createWalletDTO = new CreateWalletDTO();
        createWalletDTO.setCurrency("GBP");
        given(this.spec)
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createWalletDTO)
                .filter(document("Cannot have a wallet", responseFields(
                        fieldWithPath("status").type(JsonFieldType.STRING).description("The response status"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("The response message"),
                        fieldWithPath("time").type(JsonFieldType.STRING).description("The response time"),
                        fieldWithPath("result").type(JsonFieldType.STRING).description("The response result")
                )))
                .when().post(CONTEXT_PATH + "/wallet/create")
                .then().statusCode(403)
                .and()
                .body("status", equalTo("FORBIDDEN"));
    }

    @Test
    @Order(3)
    @DisplayName("Can fund wallet for any user")
    public void fundWalletTest() {
        WalletTransactionDTO walletTransactionDTO = new WalletTransactionDTO();
        walletTransactionDTO.setCurrency("GBP");
        walletTransactionDTO.setAmount(1000L);
        given(this.spec)
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(walletTransactionDTO)
                .filter(document("Can fund wallet for any user", responseFields(
                        fieldWithPath("status").type(JsonFieldType.STRING).description("The response status"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("The response message"),
                        fieldWithPath("time").type(JsonFieldType.STRING).description("The response time"),
                        fieldWithPath("result").type(JsonFieldType.STRING).description("The response result")
                )))
                .when().post(CONTEXT_PATH + "/wallet/fund")
                .then().statusCode(200)
                .and()
                .body("status", equalTo("OK"));
    }

    @Test
    @Order(4)
    @DisplayName("Can change main currency of any user")
    public void mainCurrencyChangeTest() {
        AdminActionsDTO adminActionsDTO = new AdminActionsDTO();
        adminActionsDTO.setTargetCurrency("CNY");
        given(this.spec)
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(adminActionsDTO)
                .filter(document("Can change main currency of any user", responseFields(
                        fieldWithPath("status").type(JsonFieldType.STRING).description("The response status"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("The response message"),
                        fieldWithPath("time").type(JsonFieldType.STRING).description("The response time"),
                        fieldWithPath("result").type(JsonFieldType.STRING).description("The response result")
                )))
                .when().put(CONTEXT_PATH + "/admin/wallet/currency-change/" + user.getId())
                .then().statusCode(200)
                .and()
                .body("status", equalTo("OK"));
    }

    @Test
    @Order(5)
    @DisplayName("Can approve noob wallet funding")
    public void noobWalletFundingApprovalTest() throws Exception {
        //Create funding request for noob user
        WalletTransactionDTO walletTransactionDTO = new WalletTransactionDTO();
        walletTransactionDTO.setCurrency("NGN");
        walletTransactionDTO.setAmount(1000L);
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + userToken)
                .body(walletTransactionDTO)
                .when().post(CONTEXT_PATH + "/wallet/fund");

        given(this.spec)
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .filter(document("Can approve noob wallet funding", responseFields(
                        fieldWithPath("status").type(JsonFieldType.STRING).description("The response status"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("The response message"),
                        fieldWithPath("time").type(JsonFieldType.STRING).description("The response time"),
                        fieldWithPath("result").type(JsonFieldType.STRING).description("The response result")
                )))
                .when().put(CONTEXT_PATH + "/admin/wallet/approve-funding/" + user.getId())
                .then().statusCode(200)
                .and()
                .body("status", equalTo("OK"));
    }

    @Test
    @Order(6)
    @DisplayName("Can promote or demote users")
    public void manageUsersTest() {
        given(this.spec)
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .filter(document("Can promote or demote users", responseFields(
                        fieldWithPath("status").type(JsonFieldType.STRING).description("The response status"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("The response message"),
                        fieldWithPath("time").type(JsonFieldType.STRING).description("The response time"),
                        fieldWithPath("result").type(JsonFieldType.STRING).description("The response result")
                )))
                .when().put(CONTEXT_PATH + "/admin/user/manage/promote-demote/" + user.getId())
                .then().statusCode(200)
                .and()
                .body("status", equalTo("OK"));
    }
}