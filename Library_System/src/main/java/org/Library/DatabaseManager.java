package org.Library;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.*;


public class DatabaseManager {
    static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("db_url");
    private static final String USER = dotenv.get("db_username");
    private static final String PASSWORD = dotenv.get("db_psswd");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void InitializeDatabase() {
        createBooksTable();
        createUsersTable();
        createBorrowedBooksTable();
    }

    public static void createBooksTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Books (" +
                "ID VARCHAR(200) PRIMARY KEY, " +
                "title VARCHAR(200), " +
                "author VARCHAR(200), " +
                "genre VARCHAR(200), " +
                "availableCopies INT)";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Books table created or already exists.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean bookExists(String bookId) {
        String sql = "SELECT COUNT(*) FROM Books WHERE ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Users (" +
                "id VARCHAR(200) PRIMARY KEY, " +
                "name VARCHAR(200) UNIQUE, " +
                "email VARCHAR(200) UNIQUE, " +
                "password VARCHAR(200), " +
                "role ENUM('admin', 'user') DEFAULT 'user'" +
                ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Users table created or already exists.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clear() {
        String sql = "DROP TABLE IF EXISTS BorrowedBooks, Users, Books";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("All tables cleared.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createBorrowedBooksTable() {
        String sql = "CREATE TABLE IF NOT EXISTS BorrowedBooks (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id VARCHAR(200), " +
                "book_id VARCHAR(200), " +
                "borrow_date DATE, " +
                "return_date DATE, " +
                "FOREIGN KEY (user_id) REFERENCES Users(id), " +
                "FOREIGN KEY (book_id) REFERENCES Books(ID)" +
                ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("BorrowedBooks table created or already exists.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void insertBook(Book book) {
        String checkSql = "SELECT COUNT(*) FROM Books WHERE id = ? OR (title = ? AND author = ?)";
        String insertSql = "INSERT INTO Books (ID, title, author, genre, availableCopies) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, book.getId());
            checkStmt.setString(2, book.getTitle());
            checkStmt.setString(3, book.getAuthor());

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                // No duplicate found, insert the book
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, book.getId());
                    insertStmt.setString(2, book.getTitle());
                    insertStmt.setString(3, book.getAuthor());
                    insertStmt.setString(4, book.getGenre());
                    insertStmt.setInt(5, book.getAvailableCopies());
                    insertStmt.executeUpdate();
                    System.out.println("Book inserted successfully.");
                }
            } else {
                System.out.println("Book already exists. Insertion skipped.");
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getString("ID"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("availableCopies")
                ));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String username = rs.getString("name");
                String email = rs.getString("email");
                String role = rs.getString("role");

                User user = new User(id, username, email, role);
                users.add(user);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }


    public static void deleteBook(String id) {
        String sql = "DELETE FROM Books WHERE ID = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("Book deleted.");
            else System.out.println("Book not found.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBook(Book book) {
        String checkSql = "SELECT COUNT(*) FROM Books WHERE ID = ?";
        String updateSql = "UPDATE Books SET title = ?, author = ?, genre = ?, availableCopies = ? WHERE ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            checkStmt.setString(1, book.getId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                updateStmt.setString(1, book.getTitle());
                updateStmt.setString(2, book.getAuthor());
                updateStmt.setString(3, book.getGenre());
                updateStmt.setInt(4, book.getAvailableCopies());
                updateStmt.setString(5, book.getId());

                int updated = updateStmt.executeUpdate();
                if (updated > 0) {
                    System.out.println("Book updated successfully.");
                } else {
                    System.out.println("Book update failed.");
                }
            } else {
                System.out.println("Book with ID '" + book.getId() + "' does not exist.");
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertUser(User user) {
        String checkSql = "SELECT id, name, email FROM Users WHERE id = ? OR name = ? OR email = ?";
        String insertSql = "INSERT INTO Users (id, name, email, password, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, user.getId());
            checkStmt.setString(2, user.getName());
            checkStmt.setString(3, user.getEmail());

            ResultSet rs = checkStmt.executeQuery();
            boolean exists = false;

            while (rs.next()) {
                if (user.getId().equals(rs.getString("id"))) {
                    System.out.println("A user with this ID already exists.");
                    exists = true;
                    break;
                }
                if (user.getName().equals(rs.getString("name"))) {
                    System.out.println("A user with this name already exists.");
                    exists = true;
                    break;
                }
                if (user.getEmail().equals(rs.getString("email"))) {
                    System.out.println("A user with this email already exists.");
                    exists = true;
                }
            }

            if (!exists) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, user.getId());
                    insertStmt.setString(2, user.getName());
                    insertStmt.setString(3, user.getEmail());
                    insertStmt.setString(4, user.getPassword());
                    insertStmt.setString(5, user instanceof Admin ? "admin" : "user");

                    insertStmt.executeUpdate();
                    System.out.println("User inserted successfully.");
                }
            } else {
                System.out.println("User insertion skipped due to duplicate ID, name, or email.");
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean login(String username, String password) {
        String sql = "SELECT password FROM Users WHERE name = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                return storedHash.equals(Passwordencrypt.hashPassword(password));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean borrowBook(String userId, String bookId) {
        String checkSql = "SELECT * FROM BorrowedBooks WHERE user_id = ? AND book_id = ?";
        String borrowSql = "INSERT INTO BorrowedBooks (user_id, book_id, borrow_date) VALUES (?, ?, CURRENT_DATE)";
        String updateBookSql = "UPDATE Books SET availableCopies = availableCopies - 1 WHERE ID = ? AND availableCopies > 0";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                    PreparedStatement borrowStmt = conn.prepareStatement(borrowSql);
                    PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql)
            ) {
                checkStmt.setString(1, userId);
                checkStmt.setString(2, bookId);

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    System.out.println("User has already borrowed this book and not returned it.");
                    conn.rollback();
                    return false;
                }
                borrowStmt.setString(1, userId);
                borrowStmt.setString(2, bookId);
                borrowStmt.executeUpdate();


                updateBookStmt.setString(1, bookId);
                int updated = updateBookStmt.executeUpdate();

                if (updated > 0) {
                    conn.commit();
                    System.out.println("Book borrowed successfully.");
                    return true;
                } else {
                    conn.rollback();
                    System.out.println("Book not available.");
                    return false;
                }

            }
            catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean returnBook(String userId, String bookId) {
        String updateReturn = "UPDATE BorrowedBooks SET return_date = CURRENT_DATE " +
                "WHERE user_id = ? AND book_id = ? AND return_date IS NULL";

        String updateBookStock = "UPDATE Books SET availableCopies = availableCopies + 1 WHERE id = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement returnStmt = conn.prepareStatement(updateReturn);
                    PreparedStatement updateStockStmt = conn.prepareStatement(updateBookStock)
            ) {

                returnStmt.setString(1, userId);
                returnStmt.setString(2, bookId);

                int updatedReturn = returnStmt.executeUpdate();

                if (updatedReturn == 0) {
                    System.out.println("No matching borrow record found or book already returned.");
                    conn.rollback();
                    return false;
                }

                updateStockStmt.setString(1, bookId);
                updateStockStmt.executeUpdate();

                conn.commit();
                System.out.println("Book returned successfully.");
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static User getUserById(String userId) {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String role = rs.getString("role");

                if ("admin".equalsIgnoreCase(role)) {
                    return new Admin(id, name, password);
                } else {
                    return new User(id, name, password);
                }
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static User getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String role = rs.getString("role");

                if ("admin".equalsIgnoreCase(role)) {
                    return new Admin(id, name, email, password);
                } else {
                    return new User(id, name, email, password);
                }
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getEmailByUsername(String username) {
        String sql = "SELECT email FROM Users WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("email");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getUserRoleByUsername(String username) {
        String sql = "SELECT role FROM Users WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static User getUserByName(String name) {
        String sql = "SELECT * FROM Users WHERE name = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String email = rs.getString("email");
                    String role = rs.getString("role");

                    return new User(id, name, email, role);
                }
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static RegularUser getRegularUserByName(String name) {
        String sql = "SELECT * FROM Users WHERE name = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String email = rs.getString("email");
                    String role = rs.getString("role");

                    return new RegularUser(id, name, email, role);
                }
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Set<String> getAllGenres() {
        Set<String> genres = new HashSet<>();
        String sql = "SELECT DISTINCT genre FROM Books";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                genres.add(rs.getString("genre"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return genres;
    }

    public static Map<String, Book> getAllBooksMap() {
        Map<String, Book> booksMap = new HashMap<>();

        String sql = "SELECT * FROM Books";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("availableCopies")
                );
                booksMap.put(book.getId(), book);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return booksMap;
    }

    public static Map<String, User> getAllUsersMap() {
        Map<String, User> usersMap = new HashMap<>();
        String sql = "SELECT * FROM Users";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String role = rs.getString("role");

                User user = new User(id, name, email, role);
                usersMap.put(id, user);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return usersMap;
    }
    private static Book mapRowToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getString("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("genre"),
                rs.getInt("availableCopies")
        );
    }


    public static List<Book> getBooksByGenre(String genre) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM Books WHERE genre LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + genre + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public static List<Book> getBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM Books WHERE author LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + author + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

}