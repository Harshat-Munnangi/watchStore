# E-Commerce API

This project is a simplified e-commerce API with a single endpoint that performs a checkout action for a list of watches.

## Table of Contents
- [Features](#features)
- [Requirements](#requirements)
- [Setup](#setup)
- [Endpoint](#endpoint)
- [Sample Request](#sample-request)
- [Sample Response](#sample-response)
- [Exception Handling](#exception-handling)
- [Testing](#testing)

## Features

- Checkout endpoint for calculating the total cost of a list of watches.
- Discount logic for certain watches.
- Exception handling for common scenarios.

## Requirements

- Java 17
- Gradle

## Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/Harshat-Munnangi/watchStore.git
   ```

2. Build the project:

   ```bash
   cd watchStore
   ./gradlew build
   ```

3. Run the application:

   ```bash
   java -jar build/libs/watch-0.0.1.jar
   ```

   The API will be accessible at `http://localhost:8080`.

## Endpoint

### Checkout

- **Method:** POST
- **URL:** http://localhost:8080/checkout
- **Headers:**
    - Accept: application/json
    - Content-Type: application/json
- **Request Body:**
    - List of watch IDs
- **Response:**
    - JSON with the total price

## Sample Request

```json
POST http://localhost:8080/checkout
Headers:
  Accept: application/json
  Content-Type: application/json
Body:
  ["001", "002", "001", "004", "003"]
```
## Sample Request (cURL)

You can use `curl` to make requests to the API from the command line. Here's an example:

```bash
curl -X POST http://localhost:8080/checkout \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  -d '["001", "002", "001", "004", "003"]'
```

## Sample Response

```json
HTTP Status: 200 OK
Headers:
  Content-Type: application/json
Body:
  {"price": 360}
```

## Exception Handling

- **400 Bad Request:** MethodArgumentNotValidException
- **404 Not Found:** WatchNotFoundException
- **500 Internal Server Error:** CheckoutCalculationException and other unexpected errors

## Testing

To run tests, use the following command:

```bash
./gradlew test
```