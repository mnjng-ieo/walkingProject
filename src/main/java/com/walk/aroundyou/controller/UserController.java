package com.walk.aroundyou.controller;

import java.security.Principal;
//import java.util.HashMap;
//import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.dto.UserRequest;
import com.walk.aroundyou.service.UserService;

import lombok.extern.slf4j.Slf4j;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;

@Slf4j
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

		log.info(request.toString());
		// 회원가입 메서드 호출
		userService.registerUser(request);

		// 회원가입이 완료된 이후에 로그인 페이지로 이동
		return "redirect:/login";
	}

	//////////////////// 아이디 중복 체크
	@GetMapping("/signup/checkId")
	@ResponseBody
	// ResponseEntity를 사용해 상태 코드를 설정
	// 제네릭을 이용해서 상태에 따른 결과 확인
	public ResponseEntity<?> checkUserId(@RequestParam String userId) {

		boolean user = userService.isUserIdDuplicate(userId);

		// 중복 체크에 대한 조건문 -> ajax에서 요청 처리 결과 success와 error랑 다른 부분
		if (user) {
			return ResponseEntity.ok().body("다른 아이디를 입력하세요");
		} else {
			return ResponseEntity.ok().body("사용 가능한 아이디입니다.");
		}
	}

	// 로그인 메인 페이지(처음 시작 할 때의 화면)
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	// 로그인 결과에 맞춰서 네비의 로그인, 회원가입, 로그아웃 버튼 숨기기
//	@ResponseBody
//	@GetMapping("/checkLoginStatus")
//	// Authentication 객체를 매개변수로 받아서 로그인 상태를 확인하고 JSON 응답을 생성
//    public Map<String, Boolean> checkLoginStatus(Authentication authentication) {
//		
//        // Spring Security에서 제공하는 Authentication 객체를 통해 로그인 상태 확인
//		// authentication != null && authentication.isAuthenticated() : 로그인 상태
//		// loggedIn : true
//        boolean loggedIn = authentication != null && authentication.isAuthenticated();
//
//        // JSON 응답 생성
//        Map<String, Boolean> response = new HashMap<>();
//        response.put("loggedIn", loggedIn);
//
//        return response;
//    }

	//////////////////// 아이디 찾기
	@GetMapping("/login/idlookup")
	public String showIdLookupForm() {

		// 아이디 조회 폼을 보여주는 뷰 이름
		return "idlookupform";
	}

	@PostMapping("/login/idlookup")
	@ResponseBody
	public String processIdLookupForm(@RequestParam String userName, @RequestParam String userEmail, Model model) {

		// id 기능 구현해 놓은 서비스 가져옴
		String idlookup = userService.searchByUserId(userName, userEmail);

		// 조회 결과를 모델에 데이터 추가하여 뷰에서 표시
		// ${idlookup}와 같이 Thymeleaf 템플릿 문법 사용
		model.addAttribute("idlookup", idlookup);

		return "" + idlookup;
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

		return "" + pwdlookup;
	}

	//////////////////// 마이페이지
	// Principal은 Spring Security에서 인증된 사용자 정보를 제공하는 객체
	// 보통 사용자의 아이디(username)이나 식별자(identifier)와 같은 정보를 포함
	// Principal 객체는 현재 로그인한 사용자에 대한 정보를 얻기 위해 컨트롤러 메서드에서 매개변수로 사용 가능
	// 로그인 된 상태에 탈퇴를 하는 거니까 인증된 사용자의 정보를 받아오는 principal 사용
	// 처음 불러오는 정보
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/main/mypage")
	// Authentication은 이 사용자의 인증 상태와 함께 사용자 정보를 포함하는 래퍼 객체
	public String showMypage(Model model, Authentication authentication) {

		if (authentication != null && authentication.isAuthenticated()) {
			// 현재 로그인한 사용자 아이디
			String userId = authentication.getName();
			// 위에 가져온 아이디를 기준으로 사용자 정보 불러옴
			Optional<Member> member = userService.findByUserId(userId);

			if (member.isPresent()) {
				// model에 user 객체 추가해서 뷰로 넘겨줌
				model.addAttribute("user", member.get());
				return "mypage";
			} else {
				return "error";
			}
		} else {
			// 로그인되지 않은 경우 처리
			return "login"; // 로그인 페이지로 리다이렉트 또는 다른 처리를 수행
		}
	}

	// 변경 후 블러오는 정보
	// @PreAuthorize("isAuthenticated()") // 로그인한 사용자에게만 메서드가 호출된다
	// Authentication authentication : 매개변수를 통해 사용자 정보를 확인
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/main/mypage")
	public String processMypage(@RequestParam String userId, @RequestParam String userNickname,
			@RequestParam String userImg, @RequestParam String userDescription,
			@AuthenticationPrincipal Principal principal) {

		log.info("processMypage Controller 진입");

		// 로그인 된 아이디를 가져옴
		String loginid = principal.getName();

		Optional<Member> member = userService.findByUserId(loginid);

		if (member.isPresent()) {

			Member updateUser = userService.updateMypage(userId, userNickname, userImg, userDescription);

			if (updateUser != null) {
				// 정보 업데이트를 성공한 경우
				return "redirect:mypage";
			} else {
				return "error";
			}
		} else {
			return "error";
		}
	}

	//////////////////// 유저페이지
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/main/mypage/userpage")
	public String showUserpage(Model model, Authentication authentication) {

		if (authentication != null && authentication.isAuthenticated()) {
			// 현재 로그인한 사용자 아이디
			String loginId = authentication.getName();
			// 위에 가져온 아이디를 기준으로 사용자 정보 불러옴
			Optional<Member> member = userService.findByUserId(loginId);

			if (member.isPresent()) {
				// model에 user 객체 추가해서 뷰로 넘겨줌
				model.addAttribute("user", member.get());
				return "userpage";
			} else {
				return "error";
			}
		} else {
			// 로그인되지 않은 경우 처리
			return "login"; // 로그인 페이지로 리다이렉트 또는 다른 처리를 수행
		}
	}

