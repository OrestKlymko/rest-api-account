package com.example.api.user;


import com.example.api.user.exception.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@GetMapping("/list")
	public ResponseEntity getAllUsers() {
		return ResponseEntity.ok(userRepository.findAll());
	}

	@PostMapping("/create")
	public ResponseEntity createUser(@RequestBody UserEntity userEntity) {
		try {
			return ResponseEntity.ok(userService.registration(userEntity));
		} catch (PasswordEmptyException | ValidateEmailException | UsernameEmptyException | UserAlreadyExistException e ){
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e){
			return ResponseEntity.badRequest().body("Something wrong...");
		}
	}

	@PostMapping("/update")
	public ResponseEntity updateUser(@RequestBody UserEntity userEntity) {
		try {
			return ResponseEntity.ok(userService.updateUser(userEntity));
		} catch (ValidateEmailException | UsernameEmptyException | PasswordEmptyException | UserNotFoundException e ){
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e){
			return ResponseEntity.badRequest().body("Something wrong...");
		}
	}

	@PostMapping("/login")
	public ResponseEntity loginUser(@RequestBody UserEntity userEntity) {
		try {
			return ResponseEntity.ok(userService.login(userEntity));
		}catch (UserNotFoundException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		catch (Exception e){
			return ResponseEntity.badRequest().body("Something wrong...");
		}
	}
}
