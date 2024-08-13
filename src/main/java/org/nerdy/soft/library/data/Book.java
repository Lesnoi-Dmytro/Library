package org.nerdy.soft.library.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nerdy.soft.library.data.borrow.Borrowed;
import org.nerdy.soft.library.data.borrow.History;

import java.util.ArrayList;
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

	private int amount;

	@OneToMany(mappedBy = "id.book")
	@JsonIgnore
	private List<Borrowed> borrowed;

	@OneToMany(mappedBy = "id.book")
	@JsonIgnore
	private List<History> history;

	public void addAmount(int amount) {
		this.amount += amount;
	}

	public void subtractAmount(int amount) {
		this.amount -= amount;
	}

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
		if (this.history != null) {
			this.history.remove(history);
		}
	}

	@Override
	public String toString() {
		return "Book{" +
				"id=" + id +
				", title='" + title + '\'' +
				", author='" + author + '\'' +
				", amount=" + amount +
				'}';
	}
}