//	@PreAuthorize("isAuthenticated()") // 로그인한 사용자에게만 허용
//	@PostMapping("/main/mypage/userpage/{userId}")
//	public String processUserpage(@PathVariable String userId, @RequestParam String userName, @RequestParam String userNickname, @RequestParam String userTelnumber, @RequestParam String userEmail, @AuthenticationPrincipal Principal principal) {
//		
//		String userId1 = principal.getName();
//		
//		Member updateUser = userService.updateUserInfo(userId, userName, userNickname, userTelnumber, userEmail);
//		
//		if (updateUser != null) {
//			return "redirect:userpage";
//		}else {
//			return "error";
//		}
//	}
	@PostMapping("/main/mypage/userpage/{userId}")
	@ResponseBody
	public ResponseEntity<Member> processUserpage(@PathVariable String userId, @RequestParam String userName,
			@RequestParam String userNickname, @RequestParam String userTelnumber, @RequestParam String userEmail,
			Authentication authentication) {

		if (authentication != null && authentication.isAuthenticated()) {
			
			// 현재 로그인한 사용자 아이디
			String loginId = authentication.getName();
			
			Member updateUser = userService.updateUserInfo(loginId, userName, userNickname, userTelnumber, userEmail);

			if (updateUser != null) {
				return ResponseEntity.ok(updateUser);
			} else {
				// 사용자가 존재하지 않을 경우 404 응답 반환
				return ResponseEntity.notFound().build();
			}
		} return ResponseEntity.notFound().build();
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
	public String processWithdrawForm(@RequestParam String currentPwd, @AuthenticationPrincipal Principal principal) {

		// .getName() -> id불러옴
		String userId = principal.getName();

		// 사용자 탈퇴 처리
		userService.deleteByUserId(userId, currentPwd);

		// 탈퇴 후 로그아웃하도록 리다이렉트
		return "redirect:/logout";
	}

	// 관리자가 강퇴하는 곳 -> 관리자사이트 매핑?
//    @PostMapping("/ban/{userId}")
//    public String banUser(@PathVariable String userId, Principal principal) {
//    	
//        // 현재 로그인한 사용자 정보를 확인하여 관리자인지 확인
//        Optional<User> currentUser = userService.findByUserId(principal.getName());
//        User user = currentUser.get();
//
//        // 관리자 권한 가지고 있는지 확인
//        if (currentUser != null && user.getRole() == UserRole.ADMIN) {
//  
//            // 관리자가 강퇴 처리
//        	userService.deleteByAdmin(userId, UserRole.ADMIN);
//        }
//        
//        // 강퇴 후 관리자 대시보드로 리다이렉트
//        return "redirect:/admin/dashboard"; 
//    }

}
