# PayInsight — Smart Payment & Transaction Management System

## 1. Project Overview

**PayInsight** is a Smart Payment and Transaction Management System developed using Java, XML, XSD, XPath, and SOAP Web Services.

The project aims to model and manage payment data, validate its structure, retrieve useful information through XPath queries, and expose payment operations through a SOAP Web Service.

This project integrates two practical assignments (TP1 and TP2) into one coherent application based on the FinTech domain.

## 2. Project Objectives

The main objectives are:

* Model a real-world payment system using XML.
* Define an XML structure using an XSD schema.
* Validate XML documents against an XSD schema.
* Retrieve payment information using XPath expressions.
* Develop a Java application using Maven.
* Create and publish a SOAP Web Service.
* Retrieve transaction information through SOAP operations.
* Test the application using JUnit and SoapUI.

## 3. Technologies Used

| Technology    | Purpose                           |
| ------------- | --------------------------------- |
| Java          | Main programming language         |
| XML           | Payment data representation       |
| XSD           | XML structure and validation      |
| XPath         | Querying XML data                 |
| JAXP          | XML parsing and validation        |
| Maven         | Project and dependency management |
| SOAP          | Web Service communication         |
| JAX-WS        | SOAP Web Service implementation   |
| JAXB          | Java/XML data binding             |
| JUnit 4       | Unit testing                      |
| SoapUI        | SOAP service testing              |
| IntelliJ IDEA | Development environment           |
| Git & GitHub  | Version control                   |

## 4. Project Structure

```text
PayInsight/
│
├── data/
│   ├── payments.xml
│   └── payments.xsd
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── payinsight/
│   │               ├── Main.java
│   │               │
│   │               ├── data/
│   │               │   ├── Transaction.java
│   │               │   └── PaymentDataService.java
│   │               │
│   │               ├── service/
│   │               │   ├── IPaymentService.java
│   │               │   └── PaymentService.java
│   │               │
│   │               └── server/
│   │                   └── PayInsightServer.java
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── payinsight/
│                   └── PayInsightTest.java
│
├── images/
│   ├── validation-success.png
│   ├── xpath-results.png
│   ├── validation-error.png
│   ├── junit-tests.png
│   ├── soap-get-transaction.png
│   ├── soap-approved-transactions.png
│   ├── soap-transactions-above-amount.png
│   └── soap-transaction-count.png
│
├── pom.xml
├── README.md
└── .gitignore
```

## 5. XML Data Modeling

The project uses XML to represent a payment management system.

The main XML document is `payments.xml`.

It contains four main data categories:

### 5.1 Customers

Each customer has:

* Customer ID
* Name

### 5.2 Merchants

Each merchant has:

* Merchant ID
* Name
* Category

### 5.3 Payment Terminals

Each terminal has:

* Terminal ID
* Merchant reference
* Location
* Status

### 5.4 Transactions

Each transaction contains:

* Transaction ID
* Customer ID
* Merchant ID
* Terminal ID
* Amount
* Currency
* Date
* Status

The XML document also contains payment error codes and their descriptions.

### Sample Payment Data

| Transaction ID | Customer | Merchant | Amount | Currency | Status   |
| -------------- | -------- | -------- | -----: | -------- | -------- |
| TX001          | C001     | M001     | 250.00 | TND      | approved |
| TX002          | C002     | M002     | 120.00 | TND      | declined |
| TX003          | C001     | M001     |  75.50 | TND      | approved |

## 6. XML Schema (XSD)

The file `payments.xsd` defines the structure and rules of the XML document.

It ensures that:

* Required elements are present.
* Data types are respected.
* IDs are unique.
* ID references correspond to existing elements.
* Transaction statuses use permitted values.
* Terminal statuses follow the defined restrictions.

The schema uses:

* `xs:element`
* `xs:complexType`
* `xs:simpleType`
* `xs:attribute`
* `xs:ID`
* `xs:IDREF`
* Enumerations

## 7. XML Validation

The Java application validates `payments.xml` against `payments.xsd` using the Java XML API (JAXP).

The validation process checks whether the XML document respects the structure and constraints defined in the XSD schema.

If the XML document is valid, the application displays a successful validation message.

If the XML document contains an invalid value or violates the schema, validation reports an error.

### Validation Results

![Successful XML Validation](images/validation-success.png)

### Validation Error Example

![XML Validation Error](images/validation-error.png)

## 8. XPath Queries

XPath is used to retrieve specific information from the XML document without manually navigating through all its elements.

The project implements the following queries:

### Query 1: Retrieve all merchant names

```xpath
//merchant/name
```

### Query 2: Retrieve IDs of active terminals

```xpath
//terminal[@status='active']/@id
```

### Query 3: Retrieve approved transaction IDs

```xpath
//transaction[@status='approved']/@id
```

### Query 4: Retrieve transactions with an amount greater than 100 TND

```xpath
//transaction[amount > 100]
```

### Query 5: Retrieve declined transaction IDs

```xpath
//transaction[@status='declined']/@id
```

### Query 6: Retrieve the description of error code 05

```xpath
//error[@code='05']/description
```

### XPath Execution Results

![XPath Query Results](images/xpath-results.png)

## 9. SOAP Web Service Integration

The second practical assignment extends PayInsight by introducing a SOAP Web Service.

