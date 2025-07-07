package com.chakak.controller;

import com.chakak.common.constants.AuthConstants;
import com.chakak.dto.request.UserLoginDto;
import com.chakak.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute(AuthConstants.ATTR_LOGIN_DTO, new UserLoginDto());
        return AuthConstants.VIEW_LOGIN;
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute(AuthConstants.ATTR_LOGIN_DTO) UserLoginDto loginDto, BindingResult bindingResult,
                        HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return AuthConstants.VIEW_LOGIN;
        }

        try {
            String token = authService.login(loginDto);
            log.info("Login successful for user: {}", loginDto.getUserId());

            Cookie cookie = new Cookie(AuthConstants.TOKEN_COOKIE_NAME, token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(AuthConstants.TOKEN_COOKIE_MAX_AGE);
            response.addCookie(cookie);

            return AuthConstants.REDIRECT_MYPAGE; 

        } catch (BadCredentialsException e) {
            log.warn("Login failed for user: {}", loginDto.getUserId());
            bindingResult.reject("login.fail", e.getMessage());
            return AuthConstants.VIEW_LOGIN;
        } catch (RuntimeException e) {
            log.error("Login error", e);
            bindingResult.reject("login.fail", "로그인 중 오류가 발생했습니다.");
            return AuthConstants.VIEW_LOGIN;
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(AuthConstants.TOKEN_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        SecurityContextHolder.clearContext();

        return AuthConstants.REDIRECT_HOME;
    }
}
