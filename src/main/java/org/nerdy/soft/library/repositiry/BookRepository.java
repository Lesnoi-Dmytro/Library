package org.nerdy.soft.library.repositiry;

import org.nerdy.soft.library.data.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository
		extends JpaRepository<Book, Long> {
	Optional<Book> findByTitleAndAuthor(String title, String author);
}
