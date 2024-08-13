package org.nerdy.soft.library.response;

import lombok.Data;
import org.nerdy.soft.library.data.Book;

@Data
public class BorrowedBooksResponse {
	private String bookTitle;
	private int borrowedCount;

	public BorrowedBooksResponse(Book book) {
		this.bookTitle = book.getTitle();
		this.borrowedCount = book.getBorrowed().size();
	}
}
