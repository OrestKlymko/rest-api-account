package com.example.api.user;


import lombok.Data;

@Data
public class UserModel {
	private String email;
	private String username;

	public static UserModel toModel(UserEntity userEntity){
		UserModel userModel = new UserModel();
		userModel.setEmail(userEntity.getEmail());
		userModel.setUsername(userEntity.getUsername());
		return userModel;
	}
}
