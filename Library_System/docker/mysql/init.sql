CREATE TABLE IF NOT EXISTS Users
(
    id
    VARCHAR
(
    200
) PRIMARY KEY,
    name VARCHAR
(
    200
) UNIQUE,
    email VARCHAR
(
    200
) UNIQUE,
    password VARCHAR
(
    200
),
    role ENUM
(
    'admin',
    'user'
) DEFAULT 'user'
    );

CREATE TABLE IF NOT EXISTS Books
(
    ID
    VARCHAR
(
    200
) PRIMARY KEY,
    title VARCHAR
(
    200
),
    author VARCHAR
(
    200
),
    genre VARCHAR
(
    200
),
    availableCopies INT
    );

CREATE TABLE IF NOT EXISTS BorrowedBooks
(
    id
    INT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    user_id
    VARCHAR
(
    200
),
    book_id VARCHAR
(
    200
),
    borrow_date DATE,
    return_date DATE,
    FOREIGN KEY
(
    user_id
) REFERENCES Users
(
    id
),
    FOREIGN KEY
(
    book_id
) REFERENCES Books
(
    ID
)
    );

INSERT INTO Books (ID, title, author, genre, availableCopies)
VALUES ('1', '1984', 'George Orwell', 'Dystopian', 5),
       ('2', 'To Kill a Mockingbird', 'Harper Lee', 'Classic', 3),
       ('3', 'The Great Gatsby', 'F. Scott Fitzgerald', 'Novel', 4);

INSERT INTO Users (id, name, email, password, role)
VALUES ('u1', 'admin', 'admin@example.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin'),
       ('u2', 'moaz', 'moaz@example.com', '4aa3023b53f0c74eb3f3158193dfcaff85a8cb708e5ed05cc8dd07f2dcd003d0', 'user');
