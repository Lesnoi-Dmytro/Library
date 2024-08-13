package org.nerdy.soft.library.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nerdy.soft.library.data.borrow.Borrow;
import org.nerdy.soft.library.data.borrow.History;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@NotBlank
	@Column(name = "member_name")
	private String name;

	@Column(name = "membership_date")
	private LocalDate membershipDate;


	@OneToMany(mappedBy = "id.member")
	@JsonIgnore
	private List<Borrow> borrowed;

	@OneToMany(mappedBy = "id.member")
	@JsonIgnore
	private List<History> history;
}
