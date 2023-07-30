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
		UserEntity user = UserEntityValidator.getInstance().validUserEntity(userEntity);
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
		UserEntity user = UserEntityValidator.getInstance().validUserEntity(userEntity);
		userRepository.save(user);
		return user;
	}


}

