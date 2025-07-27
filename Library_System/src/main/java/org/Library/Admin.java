package org.Library;

public class Admin extends User {
    public Admin(String id, String name, String password) {
        super(id, name);
        this.setPassword(password);
    }

    public Admin(String id, String name, String email, String password) {
        super(id, name, email, password);

    }

    public Admin(String number, String admin) {
        super(number, admin);

    }

    public void addBook(LibrarySystem library, Book book) {
        library.addBook(book);
    }
    public void removeBook(LibrarySystem library, String bookId) {
        library.removeBook(bookId);
    }
    void updateBook(Book book) {
      DatabaseManager.updateBook(book);
    }


}
