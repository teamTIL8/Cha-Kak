package com.chakak.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;

	@Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하여야 합니다.")
	private String name;

	private String password;

	private String currentPassword;

	public boolean isPasswordChangeRequested() {
		return password != null && !password.trim().isEmpty();
	}
}