Instead of creating an unrelated calculator service, the SOAP functionality is integrated into the existing payment management system.

The Web Service allows external applications to retrieve payment transaction information.

### 9.1 Service Architecture

The implementation uses the following components:

**Transaction.java**

A Java class representing a payment transaction. It uses JAXB annotations to support XML data binding.

**PaymentDataService.java**

Responsible for reading payment information from `payments.xml`, validating the XML document, and retrieving transaction data.

**IPaymentService.java**

Defines the SOAP Web Service operations using JAX-WS annotations.

**PaymentService.java**

Implements the service interface and contains the business logic for the payment operations.

**PayInsightServer.java**

Publishes the SOAP Web Service using `Endpoint.publish()`.

### 9.2 Available SOAP Operations

The service provides four operations:

| Operation                    | Description                                     |
| ---------------------------- | ----------------------------------------------- |
| `getTransaction`             | Retrieves a transaction using its ID            |
| `getApprovedTransactions`    | Retrieves all approved transactions             |
| `getTransactionsAboveAmount` | Retrieves transactions above a specified amount |
| `getTransactionCount`        | Returns the total number of transactions        |

### 9.3 SOAP Endpoint

The service is published locally at:

```text
http://localhost:8086/PayInsight/PaymentService
```

The WSDL description is available at:

```text
http://localhost:8086/PayInsight/PaymentService?wsdl
```

The WSDL (Web Services Description Language) describes the available operations, parameters, and service endpoint.

### 9.4 SOAP Testing with SoapUI

The service was tested using SoapUI.

**Test 1: Retrieve a Transaction**

Input:

```text
transactionId = TX001
```

Expected result: transaction TX001 with its customer, merchant, amount, currency, and status.

![SOAP Get Transaction](images/soap-get-transaction.png)

**Test 2: Retrieve Approved Transactions**

Expected result:

* TX001 — 250.00 TND
* TX003 — 75.50 TND

![SOAP Approved Transactions](images/soap-approved-transactions.png)

**Test 3: Retrieve Transactions Above an Amount**

Input:

```text
minimumAmount = 100
```

Expected result:

* TX001 — 250.00 TND
* TX002 — 120.00 TND

![SOAP Transactions Above Amount](images/soap-transactions-above-amount.png)

**Test 4: Retrieve Transaction Count**

Expected result:

```text
3
```

![SOAP Transaction Count](images/soap-transaction-count.png)

## 10. Unit Testing with JUnit

JUnit 4 is used to verify the main application functionalities.

The test class is:

```text
PayInsightTest.java
```

The project contains seven unit tests:

| Test                          | Description                                  |
| ----------------------------- | -------------------------------------------- |
| `testXMLValidation`           | Checks XML validation                        |
| `testGetTransaction`          | Checks transaction retrieval                 |
| `testGetApprovedTransactions` | Checks approved transaction retrieval        |
| `testTransactionsAboveAmount` | Checks amount filtering                      |
| `testTransactionCount`        | Checks the total transaction count           |
| `testInvalidTransactionID`    | Checks handling of an unknown transaction ID |
| `testNegativeAmount`          | Checks handling of a negative minimum amount |

### JUnit Results

All seven tests passed successfully.

```text
Tests run: 7
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

![JUnit Test Results](images/junit-tests.png)

## 11. How to Run the Project

### Prerequisites

Make sure you have:

* Java Development Kit (JDK)
* IntelliJ IDEA
* Apache Maven support
* SoapUI (for SOAP testing)

### Step 1: Clone the Repository

```bash
git clone YOUR_REPOSITORY_URL
```

Open the project in IntelliJ IDEA and wait for Maven to load the dependencies.

### Step 2: Run the XML Application

Open:

```text
src/main/java/com/payinsight/Main.java
```

Run the `main()` method to validate the XML document and execute the XPath queries.

### Step 3: Run the SOAP Server

Open:

```text
src/main/java/com/payinsight/server/PayInsightServer.java
```

Run its `main()` method.

The service will be available at:

```text
http://localhost:8086/PayInsight/PaymentService
```

Keep the server running while testing the SOAP operations.

### Step 4: Test the SOAP Service

Open SoapUI and create or open a SOAP project using:

```text
http://localhost:8086/PayInsight/PaymentService?wsdl
```

Execute the four available operations.

### Step 5: Run Unit Tests

In IntelliJ IDEA, open the Maven panel:

1. Expand the project.
2. Expand **Lifecycle**.
3. Double-click `clean`.
4. Double-click `test`.

The expected result is:

```text
Tests run: 7
Failures: 0
Errors: 0
BUILD SUCCESS
```

You can also run the Maven `package` lifecycle to compile, test, and package the application.

## 12. Conclusion

PayInsight demonstrates the integration of XML technologies and Java Web Services in a Smart Payment Management System.

The first part of the project focuses on XML data modeling, XSD validation, and XPath queries.

The second part extends the application with a SOAP Web Service that provides access to payment transaction information.

Finally, JUnit and SoapUI are used to test the application and verify the functionality of its components.

The project provides practical experience with structured data, XML processing, Java development, Web Services, and software testing.

**Note:** PayInsight is an academic demonstration project. It uses sample payment data and does not process real financial transactions.
