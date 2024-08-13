package org.nerdy.soft.library.data.borrow;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Entity
@Table(name = "borrowed")
public class Borrowed extends BorrowInfo {
	public Borrowed(BorrowId id) {
		super(id);
	}
}
