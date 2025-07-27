package org.Library;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegularUser  extends User implements Borrowable{

    private List<String> borrowedBooks = new ArrayList<>();
    public RegularUser(String id, String name) {
        super(id, name);
    }

    public RegularUser(String id, String name, String email, String role) {
        super(id, name, email, role);
    }

    @Override
    public  Boolean borrowBook(String bookId) {
        Book book = LibrarySystem.getBooks().get(bookId);

        if (book == null || book.getAvailableCopies() == 0 || borrowedBooks.contains(bookId)) {
            return false;
        }

        borrowedBooks.add(bookId);
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        return true;
    }

    @Override
    public Book returnBook(String bookId) {
        if (!borrowedBooks.contains(bookId)) return null;

        Book book = LibrarySystem.getBooks().get(bookId);
        if (book != null) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            borrowedBooks.remove(bookId);
            return book;
        }
        return null;
    }

    public void viewCatalog(Map<String, Book> books) {
        if (books.isEmpty()) {
            System.out.println("The book catalog is currently empty.");
            return;
        }

        for (Book book : books.values()) {
            System.out.println(book);
            System.out.println("-------------------------------------");
        }
    }


}
