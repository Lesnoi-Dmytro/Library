package org.nerdy.soft.library.controller;

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
public class BorrowController {
	private final BookService bookService;
	private final MemberService memberService;
	private final BorrowService borrowService;

	@PostMapping
	public void borrowBook(@RequestBody BorrowRequest request) {
		Book book = bookService.getBookById(request.bookId());
		Member member = memberService.getMemberById(request.memberId());

		borrowService.borrowBook(book, member);
	}

	@PostMapping("/return")
	public void returnBook(@RequestBody BorrowRequest request) {
		Book book = bookService.getBookById(request.bookId());
		Member member = memberService.getMemberById(request.memberId());

		borrowService.returnBook(book, member);
	}
}
