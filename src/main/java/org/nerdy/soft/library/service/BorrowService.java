package org.nerdy.soft.library.service;

import lombok.RequiredArgsConstructor;
import org.nerdy.soft.library.data.Book;
import org.nerdy.soft.library.data.Member;
import org.nerdy.soft.library.data.borrow.BorrowId;
import org.nerdy.soft.library.data.borrow.Borrowed;
import org.nerdy.soft.library.data.borrow.History;
import org.nerdy.soft.library.repositiry.BookRepository;
import org.nerdy.soft.library.repositiry.borrow.BorrowedRepository;
import org.nerdy.soft.library.repositiry.borrow.HistoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BorrowService {
	private final BorrowedRepository borrowedRepository;
	private final HistoryRepository historyRepository;
	private final BookRepository bookRepository;

	@Value("${borrow.limit}")
	private int limit;

	@Transactional
	public void borrowBook(Book book, Member member) {
		if (member.getBorrowed().size() >= limit) {
			throw new IllegalStateException("Member with id "
					+ member.getId() + " reached borrow limit: " + limit);
		}
		if (book.getAmount() <= 0) {
			throw new IllegalStateException("There is no free books with id " + book.getId());
		}

		BorrowId id = new BorrowId(book, member);
		borrowedRepository.findById(id)
				.ifPresent(borrow -> {
					throw new IllegalStateException("Book with id " + book.getId() +
							" is already borrowed by Member with id " + member.getId());
				});

		Borrowed borrow = new Borrowed(id);
		borrow = borrowedRepository.save(borrow);

		book.subtractAmount(1);
		book.removeBorrowed(borrow);
		bookRepository.save(book);
	}

	@Transactional
	public void returnBook(Book book, Member member) {
		BorrowId id = new BorrowId(book, member);
		Optional<Borrowed> borrowOpt = borrowedRepository.findById(id);
		if (borrowOpt.isEmpty()) {
			throw new NoSuchElementException("Book with id " + book.getId() +
					" is not borrowed by Member with id " + member.getId());
		}
		Borrowed borrow = borrowOpt.get();

		History history = new History(borrow);
		book.removeBorrowed(borrow);
		borrowedRepository.delete(borrow);
		history = historyRepository.save(history);

		book.addAmount(1);
		book.addHistory(history);
		bookRepository.save(book);
	}
}
