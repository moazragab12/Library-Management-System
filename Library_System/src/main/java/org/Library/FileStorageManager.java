package org.Library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorageManager {

    private static final String BOOKS_FILE = "books.dat",USERS_FILE = "users.dat", BORROW_FILE = "borrowed.dat";
    @SuppressWarnings("unchecked")
    private static <T> List<T> readFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static <T> void writeToFile(String filename, List<T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Book> loadBooks() {
        return readFromFile(BOOKS_FILE);
    }

    public static void saveBooks(List<Book> books) {
        writeToFile(BOOKS_FILE, books);
    }

    public static List<User> loadUsers() {
        return readFromFile(USERS_FILE);
    }

    public static void saveUsers(List<User> users) {
        writeToFile(USERS_FILE, users);
    }

    public static List<BorrowedBook> loadBorrowedBooks() {
        return readFromFile(BORROW_FILE);
    }

    public static void saveBorrowedBooks(List<BorrowedBook> borrowedBooks) {
        writeToFile(BORROW_FILE, borrowedBooks);
    }
}
