package org.nerdy.soft.library.data.borrow;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MappedSuperclass;
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
	@Schema(example = "2024-08-13")
	protected LocalDate borrowDate;

	public BorrowInfo(BorrowId id) {
		this.id = id;
		this.borrowDate = LocalDate.now();
	}
}
