package org.nerdy.soft.library.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
public class ErrorResponse {
	@Schema(example = "IllegalArgumentException")
	private String exception;

	@Schema(example = "Item with this id not found")
	private String message;

	@Schema(example = "Bad Request")
	private String code;

	@Schema(example = "400")
	private int status;

	public ErrorResponse(Exception ex, HttpStatusCode code) {
		exception = ex.getClass().getSimpleName();
		message = ex.getMessage();
		this.code = code.toString();
		status = code.value();
	}
}
