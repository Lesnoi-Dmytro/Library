package org.nerdy.soft.library.data.borrow;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BorrowInfo {
	@EmbeddedId
	protected BorrowId id;

	@Column(name = "borrow_date")
	protected LocalDate borrowDate;

	public BorrowInfo(BorrowId id) {
		this.id = id;
		this.borrowDate = LocalDate.now();
	}
}
