package com.chakak.controller;

import com.chakak.common.constants.AuthConstants;
import com.chakak.dto.request.UserRegisterDto;
import com.chakak.exception.DuplicateEmailException;
import com.chakak.exception.DuplicateUserIdException;
import com.chakak.exception.WithdrawnEmailException;
import com.chakak.exception.DuplicateEntryException;
import com.chakak.service.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRegistrationService userRegistrationService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute(AuthConstants.ATTR_REGISTER_DTO, new UserRegisterDto());
        return AuthConstants.VIEW_REGISTER;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute(AuthConstants.ATTR_REGISTER_DTO) UserRegisterDto registerDto,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return AuthConstants.VIEW_REGISTER;
        }

        try {
            userRegistrationService.register(registerDto);
            redirectAttributes.addFlashAttribute(AuthConstants.ATTR_MESSAGE, AuthConstants.MSG_REGISTRATION_SUCCESS);
            return AuthConstants.REDIRECT_LOGIN;

        } catch (DuplicateUserIdException e) {
            log.warn("Registration failed: {}", e.getMessage());
            bindingResult.rejectValue("userId", "duplicate", e.getMessage());
            return AuthConstants.VIEW_REGISTER;
        } catch (DuplicateEmailException e) {
            log.warn("Registration failed: {}", e.getMessage());
            bindingResult.rejectValue("email", "duplicate", e.getMessage());
            return AuthConstants.VIEW_REGISTER;
        } catch (WithdrawnEmailException e) {
            log.warn("Registration failed: {}", e.getMessage());
            bindingResult.rejectValue("email", "duplicate", e.getMessage());
            return AuthConstants.VIEW_REGISTER;
        } catch (DuplicateEntryException e) {
            log.warn("Registration failed: {}", e.getMessage());
            bindingResult.reject("register.fail", e.getMessage());
            return AuthConstants.VIEW_REGISTER;
        } catch (RuntimeException e) {
            log.error("Registration error", e);
            bindingResult.reject("register.fail", "회원가입 중 오류가 발생했습니다.");
            return AuthConstants.VIEW_REGISTER;
        }
    }

    @PostMapping("/check-userid")
    @ResponseBody
    public boolean checkUserId(@RequestParam String userId) {
        return userRegistrationService.isUserIdAvailable(userId);
    }

    @PostMapping("/check-email")
    @ResponseBody
    public boolean checkEmail(@RequestParam String email) {
        return userRegistrationService.isEmailAvailable(email);
    }
}
