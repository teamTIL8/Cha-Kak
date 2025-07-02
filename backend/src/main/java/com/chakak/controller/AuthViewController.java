package com.chakak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthViewController {

	@GetMapping("/")
	public String index() {
		return "Auth/index";
	}

	@GetMapping("/signup")
	public String signUpPage() {
		return "Auth/signup";
	}

	@GetMapping("/login")
	public String loginPage() {
		return "Auth/login";
	}

	@GetMapping("/home")
	public String homePage() {
		return "Auth/home";
	}

	@GetMapping("/mypage")
	public String myPage() {
		return "Auth/mypage";
	}

	@GetMapping("/edit-profile")
	public String editProfilePage() {
		return "Auth/edit_profile";
	}
}
