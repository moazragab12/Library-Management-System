package org.Library;
import java.util.Scanner;

public class AdminPanel {

    public static void runAdminPanel(Scanner scanner, LibrarySystem librarySystem) {
        while (true) {
            System.out.println("\n====== Admin Panel ======");
            System.out.println("1 - Add new book");
            System.out.println("2 - Edit book");
            System.out.println("3 - Delete book");
            System.out.println("4 - Register user");
            System.out.println("5 - View all users");
            System.out.println("6 - Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addBook(scanner, librarySystem);
                    break;
                case "2":
                    editBook(scanner);
                    break;
                case "3":
                    deleteBook(scanner, librarySystem);
                    break;
                case "4":
                    registerUser(scanner);
                    break;
                case "5":
                    viewAllUsers();
                    break;
                case "6":
                    System.out.println("Exiting Admin Panel...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addBook(Scanner scanner, LibrarySystem librarySystem) {
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Author: ");
        String author = scanner.nextLine();

        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();

        System.out.print("Enter Quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        Book newBook = new Book(id, title, author, genre, quantity);
        librarySystem.addBook(newBook);
    }

    private static void editBook(Scanner scanner) {
        System.out.print("Enter Book ID to edit: ");
        String id = scanner.nextLine();

        Book book = LibrarySystem.getBooks().get(id);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.print("New Title (leave blank to keep current): ");
        String title = scanner.nextLine();
        if (!title.isEmpty()) book.setTitle(title);

        System.out.print("New Author (leave blank to keep current): ");
        String author = scanner.nextLine();
        if (!author.isEmpty()) book.setAuthor(author);

        System.out.print("New Genre (leave blank to keep current): ");
        String genre = scanner.nextLine();
        if (!genre.isEmpty()) book.setGenre(genre);

        System.out.print("New Quantity (-1 to skip): ");
        String qtyInput = scanner.nextLine();
        if (!qtyInput.isEmpty()) {
            int qty = Integer.parseInt(qtyInput);
            if (qty >= 0) book.setAvailableCopies(qty);
        }

        System.out.println("Book updated.");
    }

    private static void deleteBook(Scanner scanner, LibrarySystem librarySystem) {
        System.out.print("Enter Book ID to delete: ");
        String id = scanner.nextLine();
        librarySystem.removeBook(id);
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("Enter User ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Role (admin/user): ");
        String role = scanner.nextLine();

        if (!role.equalsIgnoreCase("admin") && !role.equalsIgnoreCase("user")) {
            System.out.println("Invalid role. Only 'admin' or 'user' allowed.");
            return;
        }

        User user = new User(id, name, role.toLowerCase());
        if (LibrarySystem.getUsers().containsKey(id)) {
            System.out.println("User with this ID already exists.");
        } else {
            LibrarySystem.getUsers().put(id, user);
            System.out.println("User registered successfully.");
        }
    }

    private static void viewAllUsers() {
        if (LibrarySystem.getUsers().isEmpty()) {
            System.out.println("No users available.");
        } else {
            LibrarySystem.getUsers().values().forEach(System.out::println);
        }
    }
}
