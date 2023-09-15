package com.walk.aroundyou.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.walk.aroundyou.security.AdminAuthorize;
import com.walk.aroundyou.security.UserAuthorize;

// dashboard : 유저, 관리자 모아 보기
// 백엔드 유저 정보 알아내기(웹페이지에서 유저 정보 표시하기)
@Controller
public class ViewController {

	@GetMapping("/dashboard")
	public String dashboardPage(@AuthenticationPrincipal User user, Model model) {

		model.addAttribute("loginId", user.getUsername());
		model.addAttribute("loginRoles", user.getAuthorities());
		return "dashboard";
	}
	
	@GetMapping("/admin")
    @AdminAuthorize
    public String adminSettingPage() {
        return "admin_setting";
    }

	@GetMapping("/user")
	@UserAuthorize
	public String userSettingPage() {
		return "user_setting";
	}
	
	/////// 메인페이지 - 로그인, 회원가입 버튼 | 로그인 확인, 로그아웃 버튼			
    @GetMapping("/main")
public String processmain(@AuthenticationPrincipal User user, Model model) {
		
		// 로그인 된 상태일 경우로 조건을 줘야지 /main페이지를 public으로 불러올 수 있음
		if(user != null) {
		model.addAttribute("loginId", user.getUsername());
		}
		
		return "main";
	}
    
}
