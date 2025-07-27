package org.Library;

public interface Borrowable {
    Boolean borrowBook(String bookId);
   Book returnBook(String bookId);
}
