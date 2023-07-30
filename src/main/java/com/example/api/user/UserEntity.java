package com.example.api.user;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@Table(name = "users")
@Entity
@Data
public class UserEntity {
	@Id
	@Column(name = "email")
	private String email;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
}
