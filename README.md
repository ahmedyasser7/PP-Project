# CSV to Database Batch Processor - Parallel Programming Project

## 📌 Overview
This project is part of the **Parallel Programming** course and was developed as a college project. It demonstrates efficient batch processing using parallel computing techniques to handle large datasets effectively.
This project reads data from a CSV file and inserts it into a database using **Spring Batch**. It processes data efficiently in batches to ensure optimal performance and reliability. This approach is ideal for handling large datasets while maintaining system performance.

## 🛠️ Technologies Used
- **Java 17** → The programming language used to develop the application.
- **Spring Boot** → A framework that simplifies the development of Java applications, providing built-in configurations and reducing boilerplate code.
- **Spring Batch** → A framework for processing large volumes of data efficiently in batch mode.
- **Spring Data JPA** → A library that simplifies database interactions using Java Persistence API (JPA).
- **MySQL / PostgreSQL** → Relational database management systems used to store processed data.
- **Maven** → A build automation and dependency management tool for Java projects.
- **Java 17**
- **Spring Boot**
- **Spring Batch**
- **Spring Data JPA**
- **MySQL / PostgreSQL**
- **Maven**

## 📂 Project Structure
```
📁 src/main/java/com/example/csvprocessor
 ┣ 📂 config          # Spring Batch Configuration
 ┣ 📂 entity          # User Entity
 ┣ 📂 processor       # Data Processing Logic
 ┣ 📂 repository      # Database Repository
 ┣ 📂 writer          # Batch Writer
 ┗ 📂 main            # Main Application Entry Point
```

## 🚀 How It Works
1. **Reads** data from a CSV file using `FlatFileItemReader`.
2. **Processes** each record (e.g., validation, transformation) via `ItemProcessor`.
3. **Inserts** valid records into the database in batches using `JdbcBatchItemWriter` or `JpaItemWriter`.
4. **Ignores or logs** invalid records for later review.

## 📥 Installation & Setup
### 1️⃣ Clone the Repository
```sh
git clone https://github.com/yourusername/csv-to-database.git
cd csv-to-database
```

### 2️⃣ Configure Database
Modify `application.properties` to set up your database:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.batch.initialize-schema=always
```

### 3️⃣ Run the Application
```sh
mvn spring-boot:run
```

## 📄 Sample CSV Format
```
id,name,email,age
1,Ahmed,ahmed@gmail.com,25
2,Sara,sara@gmail.com,22
3,Ali,ali@yahoo.com,30
```

## ✅ Features
✔ Reads CSV files dynamically
✔ Processes data in batches efficiently
✔ Validates and transforms records before inserting
✔ Stores data efficiently in a database
✔ Logs errors for debugging invalid records

## 🏗 Future Enhancements
- Implement a web interface to upload CSV files.
- Add error handling and retry mechanisms.
- Support multiple file formats (e.g., JSON, XML).

## 👨‍💻 Authors
Developed by:
- Ahmed Essam Mohamed
- Ahmed Yasser Taha
- Moaaz Ahmed Hussien
- Noruhan Mohamed Emam

