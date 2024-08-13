package org.nerdy.soft.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.nerdy.soft.library.data.Member;
import org.nerdy.soft.library.data.borrow.Borrowed;
import org.nerdy.soft.library.repositiry.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MemberServiceTest {
	@Autowired
	MemberService memberService;

	@MockBean
	MemberRepository memberRepository;

	@Test
	void getMembersTest() {
		int page = 0;
		int size = 10;
		PageRequest req = PageRequest.of(page, size);

		Page<Member> members = Page.empty();

		when(memberRepository.findAll(req))
				.thenReturn(members);

		assertEquals(members, memberService.getMembers(page, size));
	}

	@Nested
	class GetMemberByIdTest {
		final long id = 1;
		Member member = mock(Member.class);

		<T> void testCorrectId(T value, Supplier<T> method) {
			when(memberRepository.findById(id))
					.thenReturn(Optional.of(member));

			assertEquals(value, method.get());
		}

		@Test
		void correctId() {
			testCorrectId(member,
					() -> memberService.getMemberById(id));
		}

		<T> void testIncorrectId(Supplier<T> method) {
			when(memberRepository.findById(id))
					.thenReturn(Optional.empty());

			assertThrows(NoSuchElementException.class, method::get);
		}

		@Test
		void incorrectId() {
			testIncorrectId(() -> memberService.getMemberById(id));
		}
	}

	@Test
	void createMemberTest() {
		Member member = mock(Member.class);

		when(memberRepository.save(member))
				.thenReturn(member);

		assertEquals(member, memberService.createMember(member));

		verify(member).setId(0);
		verify(member).setMembershipDate(LocalDate.now());
	}

	@Nested
	class UpdateMemberTest extends GetMemberByIdTest {

		@Test
		@Override
		void correctId() {
			when(member.getId()).thenReturn(id);
			when(memberRepository.save(member))
					.thenReturn(member);

			testCorrectId(member,
					() -> memberService.updateMember(member));

			verify(memberRepository).save(member);
		}

		@Test
		@Override
		void incorrectId() {
			when(member.getId()).thenReturn(id);
			testIncorrectId(() -> memberService.updateMember(member));

			verify(memberRepository, never()).save(member);
		}
	}

	@Nested
	class DeleteMemberTest extends GetMemberByIdTest {
		List<Borrowed> borrowed;

		@BeforeEach
		void setupTest() {
			borrowed = new ArrayList<>();
			when(member.getBorrowed()).thenReturn(borrowed);
		}

		@Test
		@DisplayName("correctIdNotBorrow")
		@Override
		void correctId() {
			testCorrectId(true, () -> {
				memberService.deleteMember(id);
				return true;
			});

			verify(memberRepository).delete(member);
		}

		void correctIdBorrow() {
			borrowed.add(mock(Borrowed.class));

			assertThrows(IllegalStateException.class,
					() -> memberService.deleteMember(id));

			verify(memberRepository, never()).delete(member);
		}

		@Override
		void incorrectId() {
			testIncorrectId(() -> {
				memberService.deleteMember(id);
				return true;
			});

			verify(memberRepository, never()).delete(member);
		}
	}

	@Nested
	class GetMemberByNameTest {
		String name = "John Doe";

		@Test
		void correctNameTest() {
			Member member = mock(Member.class);

			when(memberRepository.findByName(name))
					.thenReturn(Optional.of(member));

			assertEquals(member, memberService.getMemberByName(name));
		}

		@Test
		void inCorrectNameTest() {
			when(memberRepository.findByName(name))
					.thenReturn(Optional.empty());

			assertThrows(NoSuchElementException.class,
					() -> memberService.getMemberByName(name));
		}
	}
}