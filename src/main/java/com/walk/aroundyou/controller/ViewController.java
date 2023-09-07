package com.walk.aroundyou.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.walk.aroundyou.security.AdminAuthorize;
import com.walk.aroundyou.security.UserAuthorize;

// dashboard : 유저, 관리자 모아 보기
// 백엔드 유저 정보 알아내기(웹페이지에서 유저 정보 표시하기)
@Controller
@RequestMapping("/view")
public class ViewController {

	@GetMapping("/dashboard")
	public String dashboardPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("loginId", user.getUsername());
		model.addAttribute("loginRoles", user.getAuthorities());
		return "dashboard";
	}
	
	@GetMapping("/setting/admin")
    @AdminAuthorize
    public String adminSettingPage() {
        return "admin_setting";
    }

    @GetMapping("/setting/user")
    @UserAuthorize
    public String userSettingPage() {
        return "user_setting";
    }
}
