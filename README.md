🚀 QMoney Portfolio Manager

QMoney Portfolio Manager is a Java backend library for analyzing and managing user stock portfolios. It fetches historical stock data from APIs, computes annualized returns, and sorts stocks based on performance. Built with modular and extensible design, it makes adding new stock data providers seamless!

✨ Features

📁 Read User Trades from JSON:

Deserialize JSON files containing historical trades.

Extract stock symbols and trade details for processing.

📊 Fetch Historical Stock Prices via REST APIs:

Tiingo API integration.

Easily extendable to other providers like Alpha Vantage.

📈 Compute Annualized Returns:

Calculate portfolio performance over time using accurate financial formulas.

🔢 Sort Stocks by Closing Price:

Get ascending or descending order of stock performance for analysis.

🏗 Factory Method Design Pattern:

Core logic interacts with interfaces, not concrete implementations.

Swap or add new stock data providers without changing main code.

Encourages scalability and maintainability.

💾 Data Serialization & Deserialization:

Handle JSON efficiently with Jackson.

Support for Java 8 LocalDate and other date/time formats.

⚠ Error Handling & Robustness:

Gracefully handle missing or malformed data.

Handle API failures, rate-limits, and invalid responses.

🔄 Refactoring & Modularization:

Code is organized in packages and interfaces for clean architecture.

Extensible to new features or analytics calculations.

📚 Portfolio Analysis Ready for Frontend Integration:

All calculations and data structures are prepared for visualization in frontend apps.

🛠 Technologies Used

Java 21

Spring Boot

Jackson (JSON parsing)

REST API (RestTemplate)

Maven