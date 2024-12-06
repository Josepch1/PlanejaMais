# PlanejaMais 📊💰

Welcome to PlanejaMais, a financial management application designed to help you keep track of your financial records efficiently and effectively.

## Features ✨

- **Create Financial Records**: Easily add new financial records with descriptions, types, and values.
- **Update Records**: Modify existing records to keep your financial data up-to-date.
- **Delete Records**: Remove records that are no longer needed.
- **View All Records**: Get a comprehensive list of all your financial records.
- **Financial Summary**: View a summary of your total income, expenses, and balance.

## Technologies Used 🛠️

- **Java**: Core programming language.
- **Spring Boot**: Framework for building the application.
- **Spring Data JPA**: For database interactions.
- **Lombok**: To reduce boilerplate code.
- **Maven**: Build and dependency management tool.

## Getting Started 🚀

### Prerequisites

- Java 23 or higher
- Maven 3.4.0 or higher

### Installation

1. Clone the repository:
  ```sh
  git clone https://github.com/yourusername/PlanejaMais.git
  ```
2. Navigate to the project directory:
  ```sh
  cd PlanejaMais
  ```
3. Build the project:
  ```sh
  ./mvnw clean install
  ```
4. Run the application:
  ```sh
  ./mvnw spring-boot:run
  ```

## Usage 📘

### API Endpoints

- **GET /financial-record**: Retrieve all financial records.
- **POST /financial-record**: Create a new financial record.
- **PUT /financial-record/{id}**: Update an existing financial record.
- **DELETE /financial-record/{id}**: Delete a financial record by ID.
- **GET /financial-record/summary**: Get a summary of financial records.

## Contributing 🤝

Contributions are welcome! Please fork the repository and create a pull request with your changes.

Happy budgeting! 💸