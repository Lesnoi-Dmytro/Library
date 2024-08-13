package org.nerdy.soft.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.nerdy.soft.library.data.Book;
import org.nerdy.soft.library.data.Member;
import org.nerdy.soft.library.data.borrow.BorrowId;
import org.nerdy.soft.library.data.borrow.Borrowed;
import org.nerdy.soft.library.data.borrow.History;
import org.nerdy.soft.library.repositiry.BookRepository;
import org.nerdy.soft.library.repositiry.borrow.BorrowedRepository;
import org.nerdy.soft.library.repositiry.borrow.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BorrowServiceTest {
	@Autowired
	private BorrowService borrowService;

	@MockBean
	private BorrowedRepository borrowedRepository;

	@MockBean
	private HistoryRepository historyRepository;

	@MockBean
	private BookRepository bookRepository;

	@Nested
	class BorrowBook {
		Book book;
		Member member;
		List borrowedBooks;
		BorrowId id;

		@Value("${borrow.limit}")
		int limit;

		@BeforeEach
		void setupTest() {
			book = mock(Book.class);
			member = mock(Member.class);
			borrowedBooks = mock(List.class);
			when(member.getBorrowed())
					.thenReturn(borrowedBooks);
			id = new BorrowId(book, member);
		}

		@Test
		void borrowTest() {
			when(borrowedBooks.size())
					.thenReturn(limit - 1);
			when(book.getAmount()).thenReturn(1);
			when(borrowedRepository.findById(id))
					.thenReturn(Optional.empty());

			borrowService.borrowBook(book, member);

			verify(borrowedRepository).save(any(Borrowed.class));
			verify(book, times(1)).subtractAmount(1);
			verify(bookRepository).save(any(Book.class));
		}

		@Test
		void borrowWithLimitReachedTest() {
			when(borrowedBooks.size())
					.thenReturn(limit);

			assertThrows(IllegalStateException.class,
					() -> borrowService.borrowBook(book, member));

			when(borrowedBooks.size())
					.thenReturn(limit + 1);

			assertThrows(IllegalStateException.class,
					() -> borrowService.borrowBook(book, member));

			verify(borrowedRepository, never()).save(any(Borrowed.class));
			verify(book, never()).subtractAmount(1);
			verify(bookRepository, never()).save(any(Book.class));
		}

		@Test
		void borrowWithoutFreeBooksTest() {
			when(borrowedBooks.size())
					.thenReturn(limit - 1);
			when(book.getAmount()).thenReturn(0);

			assertThrows(IllegalStateException.class,
					() -> borrowService.borrowBook(book, member));

			when(book.getAmount()).thenReturn(-1);

			assertThrows(IllegalStateException.class,
					() -> borrowService.borrowBook(book, member));

			verify(borrowedRepository, never()).save(any(Borrowed.class));
			verify(book, never()).subtractAmount(1);
			verify(bookRepository, never()).save(any(Book.class));
		}

		@Test
		void borrowIfAlreadyBorrowedTest() {
			when(borrowedBooks.size())
					.thenReturn(limit - 1);
			when(book.getAmount()).thenReturn(0);
			when(borrowedRepository.findById(id))
					.thenReturn(Optional.of(mock(Borrowed.class)));

			assertThrows(IllegalStateException.class,
					() -> borrowService.borrowBook(book, member));

			verify(borrowedRepository, never()).save(any(Borrowed.class));
			verify(book, never()).subtractAmount(1);
			verify(bookRepository, never()).save(any(Book.class));
		}
	}

	@Nested
	class ReturnBook {
		Book book;
		Member member;
		BorrowId id;

		@BeforeEach
		void setupTest() {
			book = mock(Book.class);
			member = mock(Member.class);
			id = new BorrowId(book, member);
		}

		@Test
		void returnTest() {
			Borrowed borrowed = mock(Borrowed.class);

			when(borrowedRepository.findById(id))
					.thenReturn(Optional.of(borrowed));

			borrowService.returnBook(book, member);

			verify(borrowedRepository).delete(borrowed);
			verify(historyRepository).save(new History(borrowed));
			verify(book, times(1))
					.addAmount(1);
			verify(bookRepository).save(book);
		}

		@Test
		void returnNotBorrowedTest() {
			when(borrowedRepository.findById(id))
					.thenReturn(Optional.empty());

			assertThrows(NoSuchElementException.class,
					() -> borrowService.returnBook(book, member));

			verify(borrowedRepository, never())
					.delete(any());
			verify(historyRepository, never())
					.save(any());
			verify(book, never())
					.addAmount(1);
		}
	}
}