package com.chakak.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

	@NotBlank(message = "사용자 ID는 필수입니다.")
	private String userId;

	@NotBlank(message = "비밀번호는 필수입니다.")
	private String password;
}