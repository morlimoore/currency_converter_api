package com.morlimoore.currencyconverterapi.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false, length = 40)
	private String email;

	@Column(unique = true, nullable = false, length = 40)
	private String username;

	@Column(nullable = false)
	private String password;

	@ManyToOne(cascade = CascadeType.ALL)
	private Role role;
}
