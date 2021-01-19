package com.morlimoore.currencyconverterapi.implementation;

import com.morlimoore.currencyconverterapi.CurrencyconverterapiApplication;
import com.morlimoore.currencyconverterapi.DTOs.*;
import com.morlimoore.currencyconverterapi.entities.User;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.payload.JwtResponse;
import com.morlimoore.currencyconverterapi.service.AuthService;
import com.morlimoore.currencyconverterapi.service.UserService;
import com.morlimoore.currencyconverterapi.service.WalletService;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static com.morlimoore.currencyconverterapi.util.RoleEnum.ROLE_ELITE;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CurrencyconverterapiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EliteUserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private WalletService walletService;

    private RequestSpecification request = given();
    private final String CONTEXT_PATH = "/api/v1";
    private String token = "";
    private User user = null;

    @Value("${local.server.port}")
    private int port;

    @BeforeAll
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        //Sign up user
        SignupRequestDTO signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setEmail("elite@gmail.com");
        signupRequestDTO.setMainCurrency("NGN");
        signupRequestDTO.setUsername("elite");
        signupRequestDTO.setPassword("password");
        given()
                .contentType("application/json")
                .body(signupRequestDTO)
                .when().post(CONTEXT_PATH + "/auth/signup");

        user = userService.findUserByUsername("elite");

        //Sign in admin user and get adminToken
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("admin");
        loginRequestDTO.setPassword("01234Admin");
        ResponseEntity<ApiResponse<JwtResponse>> res = authService.authenticateUser(loginRequestDTO);
        String adminToken = res.getBody().getResult().getToken();

        //Promote user to elite
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .when().put(CONTEXT_PATH + "/admin/user/manage/promote-demote/" + user.getId());

        //Update user object after role change
        user = userService.findUserByUsername("elite");

        //Sign in elite user and get the details
        LoginRequestDTO loginRequestDTO2 = new LoginRequestDTO();
        loginRequestDTO2.setUsername("elite");
        loginRequestDTO2.setPassword("password");
        ResponseEntity<ApiResponse<JwtResponse>> res2 = authService.authenticateUser(loginRequestDTO2);
        token = res2.getBody().getResult().getToken();
    }

    @Test
    @Order(1)
    @DisplayName("Is user role Elite")
    public void isUserElite() {
        assertTrue(user.getRole().equals(ROLE_ELITE));
    }

    @Test
    @Order(2)
    @DisplayName("Can have multiple wallets in different currencies")
    public void multipleWalletsAndCurrenciesTest() {
        CreateWalletDTO createWalletDTO = new CreateWalletDTO();
        createWalletDTO.setCurrency("GBP");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createWalletDTO)
                .when().post(CONTEXT_PATH + "/wallet/create")
                .then().statusCode(201)
                .and()
                .body("status", equalTo("CREATED"))
                .body("message", equalTo("SUCCESS"));
    }

    @Test
    @Order(3)
    @DisplayName("Funding should update or create wallet")
    public void updateOrCreateWalletTest() {
        WalletTransactionDTO walletTransactionDTO = new WalletTransactionDTO();
        walletTransactionDTO.setCurrency("CNY");
        walletTransactionDTO.setAmount(1000L);
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(walletTransactionDTO)
                .when().post(CONTEXT_PATH + "/wallet/fund")
                .then().statusCode(201)
                .and()
                .body("status", equalTo("CREATED"));
    }

    @Test
    @Order(4)
    @DisplayName("Withdrawal should reduce wallet balance")
    public void walletWithdrawalTest() {
        Long initialBalance = walletService.getWalletInCurrency("CNY", user.getId()).getAmount();
        WalletTransactionDTO walletTransactionDTO = new WalletTransactionDTO();
        walletTransactionDTO.setCurrency("CNY");
        walletTransactionDTO.setAmount(100L);
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(walletTransactionDTO)
                .when().post(CONTEXT_PATH + "/wallet/withdraw")
                .then().statusCode(200);
        Long finalBalance = walletService.getWalletInCurrency("CNY", user.getId()).getAmount();
        assertTrue(initialBalance > finalBalance);
    }

    @Test
    @Order(5)
    @DisplayName("Wallet-less currency withdrawal is converted to main currency")
    public void walletLessWithdrawalTest() {
        WalletTransactionDTO walletTransactionDTO = new WalletTransactionDTO();
        walletTransactionDTO.setCurrency("XCD");
        walletTransactionDTO.setAmount(10000L);
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(walletTransactionDTO)
                .when().post(CONTEXT_PATH + "/wallet/withdraw")
                .then().statusCode(400)
                .and()
                .body("status", equalTo("BAD_REQUEST"))
                .body("message", equalTo("ERROR"));
    }

    @Test
    @Order(6)
    @DisplayName("Cannot change main currency")
    public void mainCurrencyChangeTest() {
        AdminActionsDTO adminActionsDTO = new AdminActionsDTO();
        adminActionsDTO.setTargetCurrency("CNY");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(adminActionsDTO)
                .when().put(CONTEXT_PATH + "/admin/wallet/currency-change/" + user.getId())
                .then().statusCode(403)
                .and()
                .body("status", equalTo("FORBIDDEN"))
                .body("message", equalTo("ERROR"))
                .body("result", equalTo("Sorry, you are not authorised to access this resource."));
    }
}