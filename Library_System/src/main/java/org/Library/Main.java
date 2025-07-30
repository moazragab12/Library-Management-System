package org.Library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import io.github.cdimascio.dotenv.Dotenv;

public class Main {

    public static void main(String[] args) throws SQLException {
        final Dotenv dotenv = Dotenv.load();
        LibrarySystem library = new LibrarySystem();
       // List<Book> Booksbygeneric = new ArrayList<Book>();
       // List<User> Usersbygeneric = new ArrayList<User>();
      //  DatabaseManager.InitializeDatabase();
       // Booksbygeneric = DatabaseManager.getAllBooks();
       // Usersbygeneric = DatabaseManager.getAllUsers();
        Boolean UseDatabase = dotenv.get("USE_DATABASE").equals("true");
        LibrarySystem.setBooks(DatabaseManager.getAllBooksMap());
        LibrarySystem.setUsers(DatabaseManager.getAllUsersMap());
        library.setGenres(DatabaseManager.getAllGenres());

//        Admin admin = new Admin("1",dotenv.get("ADMIN_USER"));
//        admin.setEmail("Admin@gmail.com");
//        String password = dotenv.get("ADMIN_PASSWORD");

//        password = Passwordencrypt.hashPassword(password);
//        admin.setPassword(password);

        if (!UseDatabase) {

            List<Book> books = FileStorageManager.loadBooks();
            List<User> users = FileStorageManager.loadUsers();
            List<BorrowedBook> borrowedBooks = FileStorageManager.loadBorrowedBooks();

            LibrarySystem.setBooks(books.stream().collect(Collectors.toMap(Book::getId, b -> b)));
            LibrarySystem.setUsers(users.stream().collect(Collectors.toMap(User::getId, u -> u)));
//            if (users.stream().noneMatch(u -> u.getName().equalsIgnoreCase(admin.getName()))) {
//                users.add(admin);
//            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("1- Admin\n2- Regular user");
            System.out.print("Please select your role: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                while (true) {
                    System.out.print("Enter admin username: ");
                    String username = scanner.nextLine();

                    System.out.print("Enter password: ");
                    String passwordInput = scanner.nextLine();

                    User user = users.stream()
                            .filter(u -> u.getName().equals(username))
                            .findFirst().orElse(null);

                    if (user == null) {
                        System.out.println("User not found.");
                        continue;
                    }

                    if (!Passwordencrypt.verifyPassword(passwordInput, user.getPassword())) {
                        System.out.println("Incorrect password.");
                        continue;
                    }

                    if (!"admin".equalsIgnoreCase(user.getRole())) {
                        System.out.println("User is not an admin. Your role is: " + user.getRole());
                        continue;
                    }

                    System.out.println("Welcome Admin " + user.getName());
                    break;
                }

                Scanner scanner2 = new Scanner(System.in);
                LibrarySystem librarySystem = new LibrarySystem();

                AdminPanel.runAdminPanel(scanner2, librarySystem);

            } else if (choice == 2) {
                RegularUser regularUser = null;

                while (true) {
                    System.out.print("Enter your username: ");
                    String username = scanner.nextLine();

                    System.out.print("Enter your password: ");
                    String passwordInput = scanner.nextLine();

                    User user = users.stream()
                            .filter(u -> u.getName().equals(username))
                            .findFirst().orElse(null);

                    if (user == null || !"user".equalsIgnoreCase(user.getRole())) {
                        System.out.println("Invalid credentials or not a regular user. Your role is: " + (user != null ? user.getRole() : "N/A"));
                        continue;
                    }

                    if (!Passwordencrypt.verifyPassword(passwordInput, user.getPassword())) {
                        System.out.println("Incorrect password.");
                        continue;
                    }

                    regularUser = new RegularUser(user.getId(), user.getName(), user.getEmail(), user.getPassword());
                    System.out.println("Welcome " + user.getName());
                    break;
                }
                Scanner scanner2 = new Scanner(System.in);
                LibrarySystem librarySystem = new LibrarySystem();
                UserPanel.runUserPanel(scanner2,regularUser,librarySystem);

            }
            FileStorageManager.saveBooks(new ArrayList<>(LibrarySystem.getBooks().values()));
            FileStorageManager.saveUsers(new ArrayList<>(LibrarySystem.getUsers().values()));
            FileStorageManager.saveBorrowedBooks(borrowedBooks);
        }
        else {
          //  DatabaseManager.insertUser(admin);
            System.out.println("1- Admin\n" +
                    "2- Regular user");
            System.out.println("Please select your role:");
            int choice = new java.util.Scanner(System.in).nextInt();
            if (choice == 1) {
                while (true) {

                    System.out.println("Please enter your username:");
                    String username = new java.util.Scanner(System.in).nextLine();
                    System.out.println("Please enter your password:");
                    String passwordInput = new java.util.Scanner(System.in).nextLine();
                    Boolean isUser = DatabaseManager.login(username, passwordInput);
                    if (!isUser) {
                        System.out.println("Invalid username or password. Please try again.");

                    } else {
                        if (DatabaseManager.getUserRoleByUsername(username).equals("admin")) {
                            System.out.println("Welcome Admin " + username);
                            break;
                        } else {
                            System.out.println("You are not an admin. Please try again.");

                        }

                    }

                }
                while (true) {
                    System.out.println("1 - add new book\n" +
                            "2 - edit book \n" +
                            "3 - delete book \n" +
                            "4 - register user \n" +
                            "5 - view all users\n" +
                            "6 - exit");

                    System.out.println("Please select an option:");
                    int adminChoice = new java.util.Scanner(System.in).nextInt();
                    if (adminChoice == 1) {

                        System.out.println("Please enter the book details:");

                        System.out.println("ID:");
                        String id = new java.util.Scanner(System.in).nextLine();
                        System.out.println("Title:");
                        String title = new java.util.Scanner(System.in).nextLine();
                        System.out.println("Author:");
                        String author = new java.util.Scanner(System.in).nextLine();
                        System.out.println("Genre:");
                        String genre = new java.util.Scanner(System.in).nextLine();
                        System.out.println("Available Copies:");
                        int availableCopies = new java.util.Scanner(System.in).nextInt();
                        Book newBook = new Book(id, title, author, genre, availableCopies);

                        DatabaseManager.insertBook(newBook);
                    } else if (adminChoice == 2) {
                        System.out.println("Please enter the book ID to edit:");
                        String bookId = new java.util.Scanner(System.in).nextLine();
                        System.out.println("Please enter the new book details:");
                        System.out.println("Title:");
                        String newTitle = new java.util.Scanner(System.in).nextLine();
                        System.out.println("Author:");
                        String newAuthor = new java.util.Scanner(System.in).nextLine();
                        System.out.println("Genre:");
                        String newGenre = new java.util.Scanner(System.in).nextLine();
                        System.out.println("Available Copies:");
                        int newAvailableCopies = new java.util.Scanner(System.in).nextInt();
                        Book updatedBook = new Book(bookId, newTitle, newAuthor, newGenre, newAvailableCopies);

                        DatabaseManager.updateBook(updatedBook);
                    } else if (adminChoice == 3) {
                        System.out.println("Please enter the book ID to delete:");
                        String bookIdToDelete = new java.util.Scanner(System.in).nextLine();
                        if (bookIdToDelete.isEmpty()) {
                            System.out.println("Book ID cannot be empty. Please try again.");
                            continue;
                        }
                        if (!DatabaseManager.bookExists(bookIdToDelete)) {
                            System.out.println("Book with ID " + bookIdToDelete + " does not exist. Please try again.");
                            continue;
                        }
                        System.out.println("Are you sure you want to delete the book with ID " + bookIdToDelete + "? (yes/no)");
                        String confirmation = new java.util.Scanner(System.in).nextLine();
                        if (!confirmation.equalsIgnoreCase("yes")) {
                            System.out.println("Book deletion cancelled.");
                            continue;
                        }

                        DatabaseManager.deleteBook(bookIdToDelete);

                    } else if (adminChoice == 4) {

                        System.out.println("Registering a new user:");
                        System.out.println("Please enter the user details:");
                        System.out.println("ID:");
                        String userId = new java.util.Scanner(System.in).nextLine();
                        System.out.println("Name:");
                        String userName = new java.util.Scanner(System.in).nextLine();
                        System.out.println("Email:");
                        String userEmail = new java.util.Scanner(System.in).nextLine();
                        System.out.println("Password:");
                        String userPassword = new java.util.Scanner(System.in).nextLine();
                        userPassword = Passwordencrypt.hashPassword(userPassword);
                        User newUser = new User(userId, userName, userEmail, userPassword);
                        if (DatabaseManager.getUserById(userId) != null) {
                            System.out.println("User with ID " + userId + " already exists. Please try again.");
                            continue;
                        }
                        if (userId.isEmpty() || userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
                            System.out.println("User details cannot be empty. Please try again.");
                            continue;
                        }
                        if (!userEmail.contains("@")) {
                            System.out.println("Invalid email format. Please try again.");
                            continue;
                        }
                        if (userPassword.length() < 6) {
                            System.out.println("Password must be at least 6 characters long. Please try again.");
                            continue;
                        }
                        if (DatabaseManager.getUserByEmail(userEmail) != null) {
                            System.out.println("Email " + userEmail + " is already registered. Please try again.");
                            continue;
                        }

                        DatabaseManager.insertUser(newUser);
                    } else if (adminChoice == 5) {
                        System.out.println("Viewing all users:");

                        List<User> users = DatabaseManager.getAllUsers();
                        if (users.isEmpty()) {
                            System.out.println("No users found.");
                        } else {
                            for (User user : users) {
                                System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Email: " + user.getEmail());
                            }
                        }
                    } else if (adminChoice == 6) {
                        System.out.println("Exiting the admin panel.");
                        break;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }


                }

            } else if (choice == 2) {

                String username;
                while (true) {
                    System.out.println("Please enter your username:");
                    username = new java.util.Scanner(System.in).nextLine();
                    System.out.println("Please enter your password:");
                    String passwordInput = new java.util.Scanner(System.in).nextLine();
                    Boolean isUser = DatabaseManager.login(username, passwordInput);
                    if (!isUser) {
                        System.out.println("Invalid username or password. Please try again.");

                    } else {
                        if (DatabaseManager.getUserRoleByUsername(username).equals("user")) {
                            System.out.println("Welcome User " + username);
                            break;
                        } else {
                            System.out.println("You are not a regular user. Please try again.");

                        }

                    }

                }
                RegularUser regularUser = DatabaseManager.getRegularUserByName(username);
                while (true) {
                    System.out.println("1 - View Book Catalog\n" +
                            "2 - Borrow Book\n" +
                            "3 - Return Book\n" +
                            "4 - getAllbooks from this genre\n" +
                            "5 - getAll books by this author\n" +
                            "6 - Exit");
                    System.out.println("Please select an option:");
                    int userChoice = new java.util.Scanner(System.in).nextInt();
                    if (userChoice == 1) {
                        regularUser.viewCatalog(LibrarySystem.getBooks());
                    } else if (userChoice == 2) {
                        System.out.println("Please enter the book ID to borrow:");
                        String bookId = new java.util.Scanner(System.in).nextLine();
                        if (DatabaseManager.borrowBook(regularUser.getId(), bookId)) {
                            regularUser.borrowBook(bookId);
                        }

                    } else if (userChoice == 3) {
                        System.out.println("Please enter the book ID to return:");
                        String bookId = new java.util.Scanner(System.in).nextLine();
                        assert regularUser != null;
                        DatabaseManager.returnBook(regularUser.getId(), bookId);
                        Book returnedBook = regularUser.returnBook(bookId);

                        if (returnedBook != null) {
                            System.out.println("Book returned successfully: " + returnedBook);
                        } else {
                            System.out.println("Failed to return book. It may not exist or was not borrowed.");
                        }
                    } else if (userChoice == 4) {

                        System.out.println("Please enter the genre:");
                        String genre = new java.util.Scanner(System.in).nextLine();
                        List<Book> booksByGenre = DatabaseManager.getBooksByGenre(genre);
                        if (booksByGenre.isEmpty()) {
                            System.out.println("No books found in this genre.");
                        } else {
                            for (Book book : booksByGenre) {
                                System.out.println(book);
                                System.out.println("-------------------------------------");
                            }
                        }
                    } else if (userChoice == 5) {
                        System.out.println("Please enter the author name:");
                        String author = new java.util.Scanner(System.in).nextLine();
                        List<Book> booksByAuthor = DatabaseManager.getBooksByAuthor(author);
                        if (booksByAuthor.isEmpty()) {
                            System.out.println("No books found by this author.");
                        } else {
                            for (Book book : booksByAuthor) {
                                System.out.println(book);
                                System.out.println("-------------------------------------");
                            }
                        }
                    } else if (userChoice == 6) {
                        System.out.println("Exiting the user panel.");
                        break;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }

            } else {
                System.out.println("Invalid choice. Please restart the application.");
            }

        }
    }
}