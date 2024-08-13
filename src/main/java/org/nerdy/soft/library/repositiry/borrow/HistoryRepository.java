package org.nerdy.soft.library.repositiry.borrow;

import org.nerdy.soft.library.data.borrow.BorrowId;
import org.nerdy.soft.library.data.borrow.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository
		extends JpaRepository<History, BorrowId> {
}
