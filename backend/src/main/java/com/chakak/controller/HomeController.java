package com.chakak.controller;

import com.chakak.common.constants.AuthConstants;
import com.chakak.domain.User;
import com.chakak.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isLoggedIn = authentication != null && authentication.isAuthenticated()
                && !AuthConstants.ANONYMOUS_USER.equals(authentication.getPrincipal());

        model.addAttribute(AuthConstants.ATTR_IS_LOGGED_IN, isLoggedIn);

        if (isLoggedIn) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = customUserDetails.getUser();
            model.addAttribute(AuthConstants.ATTR_USER, user);
        }

        return AuthConstants.VIEW_INDEX;
    }
}
