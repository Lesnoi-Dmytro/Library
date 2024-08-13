package org.nerdy.soft.library.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nerdy.soft.library.data.borrow.Borrowed;
import org.nerdy.soft.library.data.borrow.History;

import java.time.LocalDate;
import java.util.ArrayList;
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
	@Schema(example = "1")
	private long id;

	@NotBlank
	@Column(name = "member_name")
	@Schema(example = "John Doe")
	private String name;

	@Column(name = "membership_date")
	@Schema(example = "2024-08-13")
	private LocalDate membershipDate;


	@OneToMany(mappedBy = "id.member")
	@JsonIgnore
	private List<Borrowed> borrowed;

	@OneToMany(mappedBy = "id.member")
	@JsonIgnore
	private List<History> history;

	public void addBorrowed(Borrowed borrow) {
		if (borrowed == null) {
			borrowed = new ArrayList<>();
		}
		borrowed.add(borrow);
	}

	public void removeBorrowed(Borrowed borrow) {
		if (borrowed != null) {
			borrowed.remove(borrow);
		}
	}

	public void addHistory(History history) {
		if (this.history == null) {
			this.history = new ArrayList<>();
		}
		this.history.add(history);
	}

	public void removeHistory(History history) {
		if (history != null) {
			this.history.remove(history);
		}
	}

	@Override
	public String toString() {
		return "Member{" +
				"name='" + name + '\'' +
				", id=" + id +
				", membershipDate=" + membershipDate +
				'}';
	}
}
