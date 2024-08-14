package org.nerdy.soft.library.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nerdy.soft.library.data.Book;
import org.nerdy.soft.library.request.AddBookRequest;
import org.nerdy.soft.library.response.BorrowedBooksInfo;
import org.nerdy.soft.library.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(
		name = "Books",
		description = "Book CRUD operations"
)
public class BookController {
	private final BookService bookService;

	@Operation(
			description = "All books retrieval. Results are pageable",
			summary = "All books",
			parameters = {
					@Parameter(
							in = ParameterIn.QUERY,
							required = true,
							name = "page",
							description = "Page number, starts from 0",
							example = "1"),
					@Parameter(
							in = ParameterIn.QUERY,
							required = true,
							name = "size",
							description = "Page size",
							example = "20")
			},
			responses = {
					@ApiResponse(
							description = "Page of books",
							responseCode = "200"
					)
			}
	)
	@GetMapping
	public Page<Book> getAllBooks(@RequestParam int page,
								  @RequestParam int size) {
		return bookService.getBooks(page, size);
	}

	@Operation(
			description = "Returns book by id. Throws error if there is no such book",
			summary = "Book by id",
			parameters = {
					@Parameter(
							in = ParameterIn.PATH,
							required = true,
							name = "id",
							description = "Book id",
							example = "1")
			},
			responses = {
					@ApiResponse(
							description = "Book with specified id",
							responseCode = "200",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = Book.class)
							)
					),
					@ApiResponse(
							description = "Book with specified id not found",
							responseCode = "400",
							ref = "#/components/responses/400Response"
					)
			}
	)
	@GetMapping("/{id}")
	public Book getBookById(@PathVariable long id) {
		return bookService.getBookById(id);
	}

	@Operation(
			description = "Add a book. If book with the same title and author already exists, amount of books will " +
					"be added to existing book. If either title or author is different, the book is considered new",
			summary = "Add book",
			responses = {
					@ApiResponse(
							description = "Added book",
							responseCode = "200",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = Book.class)
							)
					)
			}
	)
	@PostMapping
	public Book addBook(@RequestBody AddBookRequest book) {
		return bookService.addBook(book);
	}

	@Operation(
			description = "Updates book. Throws error if there is no such book",
			summary = "Update book",
			responses = {
					@ApiResponse(
							description = "Updated book",
							responseCode = "200",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = Book.class)
							)
					),
					@ApiResponse(
							description = "Book with specified id not found",
							responseCode = "400",
							ref = "#/components/responses/400Response"
					)
			}
	)
	@PutMapping
	public Book updateBook(@RequestBody Book book) {
		return bookService.updateBook(book);
	}


	@Operation(
			description = "Deletes a book. Throws error if there is no such book or if book is borrowed",
			summary = "Delete book",
			responses = {
					@ApiResponse(
							description = "Book deleted successfully",
							responseCode = "200"
					),
					@ApiResponse(
							description = "Book with specified id not found",
							responseCode = "400",
							ref = "#/components/responses/400Response"
					)
			}
	)
	@DeleteMapping("/{id}")
	public void deleteBook(@PathVariable long id) {
		bookService.deleteBook(id);
	}


	@Operation(
			description = "Returns titles of all borrowed books",
			summary = "Borrowed books titles",
			responses = {
					@ApiResponse(
							description = "Borrowed books titles",
							responseCode = "200",
							content = @Content(
									mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = String.class))
							)
					)
			}
	)
	@GetMapping("/borrowed/titles")
	public List<String> borrowedBooksTitles() {
		return bookService.getBorrowedBooksTitle();
	}
	@Operation(
			description = "Returns info about all borrowed books",
			summary = "Borrowed books info",
			responses = {
					@ApiResponse(
							description = "Borrowed books info",
							responseCode = "200",
							content = @Content(
									mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = BorrowedBooksInfo.class))
							)
					)
			}
	)
	@GetMapping("/borrowed")
	public List<BorrowedBooksInfo> borrowedBooks() {
		return bookService.getBorrowedBooksInfo();
	}
}
