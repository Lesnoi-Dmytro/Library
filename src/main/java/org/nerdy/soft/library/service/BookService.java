package org.nerdy.soft.library.service;

import lombok.RequiredArgsConstructor;
import org.nerdy.soft.library.data.Book;
import org.nerdy.soft.library.repositiry.BookRepository;
import org.nerdy.soft.library.repositiry.borrow.BorrowedRepository;
import org.nerdy.soft.library.request.AddBookRequest;
import org.nerdy.soft.library.response.BorrowedBooksInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;
	private final BorrowedRepository borrowedRepository;

	public Page<Book> getBooks(int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);

		return bookRepository.findAll(pageRequest);
	}

	public Book getBookById(long id) {
		return bookRepository.findById(id).orElseThrow(
				() -> new NoSuchElementException("Book with id " + id + " not found"));
	}

	@Transactional
	public Book addBook(AddBookRequest addRequest) {
		if (addRequest.getAmount() == 0) {
			addRequest.setAmount(1);
		}
		Book book = new Book(0, addRequest.getTitle(), addRequest.getAuthor(),
				addRequest.getAmount(), null, null);

		Optional<Book> sameBookOpt = bookRepository
				.findByTitleAndAuthor(addRequest.getTitle(), addRequest.getAuthor());

		if (sameBookOpt.isPresent()) {
			Book sameBook = sameBookOpt.get();
			sameBook.addAmount(addRequest.getAmount());
			book = sameBook;
		}

		return bookRepository.save(book);
	}

	@Transactional
	public Book updateBook(Book book) {
		getBookById(book.getId());
		return bookRepository.save(book);
	}

	@Transactional
	public void deleteBook(long id) {
		Book book = getBookById(id);
		if (book.getBorrowed().isEmpty()) {
			bookRepository.delete(book);
		} else {
			throw new IllegalStateException("Book with id " + id + " is borrowed");
		}
	}

	public List<Book> getDistinctBorrowedBooks() {
		return borrowedRepository.findAll()
				.stream()
				.map(b -> b.getId().getBook())
				.distinct()
				.toList();
	}

	public List<String> getBorrowedBooksTitle() {
		return getDistinctBorrowedBooks()
				.stream()
				.map(Book::getTitle)
				.toList();
	}

	public List<BorrowedBooksInfo> getBorrowedBooksInfo() {
		return getDistinctBorrowedBooks()
				.stream()
				.map(BorrowedBooksInfo::new)
				.toList();
	}
}