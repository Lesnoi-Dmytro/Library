package org.nerdy.soft.library.data.borrow;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Table(name = "borrowed")
public class Borrow {
	@EmbeddedId
	private BorrowId id;

	@Column(name = "borrow_date")
	private LocalDate borrowDate;
}
