package org.nerdy.soft.library.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.nerdy.soft.library.exception.ErrorResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;

@OpenAPIDefinition(
		info = @Info(
				title = "Library API",
				description = "Documentation for Library website API for developers",
				version = "1.0"
		),
		servers = {
				@Server(
						url = "https://localhost:8080/api",
						description = "Development server"
				)
		}
)
@Configuration
public class OpenApiConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components()
						.addResponses("400Response", new ApiResponse()
								.content(new Content().addMediaType("application/json",
										new MediaType().schema(new Schema<ErrorResponse>())
												.example(new ErrorResponse(new IllegalArgumentException("Item with specified id not found"),
														HttpStatusCode.valueOf(400)))))));

	}
}