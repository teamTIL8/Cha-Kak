package com.chakak.dto.response;

import com.chakak.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
	private String userId;
	private String email;
	private String name;
	private String role;

	public static UserResponseDto from(User user) {
		UserResponseDto dto = new UserResponseDto();
		dto.setUserId(user.getUserId());
		dto.setEmail(user.getEmail());
		dto.setName(user.getName());
		dto.setRole(user.getRole().name());
		return dto;
	}
}
