package org.nerdy.soft.library.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record BorrowRequest(
		@Schema(example = "1") int bookId,
		@Schema(example = "1") int memberId) {
}
