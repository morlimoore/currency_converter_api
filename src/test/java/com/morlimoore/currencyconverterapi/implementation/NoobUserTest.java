package com.morlimoore.currencyconverterapi.implementation;

import com.morlimoore.currencyconverterapi.CurrencyconverterapiApplication;
import com.morlimoore.currencyconverterapi.DTOs.*;
import com.morlimoore.currencyconverterapi.entities.User;
import com.morlimoore.currencyconverterapi.entities.Wallet;
import com.morlimoore.currencyconverterapi.entities.WalletFunding;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.payload.JwtResponse;
import com.morlimoore.currencyconverterapi.repositories.WalletFundingRepository;
import com.morlimoore.currencyconverterapi.service.AuthService;
import com.morlimoore.currencyconverterapi.service.UserService;
import com.morlimoore.currencyconverterapi.service.WalletService;
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

import static com.morlimoore.currencyconverterapi.util.RoleEnum.ROLE_NOOB;
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
public class NoobUserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletFundingRepository walletFundingRepository;

    private RequestSpecification spec;
    private RequestSpecification request = given();
    private final String CONTEXT_PATH = "/api/v1";
    private String token = "";
    private String mainCurrency = "";
    private User user = null;
    private Wallet mainWallet = null;

    @Value("${local.server.port}")
    private int port;

    @BeforeAll
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        //Sign up user
        SignupRequestDTO signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setEmail("noob2@gmail.com");
        signupRequestDTO.setMainCurrency("NGN");
        signupRequestDTO.setUsername("noob2");
        signupRequestDTO.setPassword("password");
        given()
                .contentType("application/json")
                .body(signupRequestDTO)
                .when().post(CONTEXT_PATH + "/auth/signup");

        //Sign in noob user and get the details
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("noob2");
        loginRequestDTO.setPassword("password");
        ResponseEntity<ApiResponse<JwtResponse>> res = authService.authenticateUser(loginRequestDTO);
        token = res.getBody().getResult().getToken();
        Long userId = res.getBody().getResult().getId();
        mainWallet = walletService.getUserMainWallet(userId);
        mainCurrency = mainWallet.getCurrency();

        user = userService.findUserByUsername("noob2");
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("Is user a noob")
    public void signupNoobUserTest() {
        assertTrue(user.getRole().equals(ROLE_NOOB));
    }

    @Test
    @Order(2)
    @DisplayName("Can only have a wallet in a single currency")
    public void createWalletTest() {
        CreateWalletDTO createWalletDTO = new CreateWalletDTO();
        createWalletDTO.setCurrency("GBP");
        given(this.spec)
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createWalletDTO)
                .filter(document("Can only have a wallet in a single currency", responseFields(
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
    @DisplayName("All wallet funding is converted to the main currency")
    public void walletFundingCurrencyTest() {
        WalletTransactionDTO walletTransactionDTO = new WalletTransactionDTO();
        walletTransactionDTO.setCurrency("GBP");
        walletTransactionDTO.setAmount(1000L);
        given(this.spec)
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(walletTransactionDTO)
                .filter(document("All wallet funding is converted to the main currency", responseFields(
                        fieldWithPath("status").type(JsonFieldType.STRING).description("The response status"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("The response message"),
                        fieldWithPath("time").type(JsonFieldType.STRING).description("The response time"),
                        fieldWithPath("result").type(JsonFieldType.STRING).description("The response result")
                )))
                .when().post(CONTEXT_PATH + "/wallet/fund");
        WalletFunding walletFunding = walletFundingRepository.findWalletFundingByUserEqualsAndIsApprovedFalse(user);
        assertTrue(walletFunding.getWallet().getCurrency().equals(mainCurrency));
    }

    @Test
    @Order(4)
    @DisplayName("All wallet withdrawal is converted to the main currency")
    public void walletWithdrawalCurrencyTest() {
        WalletTransactionDTO walletTransactionDTO = new WalletTransactionDTO();
        walletTransactionDTO.setCurrency("CNY");
        walletTransactionDTO.setAmount(100L);
        given(this.spec)
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(walletTransactionDTO)
                .filter(document("All wallet withdrawal is converted to the main currency", responseFields(
                        fieldWithPath("status").type(JsonFieldType.STRING).description("The response status"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("The response message"),
                        fieldWithPath("time").type(JsonFieldType.STRING).description("The response time"),
                        fieldWithPath("result").type(JsonFieldType.STRING).description("The response result")
                )))
                .when().post(CONTEXT_PATH + "/wallet/withdraw")
                .then().statusCode(400)
                .and()
                .body("status", equalTo("BAD_REQUEST"))
                .body("message", equalTo("ERROR"))
                .body("result", equalTo("Sorry, you have insufficient balance to withdraw"));

    }

    @Test
    @Order(5)
    @DisplayName("All wallet funding needs admin approval")
    public void walletFundingApprovalTest() {
        WalletTransactionDTO walletTransactionDTO = new WalletTransactionDTO();
        walletTransactionDTO.setCurrency("GBP");
        walletTransactionDTO.setAmount(1000L);
        given(this.spec)
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(walletTransactionDTO)
                .filter(document("All wallet funding needs admin approval", responseFields(
                        fieldWithPath("status").type(JsonFieldType.STRING).description("The response status"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("The response message"),
                        fieldWithPath("time").type(JsonFieldType.STRING).description("The response time"),
                        fieldWithPath("result").type(JsonFieldType.STRING).description("The response result")
                )))
                .when().post(CONTEXT_PATH + "/wallet/fund")
                .then().statusCode(200)
                .and()
                .body("status", equalTo("OK"))
                .body("message", equalTo("SUCCESS"));
    }

    @Test
    @Order(6)
    @DisplayName("Cannot change main currency")
    public void mainCurrencyChangeTest() {
        AdminActionsDTO adminActionsDTO = new AdminActionsDTO();
        adminActionsDTO.setTargetCurrency("CNY");
        given(this.spec)
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(adminActionsDTO)
                .filter(document("Cannot change main currency", responseFields(
                        fieldWithPath("status").type(JsonFieldType.STRING).description("The response status"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("The response message"),
                        fieldWithPath("time").type(JsonFieldType.STRING).description("The response time"),
                        fieldWithPath("result").type(JsonFieldType.STRING).description("The response result")
                )))
                .when().put(CONTEXT_PATH + "/admin/wallet/currency-change/" + user.getId())
                .then().statusCode(403)
                .and()
                .body("status", equalTo("FORBIDDEN"))
                .body("message", equalTo("ERROR"))
                .body("result", equalTo("Sorry, you are not authorised to access this resource."));

    }
}
