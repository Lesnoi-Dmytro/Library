package org.nerdy.soft.library.service;

import lombok.RequiredArgsConstructor;
import org.nerdy.soft.library.data.Book;
import org.nerdy.soft.library.data.Member;
import org.nerdy.soft.library.repositiry.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	public Page<Member> getMembers(int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);

		return memberRepository.findAll(pageRequest);
	}

	public Member getMemberById(long id) {
		return memberRepository.findById(id).orElseThrow(
				() -> new NoSuchElementException("Member with id " + id + " not found"));
	}

	public Member createMember(Member member) {
		member.setId(0);
		member.setMembershipDate(LocalDate.now());

		return memberRepository.save(member);
	}

	public Member updateMember(Member member) {
		getMemberById(member.getId());
		return memberRepository.save(member);
	}

	public void deleteMember(long id) {
		Member member = getMemberById(id);
		if (member.getBorrowed().isEmpty()) {
			memberRepository.delete(member);
		} else {
			throw new IllegalStateException("Member with id " + id + " borrowed a book");
		}
	}

	public Member getMemberByName(String name) {
		return memberRepository.findByName(name).orElseThrow(
				() -> new NoSuchElementException("Member with name '" + name + "' not found"));
	}

	public List<Book> findBorrowedByName(String name) {
		Member member = getMemberByName(name);

		return member.getBorrowed()
				.stream()
				.map(b -> b.getId().getBook())
				.toList();
	}
}
