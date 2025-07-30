package org.Library;

import java.io.Serializable;
import java.time.LocalDate;

public class BorrowedBook implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean isReturned;

    public BorrowedBook(String userId, String bookId, LocalDate borrowDate, LocalDate dueDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.isReturned = false;
    }

    public BorrowedBook(String userId, String bookId)
    {
        this.borrowDate= LocalDate.now();
        this.dueDate = borrowDate.plusDays(14);

        this.userId = userId;
        this.bookId = bookId;
        this.isReturned = false;
    }

    public String getUserId() {
        return userId;
    }

    public String getBookId() {
        return bookId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    @Override
    public String toString() {
        return "BorrowedBook{" +
                "userId='" + userId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", isReturned=" + isReturned +
                '}';
    }
}
