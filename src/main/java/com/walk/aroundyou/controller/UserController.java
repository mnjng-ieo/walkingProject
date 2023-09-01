package com.walk.aroundyou.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.domain.role.UserRole;
import com.walk.aroundyou.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	//////////////////// 회원가입
	// 처음 회원가입 페이지를 보여줌
	@GetMapping("/signup")
    public String showSignupForm(Model model) {
		// 뷰 템플릿에서 폼을 표시하기 위한 초기값을 설정
		// "user"라는 이름으로 뷰 모델에 새로운 User 객체를 추가하는 것
		// 추가된 User 객체의 필드는 뷰 템플릿에서 해당 필드의 값을 사용하여 폼 필드에 초기값을 설정
        model.addAttribute("user", new User());
        return "signup";
    }
	// 회원가입 폼을 작성하고 제출했을 때 회원가입 정보를 처리하는 역할
    @PostMapping("/signup")
    // @ModelAttribute : 클라이언트가 전송한 데이터를 자동으로 User 객체에 매핑
    public String processSignupForm(@ModelAttribute User user) {
        // 사용자 정보 등록
        userService.registerUser(user);
        return "redirect:/login"; // 회원가입 후 로그인 페이지로 리다이렉트
    }
	
	
	//////////////////// 로그인
	// 로그인 메인 페이지(처음 시작 할 때의 화면)
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	// 로그인 입력 시 페이지
	@PostMapping("/login")
	public String processLoginForm(@RequestParam String userId, @RequestParam String userPwd, Model model) {

		// loginResult 값이 true = 로그인 성공, false = 로그인 실패
		boolean loginResult = userService.login(userId, userPwd);
		
		// loginResult == true
		if(loginResult){
			// 로그인 성공 시 메인 페이지로 redirect
			return "redirect:/main";
		} else {	
			// loginResult == false
			// model객체에 true 값으로 '실패 상태' 표시
			model.addAttribute("resultFail", true);
			// 로그인페이지로 가서 실패메시지 출력
			return "/login";
		}
	}
	
	//////////////////// 로그아웃
	// 스프링 시큐리티를 사용해서 로그아웃 처리
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		
	    /// Spring Security의 로그아웃 처리 과정 :
		///  SecurityContextHolder를 사용하여 현재 사용자의 인증 정보를 가져와 SecurityContextLogoutHandler를 통해 로그아웃 작업이 수행
		// Authentication : 인증 정보를 나타내는데 사용
		// SecurityContextHolder는 Spring Security의 SecurityContext를 관리하는 클래스
		// getAuthentication() 메서드로 현재 사용자의 인증 정보를 가져옴
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    
	    // 인증 정보가 null이 아닌 경우에만 로그아웃 처리를 수행
	    if (authentication != null) {
	    	// SecurityContextLogoutHandler 클래스를 사용하여 로그아웃 처리를 수행
	    	// 이 클래스는 Spring Security의 로그아웃 기능을 제공하며, 사용자의 세션을 무효화하고 인증 정보를 제거하는 역할
	        new SecurityContextLogoutHandler().logout(request, response, authentication);
	    }
	    
	    // 로그아웃이 완료되면 로그인 페이지로 리다이렉트하며, URL에 "?logout" 파라미터를 추가하여 로그아웃 성공 여부를 표시
	    return "redirect:/login?logout"; 
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
	@GetMapping("/mypage/{userId}")
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
	@PostMapping("/mypage/{userId}")
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
	public String processChangePwd(@RequestParam String currentPwd, @RequestParam String newPwd, String newPwdConfirm, Principal principal, Model model) {
		// 인증된 id 얻어오기
		String userId = principal.getName();
		// id 통해 사용자 정보 조회
		Optional<User> userOptional = userService.findByUserId(userId);
		// id가 존재한다면
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 현재 비밀번호와 맞다면 DB에 저장된 비밀 번호가 일치한다면
            if (passwordEncoder.matches(currentPwd, user.getUserPwd())) {
            	
            	// 새로 입력한 비번을 두번 입력해서 그 값이 일치하면
                if (newPwd.equals(newPwdConfirm)) {
                	// 비번 암호화해라
                    String encryptedPassword = passwordEncoder.encode(newPwd);
                    // 비번 값 변경
                    user.setUserPwd(encryptedPassword);
                    // 저장
                    userService.saveUser(user);
                    // 비밀번호 변경 성공 시 로그인 페이지로 리다이렉트
                    return "redirect:/login?passwordChanged";
                    
                } else {
                	// 일치하지 않을 경우 
                	// 모델 객체에 데이터 담아 뷰에 보냄
                    model.addAttribute("error", "새로운 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
                }
            } else {
            	// 현재 입력한 비밀 번호와 디비에 저장된 현재 비번이 일치하지 않을 경우
                model.addAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
            }
        } else {
        	// id가 존재하지 않을 경우
            model.addAttribute("error", "사용자를 찾을 수 없습니다.");
        }
        
        // 변경 실패 시 다시 폼 템플릿을 보여줌
        return "changepwdform"; 
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
