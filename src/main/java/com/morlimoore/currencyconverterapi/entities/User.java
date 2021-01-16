package com.morlimoore.currencyconverterapi.entities;

import com.morlimoore.currencyconverterapi.util.RoleEnum;
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
public class User extends BaseEntity {

	@Column(unique = true, nullable = false, length = 40)
	private String email;

	@Column(unique = true, nullable = false, length = 40)
	private String username;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	private RoleEnum role;
}
