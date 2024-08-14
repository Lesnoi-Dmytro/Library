package org.nerdy.soft.library.controller;

import lombok.RequiredArgsConstructor;
import org.nerdy.soft.library.data.Book;
import org.nerdy.soft.library.data.Member;
import org.nerdy.soft.library.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	@GetMapping
	public Page<Member> getAllMembers(@RequestParam int page,
								  @RequestParam int size) {
		return memberService.getMembers(page, size);
	}

	@GetMapping("/{id}")
	public Member getMemberById(@PathVariable long id) {
		return memberService.getMemberById(id);
	}

	@PostMapping
	public Member createMember(@RequestBody Member member) {
		return memberService.createMember(member);
	}

	@PutMapping
	public Member updateBook(@RequestBody Member member) {
		return memberService.updateMember(member);
	}

	@DeleteMapping("/{id}")
	public void deleteMember(@PathVariable long id) {
		memberService.deleteMember(id);
	}

	@GetMapping("/{name}/borrowed")
	public List<Book> borrowedBooks(@PathVariable String name) {
		return memberService.findBorrowedByName(name);
	}
}
