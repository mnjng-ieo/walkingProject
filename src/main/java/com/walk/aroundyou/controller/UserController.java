package com.walk.aroundyou.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.domain.role.UserRole;
import com.walk.aroundyou.dto.UserRequest;
import com.walk.aroundyou.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {
	
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	//////////////////// 회원가입
	// 회원가입 폼을 보여주는 페이지
	@GetMapping("/signup")
	public String showSignup() {
		return "signup";
	}
	// 회원가입 처리하는 페이지
	// 회원가입 후 작성한 데이터를 뷰에 보여주지 않으므로 Model객체 필요없음
	@PostMapping("/signup")
	public String processSignup(UserRequest request) {
		
		// 회원가입 메서드 호출
		userService.registerUser(request); 
		
		//회원가입이 완료된 이후에 로그인 페이지로 이동
		return "redirect:/login";
	}
	
	
	//////////////////// 로그인
	@GetMapping("/login")
	public String login() {
		
		return "login";
	}
	
	//////////////////// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		// SecurityContextLogoutHandler() : 로그아웃 수행
		// SecurityContextHolder.getContext().getAuthentication() : 현재 인증된 사용자의 인증 객체 -> SecurityContextHolder에서 해당 사용자의 인증 정보를 지우는 데 사용
		new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
		return "redirect:/login";
		
	}
	
	
	//////////////////// 아이디 찾기
	@GetMapping("/login/idlookup")
	public String showIdLookupForm() {
		
		// 아이디 조회 폼을 보여주는 뷰 이름
		return "idlookupform"; 
	}
	@PostMapping("/login/idlookup")
	public String processIdLookupForm(@RequestParam String userName, @RequestParam String userEmail, Model model) {
		
		// id 기능 구현해 놓은 서비스 가져옴
		String idlookup = userService.searchByUserId(userName, userEmail);
		
		// 조회 결과를 모델에 데이터 추가하여 뷰에서 표시
		// ${idlookup}와 같이 Thymeleaf 템플릿 문법 사용
		model.addAttribute("idlookup", idlookup);	
		
		// id 조회 결과를 보여주는 폼으로 이동
		return "idlookupform";
	}
	
	
	//////////////////// 비밀번호 찾기
	@GetMapping("/login/pwdlookup")
	public String showPwdLookupForm() {
		
		// 아이디 조회 폼을 보여주는 뷰 이름
		return "pwdlookupform"; 
	}
	
	@PostMapping("/login/pwdlookup")
	public String showPwdLookupForm(@RequestParam String userId, Model model) {
		
		String pwdlookup = userService.searchByUserPwd(userId);
		model.addAttribute("pwdlookup", pwdlookup);
		
		return "pwdlookupform";
	}
	
	
	//////////////////// 마이페이지
	// Principal은 Spring Security에서 인증된 사용자 정보를 제공하는 객체
    // 보통 사용자의 아이디(username)이나 식별자(identifier)와 같은 정보를 포함
    // Principal 객체는 현재 로그인한 사용자에 대한 정보를 얻기 위해 컨트롤러 메서드에서 매개변수로 사용 가능
    // 로그인 된 상태에 탈퇴를 하는 거니까 인증된 사용자의 정보를 받아오는 principal 사용
	// 처음 불러오는 정보
	@GetMapping("/mypage")
	public String showMypage(Model model, @AuthenticationPrincipal Principal principal) {
		
		// 현재 로그인한 사용자 아이디
		String userId = principal.getName();
		// 위에 가져온 아이디를 기준으로 사용자 정보 불러옴
		Optional<User> user = userService.findByUserId(userId);
		
		if(user != null) {
			// model에 user객체 추가해서 뷰로 넘겨줌
			model.addAttribute("user", user);
			return "mypage";
		}else {
			return "error";
		}
	}
	
	// 변경 후 블러오는 정보
	@PreAuthorize("isAuthenticated()") // 로그인한 사용자에게만 허용
	@PostMapping("/mypage")
	public String processMypage(@PathVariable String userId, @RequestParam String userNickname, @RequestParam String userImg, @RequestParam String uesrDescription, Principal principal) {
		
		// 로그인 된 아이디를 가져옴
		String userId1 = principal.getName();
		
		User updateUser = userService.updateMypage(userId, userNickname, userImg, uesrDescription);
		
		if(updateUser != null) {
			// 정보 업데이트를 성공한 경우
			return "redirect:mypage";
		}else {
			return "error";
		}
	}
	
	
	//////////////////// 유저페이지
	@GetMapping("/userpage/{userId}")
	public String showUserpage(Model model, Principal principal) {

		// 현재 로그인한 사용자 아이디
		String userId = principal.getName();
		// 위에 가져온 아이디를 기준으로 사용자 정보 불러옴
		Optional<User> user = userService.findByUserId(userId);
				
		if (user != null) {
			// model에 user객체 추가해서 뷰로 넘겨줌
			model.addAttribute("user", user);
			return "userpage";
		} else {
			return "error";
		}
	}
	
	@PreAuthorize("isAuthenticated()") // 로그인한 사용자에게만 허용
	@PostMapping("/userpage/{userId}")
	public String processUserpage(@PathVariable String userId, @RequestParam String userName, @RequestParam String userNickname, @RequestParam String userTelnumber, @RequestParam String userEmail, Principal principal) {
		
		String userId1 = principal.getName();
		
		User updateUser = userService.updateUserInfo(userId, userName, userNickname, userTelnumber, userEmail);
		
		if (updateUser != null) {
			return "redirect:userpage";
		}else {
			return "error";
		}
	}
	
	
	
	//////////////////// 비밀번호 변경
	@GetMapping("/changePwd")
	public String showChangePwd() {
		return "changepwdform";
	}
	@PostMapping("/changePwd")
	public String processChangePwd(@RequestParam String userId, @RequestParam String currentPwd,
			@RequestParam String newPwd, String newPwdConfirm, Model model) {

		boolean isPasswordChanged = userService.changePwd(userId, currentPwd, newPwd, newPwdConfirm);

		if (isPasswordChanged) {
			// 비밀번호 변경 성공 시 메인 페이지 또는 다른 페이지로 리디렉션
			return "redirect:/userpage";
		} else {
			// 비밀번호 변경 실패 시 에러 메시지를 모델에 추가하여 폼 다시 표시
			model.addAttribute("error", "비밀번호 변경에 실패했습니다. 입력 정보를 확인해 주세요.");
			// 변경 실패 시 다시 폼 템플릿을 보여줌
			return "changepwdform";
		}
	}

	 
	
	
	//////////////////// 탈퇴와 강퇴
	// 탈퇴 버튼 누르면 나오는 화면
	@GetMapping("/withdraw")
    public String showWithdrawForm() {
		// 탈퇴 폼 템플릿을 보여줌
        return "withdraw"; 
    }

	// 탈퇴 처리하는 곳
    @PostMapping("/withdraw")
    public String processWithdrawForm(@RequestParam String currentPwd, Principal principal) {
       
    	// .getName() -> id불러옴
    	String userId = principal.getName();
        
        // 사용자 탈퇴 처리
        userService.deleteUserById(userId, currentPwd);

        // 탈퇴 후 로그아웃하도록 리다이렉트
        return "redirect:/logout"; 
    }

    // 관리자가 강퇴하는 곳 -> 관리자사이트 매핑?
    @PostMapping("/ban/{userId}")
    public String banUser(@PathVariable String userId, Principal principal) {
    	
        // 현재 로그인한 사용자 정보를 확인하여 관리자인지 확인
        Optional<User> currentUser = userService.findByUserId(principal.getName());
        User user = currentUser.get();

        // 관리자 권한 가지고 있는지 확인
        if (currentUser != null && user.getRole() == UserRole.ADMIN) {
  
            // 관리자가 강퇴 처리
            userService.deleteByAdmin(userId, UserRole.ADMIN);
        }
        
        // 강퇴 후 관리자 대시보드로 리다이렉트
        return "redirect:/admin/dashboard"; 
    }

}
