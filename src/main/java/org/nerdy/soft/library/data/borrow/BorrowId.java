package org.nerdy.soft.library.data.borrow;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nerdy.soft.library.data.Book;
import org.nerdy.soft.library.data.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BorrowId {
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "book_id")
	private Book book;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "member_id")
	private Member member;
}
