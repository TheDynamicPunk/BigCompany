# Employee Analyzer (Java 17 + Maven)

A simple, clean Java 17 console application that analyzes an
organizational structure from a CSV file.

It reports:

1.  **Managers who earn less than they should**
2.  **Managers who earn more than they should**
3.  **Employees with more than 4 managers between them and the CEO**

Designed to be:

-   Clean and readable
-   Easy to test
-   Easy to run
-   No external dependencies (Java SE + JUnit only)

## 🚀 Features

### ✔️ CSV Parsing

Reads a CSV file of employees with fields:

    Id,firstName,lastName,salary,managerId

### ✔️ Hierarchy Builder

Links employees to their managers and identifies the CEO as the tree
root.

### ✔️ Salary Analysis

For each manager:

-   Salary must be **20% to 50%** above the **average salary of direct
    subordinates**\
-   Reports **UNDERPAID** and **OVERPAID** managers with the exact
    difference

### ✔️ Reporting Depth Analysis

Finds employees who have **more than 4 managers** between them and the
CEO.

### ✔️ Two Output Formats

Use at runtime:

-   `--format=pretty` (default): human-friendly, clean output\
-   `--format=json`: structured machine-friendly output

## 📁 Project Structure

    employee-analyzer/
    ├── README.md
    ├── src
    │   ├── main/java/com/company/analyzer/...
    │   ├── main/resources/employees.csv
    │   └── test/java/com/company/analyzer/...
    └── gradlew / gradlew.bat

### **Run the application**

Clean and build package

```bash
mvn clean package
```

Run these commands in the root of the project

``` bash
mvn exec:java "-Dexec.args=src/main/resources/employees.csv --format=pretty"
```

Or with JSON output:

``` bash
mvn exec:java "-Dexec.args=src/main/resources/employees.csv --format=json"
```

### **Run tests**

``` bash
mvn test
```

## 📌 CSV File Format

Your CSV must follow this structure:

    Id,firstName,lastName,salary,managerId
    123,Joe,Doe,60000,
    124,Martin,Chekov,45000,123
    125,Bob,Ronstad,47000,123
    300,Alice,Hasacat,50000,124
    305,Brett,Hardleaf,34000,300