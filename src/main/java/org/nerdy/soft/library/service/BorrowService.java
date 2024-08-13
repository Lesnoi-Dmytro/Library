package org.nerdy.soft.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorrowService {

	@Value("${borrow.limit}")
	private int limit;
}
