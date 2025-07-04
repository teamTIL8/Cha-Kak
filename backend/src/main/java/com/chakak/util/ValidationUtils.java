package com.chakak.util;

import com.chakak.common.constants.AuthConstants;
import com.chakak.domain.User;
import com.chakak.dto.request.UserUpdateDto;
import com.chakak.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

public final class ValidationUtils {

    private ValidationUtils() {
        // Prevent instantiation
    }

    /**
     * 이메일 형식 검증
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * 기본 정보(이메일, 이름) 검증
     */
    public static void validateBasicInfo(UserUpdateDto updateDto, BindingResult bindingResult) {
        if (updateDto.getEmail() == null || updateDto.getEmail().trim().isEmpty()) {
            bindingResult.rejectValue(AuthConstants.ATTR_USER, AuthConstants.ERROR_CODE_EMAIL, AuthConstants.MSG_EMAIL_REQUIRED);
        } else if (!isValidEmail(updateDto.getEmail())) {
            bindingResult.rejectValue(AuthConstants.ATTR_USER, AuthConstants.ERROR_CODE_EMAIL, AuthConstants.MSG_EMAIL_INVALID);
        }

        if (updateDto.getName() == null || updateDto.getName().trim().isEmpty()) {
            bindingResult.rejectValue(AuthConstants.ATTR_USER, AuthConstants.ERROR_CODE_NAME, AuthConstants.MSG_NAME_REQUIRED);
        } else if (updateDto.getName().trim().length() < 2 || updateDto.getName().trim().length() > 50) {
            bindingResult.rejectValue(AuthConstants.ATTR_USER, AuthConstants.ERROR_CODE_NAME, AuthConstants.MSG_NAME_LENGTH);
        }
    }

    /**
     * 현재 비밀번호 검증 (보안 확인용)
     */
    public static void validateCurrentPassword(UserUpdateDto updateDto, User user, UserService userService, PasswordEncoder passwordEncoder, BindingResult bindingResult) {
        if (updateDto.getCurrentPassword() == null || updateDto.getCurrentPassword().trim().isEmpty()) {
            bindingResult.rejectValue(AuthConstants.ATTR_USER, AuthConstants.ERROR_CODE_CURRENT_PASSWORD, AuthConstants.MSG_CURRENT_PASSWORD_REQUIRED);
        } else if (!userService.checkPassword(user.getUserId(), updateDto.getCurrentPassword(), passwordEncoder)) {
            bindingResult.rejectValue(AuthConstants.ATTR_USER, AuthConstants.ERROR_CODE_CURRENT_PASSWORD, AuthConstants.MSG_CURRENT_PASSWORD_INVALID);
        }
    }

    /**
     * 새 비밀번호 검증
     */
    public static void validateNewPassword(UserUpdateDto updateDto, BindingResult bindingResult) {
        String newPassword = updateDto.getPassword();
        String currentPassword = updateDto.getCurrentPassword();

        if (newPassword == null || newPassword.trim().isEmpty()) {
            bindingResult.rejectValue(AuthConstants.ATTR_USER, AuthConstants.ERROR_CODE_PASSWORD, AuthConstants.MSG_NEW_PASSWORD_REQUIRED);
            return;
        }

        if (newPassword.trim().length() < 6) {
            bindingResult.rejectValue(AuthConstants.ATTR_USER, AuthConstants.ERROR_CODE_PASSWORD, AuthConstants.MSG_NEW_PASSWORD_LENGTH_MIN);
        }

        if (newPassword.trim().length() > 100) {
            bindingResult.rejectValue(AuthConstants.ATTR_USER, AuthConstants.ERROR_CODE_PASSWORD, AuthConstants.MSG_NEW_PASSWORD_LENGTH_MAX);
        }

        if (currentPassword != null && currentPassword.equals(newPassword)) {
            bindingResult.rejectValue(AuthConstants.ATTR_USER, AuthConstants.ERROR_CODE_PASSWORD, AuthConstants.MSG_NEW_PASSWORD_SAME_AS_CURRENT);
        }
    }
}