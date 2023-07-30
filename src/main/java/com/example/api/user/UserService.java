package com.example.api.user;

import com.example.api.user.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
	@Autowired
	private final UserRepository userRepository;
	private Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(32, 64, 1, 15 * 1024, 2);

	public UserModel registration(UserEntity userEntity) throws PasswordEmptyException, ValidateEmailException, UsernameEmptyException, UserAlreadyExistException {
		UserEntity userEntityByEmail = userRepository.getUserEntityByEmail(userEntity.getEmail());
		if (userEntityByEmail != null) {
			throw new UserAlreadyExistException("User with this email already exist");
		}
		UserEntity user = validUserEntity(userEntity);
		userRepository.save(user);
		return UserModel.toModel(userEntity);
	}

	public boolean login(UserEntity userEntity) throws UserNotFoundException {
		UserEntity userEntitiesByEmail = userRepository.getUserEntityByEmail(userEntity.getEmail());
		if (userEntitiesByEmail != null) {
			return passwordEncoder.matches(userEntity.getPassword(), userEntitiesByEmail.getPassword());
		} else {
			throw new UserNotFoundException("User not found with email " + userEntity.getEmail() + "not found");
		}
	}

	public UserEntity updateUser(UserEntity userEntity) throws ValidateEmailException, UsernameEmptyException, PasswordEmptyException, UserNotFoundException {
		UserEntity userEntityByEmail = userRepository.getUserEntityByEmail(userEntity.getEmail());
		if (userEntityByEmail == null) {
			throw new UserNotFoundException("User not found with email " + userEntity.getEmail() + " not found");
		}
		UserEntity user = validUserEntity(userEntity);
		userRepository.save(user);
		return user;
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

