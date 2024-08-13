package org.nerdy.soft.library.controller;

import lombok.RequiredArgsConstructor;
import org.nerdy.soft.library.service.BookService;
import org.nerdy.soft.library.service.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/borrow")
@RequiredArgsConstructor
public class BorrowController {
	private final BookService bookService;
	private final MemberService memberService;


}
