package com.TIL8.ChaKak.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ResponseBody; // 테스트용 `@ResponseBody`는 제거

@Controller
public class ViewController {

	@GetMapping("/")
	public String index() {
		return "index"; // index.html 템플릿 반환
	}

	@GetMapping("/signup")
	public String signUpPage() {
		return "signup"; // signup.html 템플릿 반환
	}

	@GetMapping("/login")
	public String loginPage() {
		return "login"; // login.html 템플릿 반환
	}

	@GetMapping("/home")
	public String homePage() {
		return "home"; // home.html 템플릿 반환 (로그인 성공 후 이동 페이지)
	}

	// 마이페이지 뷰
	@GetMapping("/mypage")
	public String myPage() {
		// System.out.println(">>> /mypage 컨트롤러 진입! (템플릿 렌더링 시도)"); // 진단용 로그는 제거
		return "mypage"; // mypage.html 템플릿 반환
	}

	// 회원 정보 수정 뷰 추가
	@GetMapping("/edit-profile")
	public String editProfilePage() {
		// System.out.println(">>> /edit-profile 컨트롤러 진입!"); // 진단용 로그는 제거
		return "edit_profile"; // edit_profile.html 템플릿 반환
	}
}
