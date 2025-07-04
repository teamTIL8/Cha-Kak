package com.chakak.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

	@NotBlank(message = "사용자 ID는 필수입니다.")
	@Size(min = 4, max = 20, message = "사용자 ID는 4자 이상 20자 이하여야 합니다.")
	@Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "사용자 ID는 영문, 숫자, 언더스코어만 사용 가능합니다.")
	private String userId;

	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;

	@NotBlank(message = "이름은 필수입니다.")
	@Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하여야 합니다.")
	private String name;

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
	private String password;
}