[![Currency-Converter-API Actions Status](https://github.com/morlimoore/currency_converter_api/workflows/Currency-Converter-API/badge.svg)](https://github.com/morlimoore/currency_converter_api/actions)


Integration Testing was done on the project using the RestAssured library.

API documentation was done using Spring Rest Docs and Asciidocs.
You can view the documentation [here](http://localhost:63342/currencyconverterapi/target/generated-docs/currencyconverterapi.html?_ijt=7v6pa6npogi0a42hmvcpakhnln).

Integration Testing with RestAssured was used, so as to test the entire logic of the application, rather than mocking the web service layer.
Combining this with Spring Rest Doc and Asciidoc, enabled me to generate documentation dynamically, based of real calls the application.

The database used for running the application is PostgreSQL. I only used it because I am familiar with its docker set up, and I already have the image saved on my pc.

For testing purposes, H2 in-memory database is used, because it is lightweight and I configured it to only create and then drop the database once the application is run and stopped.

Spring Security with JWT is used for the Authentication. It offers a convenient and secure way, to secure REST APIs.

The project is configured to run on port 8080.

To run the project:

 1. Clone the project to your local machine
 
 2. Using your terminal, navigate to the root of the project directory.
 
 3. To run the project on Docker, use: **docker-compose -f docker-compose.yml up**

