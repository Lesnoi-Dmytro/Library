package org.nerdy.soft.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.nerdy.soft.library.data.Book;
import org.nerdy.soft.library.data.borrow.BorrowId;
import org.nerdy.soft.library.data.borrow.Borrowed;
import org.nerdy.soft.library.repositiry.BookRepository;
import org.nerdy.soft.library.repositiry.borrow.BorrowedRepository;
import org.nerdy.soft.library.request.AddBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {
	@Autowired
	BookService bookService;

	@MockBean
	BookRepository bookRepository;

	@MockBean
	BorrowedRepository borrowedRepository;

	static final String BOOK_TITLE1 = "A Tale of Two Cities";
	static final String BOOK_TITLE2 = "The Little Prince";

	static final String BOOK_AUTHOR1 = "Charles Dickens";
	static final String BOOK_AUTHOR2 = "Antoine de Saint-Exupéry";

	static final int BOOK_AMOUNT = 10;

	@Test
	void contextLoads() {
		assertNotNull(bookService);
	}

	@Test
	void getBooksTest() {
		int page = 0;
		int size = 10;
		PageRequest request = PageRequest.of(page, size);

		Page<Book> books = Page.empty();
		when(bookRepository.findAll(request)).thenReturn(books);

		assertEquals(books, bookService.getBooks(0, 10));
	}

	@Nested
	class GetBooksById {
		Book book = mock(Book.class);
		static long bookId = 1;

		<T> void testCorrectId(T value, Supplier<T> method) {
			when(bookRepository.findById(bookId))
					.thenReturn(Optional.of(book));

			assertEquals(value, method.get());
		}

		@Test
		void correctId() {
			testCorrectId(book, () -> bookService.getBookById(bookId));
		}

		<T> void testIncorrectId(Supplier<T> method) {
			when(bookRepository.findById(bookId))
					.thenReturn(Optional.empty());

			assertThrows(NoSuchElementException.class, method::get);
		}

		@Test
		void incorrectId() {
			testIncorrectId(() -> bookService.getBookById(bookId));
		}
	}

	@Nested
	public class AddBookTest {
		AddBookRequest addRequest;
		Book sameBook;

		@BeforeEach
		public void setupBook() {
			addRequest = mock(AddBookRequest.class);
			when(addRequest.getTitle()).thenReturn(BOOK_TITLE1);
			when(addRequest.getAuthor()).thenReturn(BOOK_AUTHOR1);

			sameBook = mock(Book.class);
			when(addRequest.getTitle()).thenReturn(BOOK_TITLE1);
			when(addRequest.getAuthor()).thenReturn(BOOK_AUTHOR1);
		}

		@Test
		public void addNonExistingBookWithAmount() {
			when(addRequest.getAmount()).thenReturn(BOOK_AMOUNT);
			when(bookRepository.findByTitleAndAuthor(addRequest.getTitle(), addRequest.getAuthor()))
					.thenReturn(Optional.empty());
			Book book = new Book(0, addRequest.getTitle(), addRequest.getAuthor(),
					addRequest.getAmount(), null, null);
			when(bookRepository.save(book)).thenReturn(book);

			assertEquals(book, bookService.addBook(addRequest));
			verify(addRequest, never()).setAmount(1);
		}

		@Test
		public void addNonExistingBookWithoutAmount() {
			when(addRequest.getAmount()).thenReturn(0);
			when(bookRepository.findByTitleAndAuthor(addRequest.getTitle(), addRequest.getAuthor()))
					.thenReturn(Optional.empty());
			Book book = new Book(0, addRequest.getTitle(), addRequest.getAuthor(),
					addRequest.getAmount(), null, null);
			when(bookRepository.save(book)).thenReturn(book);

			assertEquals(book, bookService.addBook(addRequest));
			verify(addRequest).setAmount(1);
		}

		@Test
		public void addExistingBook() {
			when(addRequest.getAmount()).thenReturn(BOOK_AMOUNT);
			when(bookRepository.findByTitleAndAuthor(addRequest.getTitle(), addRequest.getAuthor()))
					.thenReturn(Optional.of(sameBook));
			when(bookRepository.save(sameBook)).thenReturn(sameBook);

			assertEquals(sameBook, bookService.addBook(addRequest));
			verify(sameBook, times(1))
					.addAmount(addRequest.getAmount());
		}
	}

	@Nested
	class UpdateBookTest extends GetBooksById {

		@BeforeEach
		public void setupBook() {
			book = mock(Book.class);
			when(book.getId()).thenReturn(bookId);
		}

		@Test
		@Override
		void correctId() {
			when(bookRepository.save(book))
					.thenReturn(book);

			testCorrectId(book, () -> bookService.updateBook(book));
			verify(bookRepository).save(book);
		}

		@Test
		@Override
		void incorrectId() {
			testIncorrectId(() -> bookService.updateBook(book));
			verify(bookRepository, never()).save(book);
		}
	}

	@Nested
	class DeleteBookTest extends GetBooksById {
		List<Borrowed> borrowed;

		@BeforeEach
		public void setupBook() {
			book = mock(Book.class);
			borrowed = new ArrayList<>();
			when(book.getBorrowed()).thenReturn(borrowed);
		}

		@Test
		@DisplayName("correctIdNotBorrowed")
		@Override
		void correctId() {
			testCorrectId(true, () -> {
				bookService.deleteBook(bookId);
				return true;
			});
			verify(bookRepository).delete(book);
		}

		@Test
		void correctIdBorrowed() {
			borrowed.add(mock(Borrowed.class));

			assertThrows(IllegalStateException.class,
					() -> testCorrectId(true, () -> {
						bookService.deleteBook(bookId);
						return true;
					}));
			verify(bookRepository, never()).delete(book);
		}

		@Test
		@Override
		void incorrectId() {
			testIncorrectId(() -> {
				bookService.deleteBook(bookId);
				return true;
			});
			verify(bookRepository, never()).delete(book);
		}
	}

	@Test
	void getDistinctBorrowedBooksTest() {
		Borrowed borrowed1 = mock(Borrowed.class);
		Borrowed borrowed2 = mock(Borrowed.class);
		BorrowId id1 = mock(BorrowId.class);
		BorrowId id2 = mock(BorrowId.class);
		Book book1 = mock(Book.class);
		Book book2 = mock(Book.class);

		when(borrowed1.getId()).thenReturn(id1);
		when(borrowed2.getId()).thenReturn(id2);
		when(id1.getBook()).thenReturn(book1);
		when(id2.getBook()).thenReturn(book2);

		when(borrowedRepository.findAll())
				.thenReturn(List.of(borrowed1, borrowed2, borrowed1));

		List<Book> books = bookService.getDistinctBorrowedBooks();
		assertEquals(2, books.size());
		assertTrue(books.contains(book1));
		assertTrue(books.contains(book2));
	}
}