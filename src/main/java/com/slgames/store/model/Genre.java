package com.slgames.store.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.slgames.store.dtos.genre.GenreDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "genres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Genre{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "genre_id")
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(name = "genre_name")
	private GenreName genreName;
	@ManyToMany(mappedBy = "genres")
	@JsonBackReference
	private Set<Game> games;
	
	
	public Genre(GenreDTO dto) {
		setGenreName(dto.genreName());
	}
	
	public Genre(GenreName name) {
		setGenreName(name);
	}

	public Genre(long id, GenreName name) {
		setId(id);
		setGenreName(name);
	}
	
}