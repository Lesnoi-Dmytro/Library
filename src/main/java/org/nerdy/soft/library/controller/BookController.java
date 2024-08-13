package org.nerdy.soft.library.controller;

import lombok.RequiredArgsConstructor;
import org.nerdy.soft.library.data.Book;
import org.nerdy.soft.library.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
	private final BookService bookService;

	@GetMapping
	public Page<Book> getAllBooks(@RequestParam int page,
								  @RequestParam int size) {
		return bookService.getBooks(page, size);
	}

	@GetMapping("/{id}")
	public Book getBookById(@PathVariable long id) {
		return bookService.getBookById(id);
	}

	@PostMapping
	public Book addBook(@RequestBody Book book) {
		return bookService.addBook(book);
	}

	@PutMapping
	public Book updateBook(@RequestBody Book book) {
		return bookService.updateBook(book);
	}

	@DeleteMapping("/{id}")
	public void deleteBook(@PathVariable long id) {
		bookService.deleteBook(id);
	}
}
