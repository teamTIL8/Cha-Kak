package com.chakak.controller;

import com.chakak.domain.User;
import com.chakak.dto.request.UserLoginDto;
import com.chakak.dto.request.UserRegisterDto;
import com.chakak.dto.request.UserUpdateDto;
import com.chakak.service.UserService;
import com.chakak.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final UserService userService;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 홈페이지 (/)
	 */
	@GetMapping("/")
	public String home(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 로그인 상태 확인
		boolean isLoggedIn = authentication != null && authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getPrincipal());

		model.addAttribute("isLoggedIn", isLoggedIn);

		if (isLoggedIn) {
			User user = (User) authentication.getPrincipal();
			model.addAttribute("user", user);
		}

		return "index";
	}

	/**
	 * 로그인 페이지
	 */
	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("loginDto", new UserLoginDto());
		return "auth/login";
	}

	/**
	 * 로그인 처리
	 */
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("loginDto") UserLoginDto loginDto, BindingResult bindingResult,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "auth/login";
		}

		try {
			User user = userService.findByUserId(loginDto.getUserId());
			log.info("Login attempt for user: {}", user.getUserId());

			if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
				bindingResult.rejectValue("password", "error.password", "사용자 ID 또는 비밀번호가 올바르지 않습니다.");
				return "auth/login";
			}

			String token = jwtUtil.generateToken(user);
			log.info("Generated JWT token for user: {}", user.getUserId());

			Cookie cookie = new Cookie("token", token);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			cookie.setMaxAge(24 * 60 * 60); // 24시간
			response.addCookie(cookie);

			log.info("Login successful for user: {}", user.getUserId());
			return "redirect:/mypage";

		} catch (RuntimeException e) {
			log.error("Login error", e);
			bindingResult.rejectValue("userId", "error.userId", "사용자 ID 또는 비밀번호가 올바르지 않습니다.");
			return "auth/login";
		}
	}

	/**
	 * 회원가입 페이지
	 */
	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("registerDto", new UserRegisterDto());
		return "auth/register";
	}

	/**
	 * 회원가입 처리
	 */
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("registerDto") UserRegisterDto registerDto,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "auth/register";
		}

		try {
			userService.register(registerDto, passwordEncoder);
			redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
			return "redirect:/login";

		} catch (RuntimeException e) {
			if (e.getMessage().contains("사용자 ID")) {
				bindingResult.rejectValue("userId", "error.userId", e.getMessage());
			} else if (e.getMessage().contains("이메일")) {
				bindingResult.rejectValue("email", "error.email", e.getMessage());
			} else {
				bindingResult.rejectValue("userId", "error.userId", "회원가입 중 오류가 발생했습니다.");
			}
			return "auth/register";
		}
	}

	/**
	 * 사용자 ID 중복 확인 (AJAX용)
	 */
	@PostMapping("/check-userid")
	@ResponseBody
	public boolean checkUserId(@RequestParam String userId) {
		return userService.isUserIdAvailable(userId);
	}

	/**
	 * 이메일 중복 확인 (AJAX용)
	 */
	@PostMapping("/check-email")
	@ResponseBody
	public boolean checkEmail(@RequestParam String email) {
		return userService.isEmailAvailable(email);
	}

	/**
	 * 마이페이지
	 */
	@GetMapping("/mypage")
	public String mypage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getPrincipal())) {
			return "redirect:/login";
		}

		User user = (User) authentication.getPrincipal();
		model.addAttribute("user", user);

		return "mypage";
	}

	/**
	 * 회원정보 수정 페이지
	 */
	@GetMapping("/edit")
	public String editPage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getPrincipal())) {
			return "redirect:/login";
		}

		User user = (User) authentication.getPrincipal();

		UserUpdateDto updateDto = new UserUpdateDto();
		updateDto.setEmail(user.getEmail());
		updateDto.setName(user.getName());

		model.addAttribute("updateDto", updateDto);
		model.addAttribute("user", user);
		return "auth/edit";
	}

	/**
	 * 회원정보 수정 처리
	 */
	@PostMapping("/edit")
	public String updateProfile(@ModelAttribute("updateDto") UserUpdateDto updateDto, BindingResult bindingResult,
			Model model, RedirectAttributes redirectAttributes,
			@RequestParam(value = "changePassword", defaultValue = "false") boolean changePassword) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();

		model.addAttribute("user", user);

		validateBasicInfo(updateDto, bindingResult);

		validateCurrentPassword(updateDto, user, bindingResult);

		if (changePassword) {
			validateNewPassword(updateDto, bindingResult);
		} else {
			updateDto.setPassword(null);
		}

		if (bindingResult.hasErrors()) {
			return "auth/edit";
		}

		try {
			userService.updateUser(user.getUserId(), updateDto, passwordEncoder);

			String message = buildSuccessMessage(changePassword);
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/mypage";

		} catch (RuntimeException e) {
			log.error("Profile update error", e);
			handleUpdateException(e, bindingResult);
			return "auth/edit";
		}
	}

	/**
	 * 로그아웃
	 */
	@PostMapping("/logout")
	public String logout(HttpServletResponse response) {
		Cookie cookie = new Cookie("token", null);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		SecurityContextHolder.clearContext();

		return "redirect:/";
	}

	/**
	 * 기본 정보(이메일, 이름) 검증
	 */
	private void validateBasicInfo(UserUpdateDto updateDto, BindingResult bindingResult) {
		if (updateDto.getEmail() == null || updateDto.getEmail().trim().isEmpty()) {
			bindingResult.rejectValue("email", "error.email", "이메일은 필수입니다.");
		} else if (!isValidEmail(updateDto.getEmail())) {
			bindingResult.rejectValue("email", "error.email", "올바른 이메일 형식이 아닙니다.");
		}

		if (updateDto.getName() == null || updateDto.getName().trim().isEmpty()) {
			bindingResult.rejectValue("name", "error.name", "이름은 필수입니다.");
		} else if (updateDto.getName().trim().length() < 2 || updateDto.getName().trim().length() > 50) {
			bindingResult.rejectValue("name", "error.name", "이름은 2자 이상 50자 이하여야 합니다.");
		}
	}

	/**
	 * 현재 비밀번호 검증 (보안 확인용)
	 */
	private void validateCurrentPassword(UserUpdateDto updateDto, User user, BindingResult bindingResult) {
		if (updateDto.getCurrentPassword() == null || updateDto.getCurrentPassword().trim().isEmpty()) {
			bindingResult.rejectValue("currentPassword", "error.currentPassword", "보안을 위해 현재 비밀번호를 입력해주세요.");
		} else if (!userService.checkPassword(user.getUserId(), updateDto.getCurrentPassword(), passwordEncoder)) {
			bindingResult.rejectValue("currentPassword", "error.currentPassword", "현재 비밀번호가 올바르지 않습니다.");
		}
	}

	/**
	 * 새 비밀번호 검증
	 */
	private void validateNewPassword(UserUpdateDto updateDto, BindingResult bindingResult) {
		String newPassword = updateDto.getPassword();
		String currentPassword = updateDto.getCurrentPassword();

		if (newPassword == null || newPassword.trim().isEmpty()) {
			bindingResult.rejectValue("password", "error.password", "새 비밀번호를 입력해주세요.");
			return;
		}

		if (newPassword.trim().length() < 6) {
			bindingResult.rejectValue("password", "error.password", "새 비밀번호는 6자 이상이어야 합니다.");
		}

		if (newPassword.trim().length() > 100) {
			bindingResult.rejectValue("password", "error.password", "새 비밀번호는 100자 이하여야 합니다.");
		}

		if (currentPassword != null && currentPassword.equals(newPassword)) {
			bindingResult.rejectValue("password", "error.password", "새 비밀번호는 현재 비밀번호와 달라야 합니다.");
		}

	}

	/**
	 * 이메일 형식 검증
	 */
	private boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		return email.matches(emailRegex);
	}

	/**
	 * 성공 메시지 생성
	 */
	private String buildSuccessMessage(boolean changePassword) {
		String message = "회원정보가 성공적으로 수정되었습니다.";
		if (changePassword) {
			message += " 비밀번호도 변경되었습니다.";
		}
		return message;
	}

	/**
	 * 업데이트 예외 처리
	 */
	private void handleUpdateException(RuntimeException e, BindingResult bindingResult) {
		if (e.getMessage().contains("이메일")) {
			bindingResult.rejectValue("email", "error.email", e.getMessage());
		} else if (e.getMessage().contains("이름")) {
			bindingResult.rejectValue("name", "error.name", e.getMessage());
		} else {
			bindingResult.rejectValue("currentPassword", "error.currentPassword", "수정 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	/**
	 * 회원탈퇴 처리
	 */
	@PostMapping("/withdraw")
	public String withdrawUser(HttpServletResponse response, RedirectAttributes redirectAttributes) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    
	    if (authentication == null || !authentication.isAuthenticated() || 
	        "anonymousUser".equals(authentication.getPrincipal())) {
	        return "redirect:/login";
	    }
	    
	    User user = (User) authentication.getPrincipal();
	    
	    try {
	        userService.withdrawUser(user.getUserId());
	        log.info("User withdrawal successful: {}", user.getUserId());
	        
	        Cookie cookie = new Cookie("token", null);
	        cookie.setHttpOnly(true);
	        cookie.setPath("/");
	        cookie.setMaxAge(0);
	        response.addCookie(cookie);
	        
	        SecurityContextHolder.clearContext();
	        
	        redirectAttributes.addFlashAttribute("message", 
	            "회원탈퇴가 완료되었습니다. 30일 후에 동일한 이메일로 재가입이 가능합니다.");
	        return "redirect:/";
	        
	    } catch (RuntimeException e) {
	        log.error("User withdrawal error", e);
	        redirectAttributes.addFlashAttribute("error", 
	            "회원탈퇴 처리 중 오류가 발생했습니다: " + e.getMessage());
	        return "redirect:/mypage";
	    }
	}
}