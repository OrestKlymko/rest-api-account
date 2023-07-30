package com.example.api.user;

import com.example.api.user.exception.PasswordEmptyException;
import com.example.api.user.exception.UsernameEmptyException;
import com.example.api.user.exception.ValidateEmailException;
import lombok.Getter;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class UserEntityValidator {
	@Getter
	private static final UserEntityValidator instance = new UserEntityValidator();
	private Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(32, 64, 1, 15 * 1024, 2);
	private UserEntityValidator() {
	}

	public UserEntity validUserEntity(UserEntity userEntity) throws UsernameEmptyException, ValidateEmailException, PasswordEmptyException {
		String password = userEntity.getPassword();
		String email = userEntity.getEmail();
		String regexPattern = "^(.+)@(\\S+)$";


		UserEntity newUser = new UserEntity();

		if (!userEntity.getUsername().isEmpty()) {
			newUser.setUsername(userEntity.getUsername());
		} else {
			throw new UsernameEmptyException("Username can not be empty");
		}

		if (email != null && !email.isEmpty() && email.matches(regexPattern)) {
			newUser.setEmail(email);
		} else {
			throw new ValidateEmailException("It's not email.");
		}

		if (password != null && !password.isEmpty()) {
			String encodedPassword = passwordEncoder.encode(password);
			newUser.setPassword(encodedPassword);
		} else {
			throw new PasswordEmptyException("Password cannot be empty.");
		}
		return userEntity;
	}
}

