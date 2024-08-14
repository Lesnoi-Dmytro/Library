package org.nerdy.soft.library.data.borrow;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "history")
public class History extends BorrowInfo {
	public History(BorrowInfo borrow) {
		super(borrow.getId(), borrow.getBorrowDate());
		this.returnDate = LocalDate.now();
	}

	@Column(name = "return_date")
	@Schema(example = "2024-08-13")
	private LocalDate returnDate;
}
