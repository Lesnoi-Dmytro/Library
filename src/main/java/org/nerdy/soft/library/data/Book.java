package org.nerdy.soft.library.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nerdy.soft.library.data.borrow.Borrow;
import org.nerdy.soft.library.data.borrow.History;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "title")
	@NotBlank
	@Pattern(regexp = "^[A-Z].*")
	@Size(min = 3, max = 50)
	private String title;

	@Column(name = "author")
	@NotBlank
	@Pattern(regexp = "^[A-Z].* [A-Z].*")
	@Size(min = 1, max = 50)
	private String author;

	@OneToMany(mappedBy = "id.book")
	@JsonIgnore
	private List<Borrow> borrowed;

	@OneToMany(mappedBy = "id.book")
	@JsonIgnore
	private List<History> history;

	private int amount;

	public void addAmount(int amount) {
		this.amount += amount;
	}

	public void subtractAmount(int amount) {
		this.amount -= amount;
	}
}
