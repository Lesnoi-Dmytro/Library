package org.nerdy.soft.library.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddBookRequest {
	@Schema(example = "A Tale of Two Cities")
	private String title;

	@Schema(example = "Charles Dickens")
	private String author;

	@Schema(example = "10")
	private int amount;
}
