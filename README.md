# 📚 Library Management System

A **Java console-based Library Management System** with support for both **file-based** and **MySQL database storage**.  
The system runs fully containerized with **Docker Compose**, including:
- A MySQL database
- The Java interactive app
- An Adminer web interface for database management

---

## 🚀 Features
- Admin and regular user roles
- Manage books (add, list, borrow, return)
- Persistent storage via MySQL (with Docker volumes)
- Switch between **file-based** and **DB-based** storage using `.env`
- Simple one-command deployment with Docker Compose

---

## 🧰 Prerequisites
- [Docker](https://www.docker.com/products/docker-desktop/) (with Docker Compose)
- [Java 21](https://openjdk.org/projects/jdk/21/) and [Maven](https://maven.apache.org/) (if building the JAR manually)

---

## ⚙️ Environment Configuration

Create a `.env` file in the project root directory with the following configuration options:

```env
# ==============================
# Database Configuration
# ==============================

DB_HOST=localhost         # Hostname for the MySQL database
DB_PORT=3306              # Port number for MySQL
DB_NAME=library_db        # Name of the database to use
DB_USER=your_db_user      # Database username
DB_PASSWORD=your_db_pass  # Database password

# If set to true, the application will use the MySQL database. 
# If set to false, it will use file-based storage.
USE_DATABASE=true         

# ==============================
# Application Admin Credentials
# ==============================

ADMIN_USER=admin          # Username for the application admin
ADMIN_PASSWORD=adminpass  # Password for the application admin
```

> **Note:**  
> - Adjust the values as needed for your environment.
> - The `USE_DATABASE` variable determines whether the system uses MySQL (`true`) or file-based storage (`false`).  
> - Never commit your `.env` file with real passwords or sensitive information to public repositories.

## 🏃‍♂️ Usage & Quick Start

Follow these steps to build and run the Library Management System using Docker Compose:

### 1. Build the Java Application

Make sure you have Java and Maven installed (if you want to build the JAR manually).

```sh
mvn clean package
```

This will generate a JAR file in the `target/` directory.

---

### 2. Prepare for Docker Compose

- Ensure you are **in the project root directory** (where `pom.xml` and your Docker-related files are located).
- Place your `docker-compose.yml` file in this root directory.
- Make sure you have a `.env` file configured as needed (see **Environment Configuration** above).

---

### 3. Start the Services

Run the following command to start the MySQL database, Adminer, and the Java application containers:

```sh
docker compose up --build
```

Docker Compose will:
- Build the Java app image
- Set up the MySQL database (with persistent storage)
- Start Adminer (for easy DB administration)
- Launch the Java console app

---

### 4. Interact with the Java Console App

You need to attach a terminal to the running Java app container to interact with the console UI:

1. In a new terminal, list running containers:

    ```sh
    docker ps
    ```

2. Find the **container ID** or **name** of the Java application container (it may be called `library-app` or similar).

3. Attach to the running container:

    ```sh
    docker attach <container_id_or_name>
    ```

4. Now you can use the interactive console as Admin or regular user.

*Tip: To detach safely without stopping the container, use `Ctrl + p`, then `Ctrl + q`.*

---

### 5. Access Adminer (Optional)

Adminer is available for managing your MySQL database via a web interface.

- Visit [http://localhost:8080](http://localhost:8080)
- Use the credentials from your `.env` file to log in
---
### 6. Shut down and remove containers
   ```sh
   docker-compose down -v
  ```

---

## ℹ️ Additional Notes

- All data is persisted in Docker volumes for the database and app storage.
- For troubleshooting, check logs with `docker logs <container_id_or_name>`.
- Stopping the Java app: Use `Ctrl + C` in the attached terminal, or `docker stop <container_id_or_name>`.
