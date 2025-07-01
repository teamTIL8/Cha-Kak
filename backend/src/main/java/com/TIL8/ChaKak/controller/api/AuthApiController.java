package com.TIL8.ChaKak.controller.api;

import com.TIL8.ChaKak.domain.User;
import com.TIL8.ChaKak.dto.request.SignUpRequestDto;
import com.TIL8.ChaKak.dto.request.UpdateUserRequestDto;
import com.TIL8.ChaKak.dto.response.UserResponseDto;
import com.TIL8.ChaKak.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

	private final UserService userService;

	/**
	 * 회원가입 API
	 * 
	 * @param signUpRequestDto 회원가입 요청 DTO
	 * @return 회원가입 성공 시 201 Created 응답과 사용자 정보
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
		try {
			User newUser = userService.signUp(signUpRequestDto);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(UserResponseDto.from(newUser));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * 아이디 중복 확인 API
	 * @param userId 확인할 아이디
	 * @return 중복 여부 (true: 사용 가능, false: 중복)
	 */
	@GetMapping("/check-userId/{userId}")
	public ResponseEntity<Boolean> checkUserIdAvailability(@PathVariable String userId) {
		boolean isAvailable = userService.isUserIdAvailable(userId);
		return ResponseEntity.ok(isAvailable);
	}

	/**
	 * 현재 로그인한 사용자 정보 조회 API
	 * 
	 * @return 로그인한 사용자 정보 (UserResponseDto)
	 */
	@GetMapping("/me")
	public ResponseEntity<UserResponseDto> getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getName())) {
			String userId = authentication.getName();
			Optional<User> userOptional = userService.findUserByUserId(userId);

			if (userOptional.isPresent()) {
				return ResponseEntity.ok(UserResponseDto.from(userOptional.get()));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	/**
	 * 회원 정보 수정 API
	 * 
	 * @param updateUserRequestDto 수정 요청 DTO
	 * @param authentication       현재 인증 정보 (로그인한 사용자)
	 * @return 수정된 사용자 정보 (UserResponseDto) 또는 오류 응답
	 */
	@PutMapping("/update")
	public ResponseEntity<UserResponseDto> updateProfile(@Valid @RequestBody UpdateUserRequestDto updateUserRequestDto,
			Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getName())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String userId = authentication.getName(); // 현재 로그인한 사용자의 ID
		User updatedUser = userService.updateUser(userId, updateUserRequestDto);
		return ResponseEntity.ok(UserResponseDto.from(updatedUser));
	}

	/**
	 * 회원 탈퇴 API (소프트 삭제)
	 * @param authentication 현재 인증 정보 (로그인한 사용자)
	 * @return 성공 시 204 No Content 응답
	 */
	@DeleteMapping("/withdraw")
	public ResponseEntity<Void> withdrawUser(Authentication authentication,
											 HttpServletRequest request, HttpServletResponse response) {
		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getName())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String userId = authentication.getName();
		userService.withdrawUser(userId);

		// 사용자 탈퇴 후 즉시 로그아웃 처리
		new SecurityContextLogoutHandler().logout(request, response, authentication);
		SecurityContextHolder.clearContext(); // SecurityContext를 명시적으로 비움

		return ResponseEntity.noContent().build(); // 204 No Content
	}
}