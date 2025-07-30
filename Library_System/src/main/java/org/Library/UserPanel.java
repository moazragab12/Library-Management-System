package org.Library;

import java.util.List;
import java.util.Scanner;

import java.util.*;


public class UserPanel {
    static List<BorrowedBook> borrowedBooks = new ArrayList<>();
    public static void runUserPanel(Scanner scanner, RegularUser user, LibrarySystem librarySystem) {
        while (true) {
            System.out.println("\n====== User Panel ======");
            System.out.println("1 - View Book Catalog");
            System.out.println("2 - Borrow Book");
            System.out.println("3 - Return Book");
            System.out.println("4 - View Books by Genre");
            System.out.println("5 - View Books by Author");
            System.out.println("6 - Exit");
            System.out.print("Choose an option: ");

            borrowedBooks = FileStorageManager.loadBorrowedBooks();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewCatalog(librarySystem);
                    break;
                case "2":
                    borrowBook(scanner, user, librarySystem);
                    break;
                case "3":
                    returnBook(scanner, user, librarySystem);
                    break;
                case "4":
                    viewByGenre(scanner, librarySystem);
                    break;
                case "5":
                    viewByAuthor(scanner, librarySystem);
                    break;
                case "6":
                    System.out.println("Exiting User Panel...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void viewCatalog(LibrarySystem librarySystem) {
        System.out.println("Available books:");
        if (librarySystem.getBooks().isEmpty()) {
            System.out.println("No books in the catalog.");
        } else {
            for (Book book : librarySystem.getBooks().values()) {
                System.out.println(book);
                System.out.println("----------------------------");
            }
        }
    }

    private static void borrowBook(Scanner scanner, RegularUser user, LibrarySystem librarySystem) {
        System.out.print("Enter Book ID to borrow: ");
        String bookId = scanner.nextLine();

        Book book = librarySystem.getBooks().get(bookId);
        if (book == null || book.getAvailableCopies() <= 0) {
            System.out.println("Book not found or not available.");
            return;
        }

        List<BorrowedBook> borrowedBooks = FileStorageManager.loadBorrowedBooks();
        boolean alreadyBorrowed = borrowedBooks.stream()
                .anyMatch(b -> b.getUserId().equals(user.getId()) && b.getBookId().equals(bookId));

        if (alreadyBorrowed) {
            System.out.println("You have already borrowed this book.");
            return;
        }

        BorrowedBook borrowedBook = new BorrowedBook(user.getId(), bookId);
        borrowedBooks.add(borrowedBook);
        user.borrowBook(bookId);
        System.out.println("Book borrowed successfully.");
    }

    private static void returnBook(Scanner scanner, RegularUser user, LibrarySystem librarySystem) {
        System.out.print("Enter Book ID to return: ");
        String bookId = scanner.nextLine();

        List<BorrowedBook> borrowedBooks = FileStorageManager.loadBorrowedBooks();

        Optional<BorrowedBook> borrowedBookOptional = borrowedBooks.stream()
                .filter(b -> b.getUserId().equals(user.getId()) &&
                        b.getBookId().equals(bookId) &&
                        !b.isReturned())
                .findFirst();

        if (borrowedBookOptional.isPresent()) {
            BorrowedBook borrowedBook = borrowedBookOptional.get();

            borrowedBook.setReturned(true);
            FileStorageManager.saveBorrowedBooks(borrowedBooks);

            if (librarySystem.returnBook(bookId)) {
                System.out.println("Book returned successfully.");
            } else {
                System.out.println("Error updating book availability.");
            }

            user.returnBook(bookId);

        } else {
            System.out.println("You have not borrowed this book or it is already returned.");
        }
    }


    private static void viewByGenre(Scanner scanner, LibrarySystem librarySystem) {
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();

        List<Book> books = librarySystem.getBooksByGenre(genre);
        if (books.isEmpty()) {
            System.out.println("No books found in this genre.");
        } else {
            books.forEach(book -> {
                System.out.println(book);
                System.out.println("----------------------------");
            });
        }
    }

    private static void viewByAuthor(Scanner scanner, LibrarySystem librarySystem) {
        System.out.print("Enter author: ");
        String author = scanner.nextLine();

        List<Book> books = librarySystem.getBooksByAuthor(author);
        if (books.isEmpty()) {
            System.out.println("No books found by this author.");
        } else {
            books.forEach(book -> {
                System.out.println(book);
                System.out.println("----------------------------");
            });
        }
    }
}
