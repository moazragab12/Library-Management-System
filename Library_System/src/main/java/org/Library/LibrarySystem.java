package org.Library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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



    public void addBook(Book book) {
        if (books.containsKey(book.getId())) {
            System.out.println("Book with this ID already exists.");
            return;
        }
        books.put(book.getId(), book);
        System.out.println("Book added successfully.");
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
