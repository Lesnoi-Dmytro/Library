package org.nerdy.soft.library.repositiry.borrow;

import org.nerdy.soft.library.data.borrow.BorrowId;
import org.nerdy.soft.library.data.borrow.Borrowed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowedRepository
		extends JpaRepository<Borrowed, BorrowId> {
}
