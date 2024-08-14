package org.nerdy.soft.library.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nerdy.soft.library.data.Book;
import org.nerdy.soft.library.data.Member;
import org.nerdy.soft.library.request.BorrowRequest;
import org.nerdy.soft.library.service.BookService;
import org.nerdy.soft.library.service.BorrowService;
import org.nerdy.soft.library.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/borrow")
@RequiredArgsConstructor
@Tag(
		name = "Borrow",
		description = "Borrow operations"
)
public class BorrowController {
	private final BookService bookService;
	private final MemberService memberService;
	private final BorrowService borrowService;

	@Operation(
			description = "Borrow a book by member. Throws an exception if member or book is not found. " +
					"Also throws an exception if member reached borrow limit, there is no free book or member already borrowed this book",
			summary = "Borrow book",
			responses = {
					@ApiResponse(
							description = "Book successfully borrowed by a member",
							responseCode = "200"
					),
					@ApiResponse(
							description = "Member can't borrow this book",
							responseCode = "400",
							ref = "#/components/responses/400Response"
					)
			}
	)
	@PostMapping
	public void borrowBook(@RequestBody BorrowRequest request) {
		Book book = bookService.getBookById(request.bookId());
		Member member = memberService.getMemberById(request.memberId());

		borrowService.borrowBook(book, member);
	}

	@Operation(
			description = "Return a book by member. Throws an exception if member or book is not found. " +
					"Also throws an exception if member didn't borrowed this book",
			summary = "Return book",
			responses = {
					@ApiResponse(
							description = "Book successfully returned by a member ",
							responseCode = "200"
					),
					@ApiResponse(
							description = "Member can't return this book",
							responseCode = "400",
							ref = "#/components/responses/400Response"
					)
			}
	)
	@PostMapping("/return")
	public void returnBook(@RequestBody BorrowRequest request) {
		Book book = bookService.getBookById(request.bookId());
		Member member = memberService.getMemberById(request.memberId());

		borrowService.returnBook(book, member);
	}
}
