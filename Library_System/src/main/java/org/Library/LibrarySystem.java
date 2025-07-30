package org.Library;

import java.util.*;

public class LibrarySystem {
    private static Map<String, Book> books = new HashMap<>();
 private static Map<String, User> users = new HashMap<>();
    private Set<String> genres = new HashSet<>();

    public LibrarySystem() {

    }

    public static void setBooks(Map<String, Book> books) {
        LibrarySystem.books = books;
    }

    public static Map<String, User> getUsers() {
        return users;
    }

    public static void setUsers(Map<String, User> users) {
        LibrarySystem.users = users;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public LibrarySystem(Map<String, Book> initialBooks) {
        if (initialBooks != null) {
            books.putAll(initialBooks);
        }
    }


    public List<Book> getBooksByGenre(String genre) {
        List<Book> booksByGenre = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getGenre().contains(genre)) {
                booksByGenre.add(book);
            }
        }
        return booksByGenre;
    }

    public List<Book> getBooksByAuthor(String author) {
        List<Book> booksByAuthor = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthor().contains(author)) {
                booksByAuthor.add(book);
            }
        }
        return booksByAuthor;
    }
    public void addBook(Book book) {
        if (books.containsKey(book.getId())) {
            System.out.println("Book with this ID already exists.");
            return;
        }
        books.put(book.getId(), book);
        System.out.println("Book added successfully.");
    }
    public boolean returnBook(String bookId) {
        Book book = books.get(bookId);

        if (book != null) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            return true;
        }
        return false;
    }
    public void removeBook(String bookId) {
        if (books.remove(bookId) != null) {
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Book not found.");
        }
    }

    public static Map<String, Book> getBooks() {
        return books;
    }

    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        books.values().forEach(book -> {
            System.out.println(book);
            System.out.println("----------------------");
        });
    }
}
