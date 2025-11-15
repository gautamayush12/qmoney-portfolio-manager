# qmoney-portfolio-manager
Java Spring Boot application to analyze client stock portfolios, fetch historical data, compute annualized returns, and visualize performance.

QMoney Portfolio Manager

QMoney Portfolio Manager is a Java backend library for analyzing and managing user stock portfolios. It fetches historical stock data from APIs, computes annualized returns, and sorts stocks based on performance. The project is designed to be modular and extensible, allowing integration of multiple data providers and advanced portfolio analytics.

Features

Read and process user trades from a JSON file.

Fetch historical stock prices from Tiingo via REST APIs (and can be extended to other providers like Alpha Vantage).

Compute annualized returns for user portfolios.

Sort stocks based on closing price.

Extensible and modular design using interfaces and the Factory Method pattern:

Separate interfaces for stock data providers.

Factory methods to create instances of different providers without changing core logic.

Enables adding new APIs or changing providers easily.

Graceful handling of errors and exceptions in stock data processing.

Technologies Used

Java 21

Spring Boot

Jackson (jackson-databind, jackson-datatype-jsr310)

REST API (RestTemplate)

Maven
