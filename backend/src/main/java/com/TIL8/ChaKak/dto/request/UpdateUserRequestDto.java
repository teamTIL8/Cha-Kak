package com.TIL8.ChaKak.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequestDto {
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;

	@Size(min = 8, max = 50, message = "비밀번호는 8자 이상 50자 이하로 입력해주세요.")
	private String password; // 비밀번호는 필수가 아님 (변경을 원할 때만 입력)
}
