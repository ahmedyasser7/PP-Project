# CSV to Database Batch Processor - Parallel Programming Project

## 📌 Overview
This project is part of the **Parallel Programming** course and was developed as a college project. It demonstrates efficient batch processing using parallel computing techniques to handle large datasets effectively.
This project reads data from a CSV file and inserts it into a database using **Spring Batch**. It processes data efficiently in batches to ensure optimal performance and reliability. This approach is ideal for handling large datasets while maintaining system performance.

## 🛠️ Technologies Used
- **Java 17** → The programming language used to develop the application.
- **Spring Boot** → A framework that simplifies the development of Java applications, providing built-in configurations and reducing boilerplate code.
- **Spring Batch** → A framework for efficiently processing large volumes of data in batch mode.
- **MySQL** → Relational database management systems used to store processed data.
- **Maven** → A build automation and dependency management tool for Java projects.


## 🚀 How It Works
1. **Reads** data from a CSV file using `FlatFileItemReader`.
2. **Processes** each record (e.g., validation, transformation) via `ItemProcessor`.
3. **Inserts** valid records into the database in batches using `JdbcBatchItemWriter` or `JpaItemWriter`.
4. **Ignores or logs** invalid records for later review.


## 📄 Sample CSV Format
```
id,name,email,age
1,Ahmed,ahmed@gmail.com,25
2,Sara,sara@gmail.com,22
3,Ali,ali@yahoo.com,30
```

## ✅ Features
✔ Reads CSV files dynamically <br>
✔ Processes data in batches efficiently <br>
✔ Validates and transforms records before inserting <br>
✔ Stores data efficiently in a database <br>
✔ Logs errors for debugging invalid records


## 👨‍💻 Authors
Developed by:
- Ahmed Essam Mohammed Samy
- Ahmed Yasser Taha
- Moaaz Ahmed Hussien
- Nourhan Mohammed Emam
