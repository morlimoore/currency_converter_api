[![Currency-Converter-API Actions Status](https://github.com/morlimoore/currency_converter_api/workflows/Currency-Converter-API/badge.svg)](https://github.com/morlimoore/currency_converter_api/actions)


# Currency Converter API

This application derives its exchange rates from [currency converter api](https://www.currencyconverterapi.com/), by making real time calls to the API.

You can access this application on Heroku via this URL: `https://currency-converter-api-v1.herokuapp.com`

NOTE: I did not set up any cron-job to keep the application alive on Heroku. If the first request you send fails, please send another.

The documentation can be viewed [here](https://currency-converter-api-v1.herokuapp.com/api/v1/docs.html).

Please, be sure to replace the *localhost* stated in the URIs in the documentation, with the Heroku URL pasted above.

### Supported Currencies

This API only supports a certain list of currencies. They are:

 "ALL",
    "XCD",
    "EUR",
    "BBD",
    "BTN",
    "BND",
    "XAF",
    "CUP",
    "USD",
    "FKP",
    "GIP",
    "HUF",
    "IRR",
    "JMD",
    "AUD",
    "LAK",
    "LYD",
    "MKD",
    "XOF",
    "NZD",
    "OMR",
    "PGK",
    "RWF",
    "WST",
    "RSD",
    "SEK",
    "TZS",
    "AMD",
    "BSD",
    "BAM",
    "CVE",
    "CNY",
    "CRC",
    "CZK",
    "ERN",
    "GEL",
    "HTG",
    "INR",
    "JOD",
    "KRW",
    "LBP",
    "MWK",
    "MRO",
    "MZN",
    "ANG",
    "PEN",
    "QAR",
    "STD",
    "SLL",
    "SOS",
    "SDG",
    "SYP",
    "AOA",
    "AWG",
    "BHD",
    "BZD",
    "BWP",
    "BIF",
    "KYD",
    "COP",
    "DKK",
    "GTQ",
    "HNL",
    "IDR",
    "ILS",
    "KZT",
    "KWD",
    "LSL",
    "MYR",
    "MUR",
    "MNT",
    "MMK",
    "NGN",
    "PAB",
    "PHP",
    "RON",
    "SAR",
    "SGD",
    "ZAR",
    "SRD",
    "TWD",
    "TOP",
    "VEF",
    "DZD",
    "ARS",
    "AZN",
    "BYR",
    "BOB",
    "BGN",
    "CAD",
    "CLP",
    "CDF",
    "DOP",
    "FJD",
    "GMD",
    "GYD",
    "ISK",
    "IQD",
    "JPY",
    "KPW",
    "LVL",
    "CHF",
    "MGA",
    "MDL",
    "MAD",
    "NPR",
    "NIO",
    "PKR",
    "PYG",
    "SHP",
    "SCR",
    "SBD",
    "LKR",
    "THB",
    "TRY",
    "AED",
    "VUV",
    "YER",
    "AFN",
    "BDT",
    "BRL",
    "KHR",
    "KMF",
    "HRK",
    "DJF",
    "EGP",
    "ETB",
    "XPF",
    "GHS",
    "GNF",
    "HKD",
    "XDR",
    "KES",
    "KGS",
    "LRD",
    "MOP",
    "MVR",
    "MXN",
    "NAD",
    "NOK",
    "PLN",
    "RUB",
    "SZL",
    "TJS",
    "TTD",
    "UGX",
    "UYU",
    "VND",
    "TND",
    "UAH",
    "UZS",
    "TMT",
    "GBP",
    "ZMW",
    "BTC",
    "BYN",
    "BMD",
    "GGP",
    "CLF",
    "CUC",
    "IMP",
    "JEP",
    "SVC",
    "ZMK",
    "XAG",
    "ZWL"

### Application details

The application is already seeded with an admin user with username `admin` and password `01234Admin`

To successfully send calls to the API, be sure to for each request, include in the header, the token gotten after sign in, as authorization bearer token.

Integration Testing was done on the project using the RestAssured library, while Spring Rest Docs and Asciidocs was used for the API documentation.

Integration Testing with RestAssured was used, to test the entire logic of the application, rather than mocking the web service layer.
Combining this with Spring Rest Doc and Asciidoc, enabled me to generate documentation dynamically, based of real calls to the application.

The database used for running the application is PostgreSQL. I only used it because I am familiar with its docker set up, and I already have the image saved on my pc.

For testing purposes, I used the H2 in-memory database, because it is lightweight, and I configured it to only create and then drop the database once the application runs and stops.

Spring Security with JWT is used for the Authentication. It offers a convenient and secure way, to secure REST APIs.

The project runs on port 8080.

To run the project:

 1. Clone the project to your local machine
 
 2. Using your terminal, navigate to the root of the project directory.
 
 3. To run the project on Docker, use: **docker-compose -f docker-compose.yml up**
 
 **NOTE:** You must have internet connection for you to be able to use this app successfully.

