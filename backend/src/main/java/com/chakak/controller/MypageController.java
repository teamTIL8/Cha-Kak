package com.chakak.controller;

import com.chakak.common.constants.AuthConstants;
import com.chakak.domain.User;
import com.chakak.dto.request.UserUpdateDto;
import com.chakak.service.CustomUserDetails;
import com.chakak.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MypageController {

    private final UserService userService;

    @GetMapping("/mypage")
    public String mypage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || AuthConstants.ANONYMOUS_USER.equals(authentication.getPrincipal())) {
            return AuthConstants.REDIRECT_LOGIN;
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();
        model.addAttribute(AuthConstants.ATTR_USER, user);

        return AuthConstants.VIEW_MYPAGE;
    }

    @GetMapping("/edit")
    public String editPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || AuthConstants.ANONYMOUS_USER.equals(authentication.getPrincipal())) {
            return AuthConstants.REDIRECT_LOGIN;
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setEmail(user.getEmail());
        updateDto.setName(user.getName());

        model.addAttribute(AuthConstants.ATTR_UPDATE_DTO, updateDto);
        model.addAttribute(AuthConstants.ATTR_USER, user);
        return AuthConstants.VIEW_EDIT;
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute(AuthConstants.ATTR_UPDATE_DTO) UserUpdateDto updateDto, BindingResult bindingResult,
                                Model model, RedirectAttributes redirectAttributes,
                                @RequestParam(value = "changePassword", defaultValue = "false") boolean changePassword) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        model.addAttribute(AuthConstants.ATTR_USER, user);

        try {
            userService.updateUser(user.getUserId(), updateDto, changePassword);

            String message = buildSuccessMessage(changePassword);
            redirectAttributes.addFlashAttribute(AuthConstants.ATTR_MESSAGE, message);
            return AuthConstants.REDIRECT_MYPAGE;

        } catch (RuntimeException e) {
            log.warn("Profile update failed: {}", e.getMessage());
            bindingResult.reject("update.fail", e.getMessage());
            return AuthConstants.VIEW_EDIT;
        }
    }

    @PostMapping("/withdraw")
    public String withdrawUser(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                AuthConstants.ANONYMOUS_USER.equals(authentication.getPrincipal())) {
            return AuthConstants.REDIRECT_LOGIN;
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        try {
            userService.withdrawUser(user.getUserId());
            log.info("User withdrawal successful: {}", user.getUserId());

            Cookie cookie = new Cookie(AuthConstants.TOKEN_COOKIE_NAME, null);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            SecurityContextHolder.clearContext();

            redirectAttributes.addFlashAttribute(AuthConstants.ATTR_MESSAGE,
                    AuthConstants.MSG_WITHDRAWAL_SUCCESS);
            return AuthConstants.REDIRECT_HOME;

        } catch (RuntimeException e) {
            log.error("User withdrawal error", e);
            redirectAttributes.addFlashAttribute(AuthConstants.ATTR_ERROR,
                    AuthConstants.MSG_WITHDRAWAL_ERROR + e.getMessage());
            return AuthConstants.REDIRECT_MYPAGE;
        }
    }

    private String buildSuccessMessage(boolean changePassword) {
        if (changePassword) {
            return AuthConstants.MSG_UPDATE_SUCCESS_WITH_PASSWORD;
        }
        return AuthConstants.MSG_UPDATE_SUCCESS;
    }
}
