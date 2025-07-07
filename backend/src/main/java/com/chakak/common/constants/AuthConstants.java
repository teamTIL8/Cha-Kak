package com.chakak.common.constants;

public final class AuthConstants {

	// Cookie
	public static final String TOKEN_COOKIE_NAME = "token";
	public static final int TOKEN_COOKIE_MAX_AGE = 24 * 60 * 60; // 24 hours

	// Spring Security
	public static final String ANONYMOUS_USER = "anonymousUser";

	// Model Attributes
	public static final String ATTR_IS_LOGGED_IN = "isLoggedIn";
	public static final String ATTR_USER = "user";
	public static final String ATTR_LOGIN_DTO = "loginDto";
	public static final String ATTR_REGISTER_DTO = "registerDto";
	public static final String ATTR_UPDATE_DTO = "updateDto";
	public static final String ATTR_MESSAGE = "message";
	public static final String ATTR_ERROR = "error";

	// View Names
	public static final String VIEW_INDEX = "index";
	public static final String VIEW_LOGIN = "auth/login";
	public static final String VIEW_REGISTER = "auth/register";
	public static final String VIEW_MYPAGE = "mypage";
	public static final String VIEW_EDIT = "auth/edit";

	// Redirect URLs
	public static final String REDIRECT_HOME = "redirect:/";
	public static final String REDIRECT_LOGIN = "redirect:/login";
	public static final String REDIRECT_MYPAGE = "redirect:/mypage";

	// Validation Error Codes
	public static final String ERROR_CODE_PASSWORD = "error.password";
	public static final String ERROR_CODE_USER_ID = "error.userId";
	public static final String ERROR_CODE_EMAIL = "error.email";
	public static final String ERROR_CODE_NAME = "error.name";
	public static final String ERROR_CODE_CURRENT_PASSWORD = "error.currentPassword";

	// Validation Messages
	public static final String MSG_INVALID_CREDENTIALS = "사용자 ID 또는 비밀번호가 올바르지 않습니다.";
	public static final String MSG_REGISTRATION_SUCCESS = "회원가입이 완료되었습니다. 로그인해주세요.";
	public static final String MSG_REGISTRATION_ERROR = "회원가입 중 오류가 발생했습니다.";
	public static final String MSG_UPDATE_SUCCESS = "회원정보가 성공적으로 수정되었습니다.";
	public static final String MSG_UPDATE_SUCCESS_WITH_PASSWORD = " 회원정보가 성공적으로 수정되었습니다. 비밀번호도 변경되었습니다.";
	public static final String MSG_WITHDRAWAL_SUCCESS = "회원탈퇴가 완료되었습니다. 30일 후에 동일한 이메일로 재가입이 가능합니다.";
	public static final String MSG_WITHDRAWAL_ERROR = "회원탈퇴 처리 중 오류가 발생했습니다: ";
	public static final String MSG_EMAIL_REQUIRED = "이메일은 필수입니다.";
	public static final String MSG_EMAIL_INVALID = "올바른 이메일 형식이 아닙니다.";
	public static final String MSG_NAME_REQUIRED = "이름은 필수입니다.";
	public static final String MSG_NAME_LENGTH = "이름은 2자 이상 50자 이하여야 합니다.";
	public static final String MSG_CURRENT_PASSWORD_REQUIRED = "보안을 위해 현재 비밀번호를 입력해주세요.";
	public static final String MSG_CURRENT_PASSWORD_INVALID = "현재 비밀번호가 올바르지 않습니다.";
	public static final String MSG_NEW_PASSWORD_REQUIRED = "새 비밀번호를 입력해주세요.";
	public static final String MSG_NEW_PASSWORD_LENGTH_MIN = "새 비밀번호는 6자 이상이어야 합니다.";
	public static final String MSG_NEW_PASSWORD_LENGTH_MAX = "새 비밀번호는 100자 이하여야 합니다.";
	public static final String MSG_NEW_PASSWORD_SAME_AS_CURRENT = "새 비밀번호는 현재 비밀번호와 달라야 합니다.";
	public static final String MSG_UPDATE_ERROR = "수정 중 오류가 발생했습니다: ";

	// Registration Messages
	public static final String MSG_DUPLICATE_USER_ID = "이미 사용 중인 사용자 ID입니다.";
	public static final String MSG_DUPLICATE_EMAIL = "이미 사용 중인 이메일입니다.";
	public static final String MSG_WITHDRAWN_EMAIL_RE_REGISTRATION_WAIT = "탈퇴한 회원의 이메일입니다. %s 후에 재가입이 가능합니다.";
	public static final String MSG_DUPLICATE_ENTRY = "이미 사용 중인 정보가 있습니다.";
	public static final String MSG_REGISTRATION_UNEXPECTED_ERROR = "회원가입 중 예상치 못한 오류가 발생했습니다.";
	public static final String MSG_USER_ID_AVAILABILITY_CHECK_ERROR = "사용자 ID 중복 확인 중 오류가 발생했습니다.";
	public static final String MSG_EMAIL_AVAILABILITY_CHECK_ERROR = "이메일 중복 확인 중 오류가 발생했습니다.";
	public static final String MSG_CONSTRAINT_VIOLATION_CHECK_ERROR = "데이터 제약조건 확인 중 오류가 발생했습니다.";

	private AuthConstants() {
		// Prevent instantiation
	}
}