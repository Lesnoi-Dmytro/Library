package org.nerdy.soft.library.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nerdy.soft.library.data.Book;
import org.nerdy.soft.library.data.Member;
import org.nerdy.soft.library.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(
		name = "Members",
		description = "Member CRUD operations"
)
public class MemberController {
	private final MemberService memberService;

	@Operation(
			description = "All members retrieval. Results are pageable",
			summary = "All members",
			parameters = {
					@Parameter(
							in = ParameterIn.QUERY,
							required = true,
							name = "page",
							description = "Page number, starts from 0",
							example = "1"),
					@Parameter(
							in = ParameterIn.QUERY,
							required = true,
							name = "size",
							description = "Page size",
							example = "20")
			},
			responses = {
					@ApiResponse(
							description = "Page of members",
							responseCode = "200"
					)
			}
	)
	@GetMapping
	public Page<Member> getAllMembers(@RequestParam int page,
									  @RequestParam int size) {
		return memberService.getMembers(page, size);
	}

	@Operation(
			description = "Returns member by id. Throws error if there is no such member",
			summary = "Member by id",
			parameters = {
					@Parameter(
							in = ParameterIn.PATH,
							required = true,
							name = "id",
							description = "Member id",
							example = "1")
			},
			responses = {
					@ApiResponse(
							description = "Member with specified id",
							responseCode = "200",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = Member.class)
							)
					),
					@ApiResponse(
							description = "Member with specified id not found",
							responseCode = "400",
							ref = "#/components/responses/400Response"
					)
			}
	)
	@GetMapping("/{id}")
	public Member getMemberById(@PathVariable long id) {
		return memberService.getMemberById(id);
	}

	@Operation(
			description = "Create a member. Membership date assigned automatically",
			summary = "Create member",
			responses = {
					@ApiResponse(
							description = "Created member",
							responseCode = "200",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = Member.class)
							)
					)
			}
	)
	@PostMapping
	public Member createMember(@RequestBody Member member) {
		return memberService.createMember(member);
	}

	@Operation(
			description = "Update a member. Throws error if there is no such book",
			summary = "Update member",
			responses = {
					@ApiResponse(
							description = "Updated member",
							responseCode = "200",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = Member.class)
							)
					),
					@ApiResponse(
							description = "Member with specified id not found",
							responseCode = "400",
							ref = "#/components/responses/400Response"
					)
			}
	)
	@PutMapping
	public Member updateBook(@RequestBody Member member) {
		return memberService.updateMember(member);
	}

	@Operation(
			description = "Delete a member. Throws error if there is no such book",
			summary = "Delete member",
			responses = {
					@ApiResponse(
							description = "Member deleted successfully",
							responseCode = "200",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = Member.class)
							)
					),
					@ApiResponse(
							description = "Member with specified id not found",
							responseCode = "400",
							ref = "#/components/responses/400Response"
					)
			}
	)
	@DeleteMapping("/{id}")
	public void deleteMember(@PathVariable long id) {
		memberService.deleteMember(id);
	}

	@Operation(
			description = "All books borrowed by member with a specified name. Throws error if there is no such member",
			summary = "Borrowed books by name",
			parameters = {
					@Parameter(in = ParameterIn.PATH,
							required = true,
							description = "Member name",
							example = "John Doe")
			},
			responses = {
					@ApiResponse(
							description = "Updated member",
							responseCode = "200",
							content = @Content(
									mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = Book.class))
							)
					),
					@ApiResponse(
							description = "Member with specified name not found",
							responseCode = "400",
							ref = "#/components/responses/400Response"
					)
			}
	)
	@GetMapping("/{name}/borrowed")
	public List<Book> borrowedBooks(@PathVariable String name) {
		return memberService.findBorrowedByName(name);
	}
}
