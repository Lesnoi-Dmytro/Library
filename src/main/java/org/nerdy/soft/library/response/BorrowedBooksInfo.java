package org.nerdy.soft.library.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.nerdy.soft.library.data.Book;

@Data
public class BorrowedBooksInfo {
	@Schema(example = "A Tale of Two Cities")
	private String bookTitle;

	@Schema(example = "2")
	private int borrowedCount;

	public BorrowedBooksInfo(Book book) {
		this.bookTitle = book.getTitle();
		this.borrowedCount = book.getBorrowed().size();
	}
}
