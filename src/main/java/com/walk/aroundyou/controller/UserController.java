package com.walk.aroundyou.controller;

import java.util.Objects;
//import java.util.HashMap;
//import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.dto.UpdateMypageDTO;
import com.walk.aroundyou.dto.UpdateUserpageDTO;
import com.walk.aroundyou.dto.UserPasswordChangeDTO;
import com.walk.aroundyou.dto.UserPasswordSendDTO;
import com.walk.aroundyou.dto.UserRequest;
import com.walk.aroundyou.service.MailService;
import com.walk.aroundyou.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

	///////////////// 로그인 메인 페이지(처음 시작 할 때의 화면)
	@GetMapping("/login")
	public String login() {
		return "login";
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

		// 중복 체크에 대한 조건문 -> ajax에서 요청 처리 결과
		if (user) {
			return ResponseEntity.ok().body("다른 아이디를 입력하세요");
		} else {
			return ResponseEntity.ok().body("사용 가능한 아이디입니다.");
		}
	}
	

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

	

	///////////////////////////// 비밀번호 찾기 - 이메일로 받기
	@GetMapping("/login/pwdlookup")
	public String showPwdLookupForm() {
		log.info("들어왔다");

		// 아이디 조회 폼을 보여주는 뷰 이름
		return "pwdlookupform";
	}

	// 뷰에서 userEmail을 파라미터로 받아 이메일 유무를 확인하는 서비스 호출
	@PostMapping("/login/pwdlookup/check")
	@ResponseBody
	public boolean checkEmail(@RequestParam(name = "userEmail") String userEmail) {
		log.info("checkEmail 진입");
		// 아이디 조회 폼을 보여주는 뷰 이름
		return userService.checkEmail(userEmail);
	}

	// 임시 비밀번호 생성하고 메일을 생성 & 전송하는 컨트롤러
	// 이메일로 임시 비밀번호 보내기
	@PostMapping("/login/pwdlookup/send")
	public String sendEmail(@RequestParam("userEmail") String userEmail) {
		log.info("sendEmail진입");
		log.info("이메일 : " + userEmail);

		// 임시 비밀번호 생성
		String tmpPwd = userService.getTmpPwd();

		// 임시 비밀번호 저장
		userService.updatePwd(tmpPwd, userEmail);

		// 메일 생성 & 전송
		MailService mailService = new MailService();
		UserPasswordSendDTO mail = mailService.createMail(tmpPwd, userEmail);
		mailService.sendMail(mail);

		log.info("임시 비밀번호 전송 완료");

		return "/login";
	}

	
	
	//////////////////// 마이페이지
	// Principal은 Spring Security에서 인증된 사용자 정보를 제공하는 객체
	// 보통 사용자의 아이디(username)이나 식별자(identifier)와 같은 정보를 포함
	// Principal 객체는 현재 로그인한 사용자에 대한 정보를 얻기 위해 컨트롤러 메서드에서 매개변수로 사용 가능
	// 로그인 된 상태에 탈퇴를 하는 거니까 인증된 사용자의 정보를 받아오는 principal 사용
	// 처음 불러오는 정보
	// @PreAuthorize("isAuthenticated()")
	@GetMapping("/main/mypage")
	// Authentication은 이 사용자의 인증 상태와 함께 사용자 정보를 포함하는 래퍼 객체
	public String showMypage(
			Model model, 
			Authentication authentication, 
			@AuthenticationPrincipal User user) {

		// 헤더에 정보 추가하기 위한 코드
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
		}

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
	@PreAuthorize("isAuthenticated()") // 로그인한 사용자에게만 메서드가 호출된다
	@PostMapping("/main/mypage")
	public String processMypage(String userId, UpdateMypageDTO dto, Model model) {

		model.addAttribute("user", dto);
		Member updatedMember = userService.updateMypage(userId, dto);

		// log.info("마이페이지 업데이트 성공: userId={}, userNickname={}, userImg={},
		// userDescription={}", userId.toString(),
		// userNickname.toString(), userImg.toString(), userDescription.toString());

		if (updatedMember != null) {
			return "redirect:/main/mypage"; // 정보가 업데이트되면 마이페이지로 리다이렉트
		} else {
			model.addAttribute("showAlert", false);

			// ${showAlert}가 있는 폼으로 이동
			return "mypage"; // 오류 처리
		}
	}

	
	

	//////////////////// 유저페이지
	// import org.springframework.security.core.userdetails.User;
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/main/mypage/userpage")
	public String showUserpage(Model model, Authentication authentication, @AuthenticationPrincipal User user) {

		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
		}

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

	@PreAuthorize("isAuthenticated()") // 로그인한 사용자에게만 메서드가 호출된다
	@PostMapping("/main/mypage/userpage")
	public String processuserpage(UpdateUserpageDTO dto, Model model) {

		model.addAttribute("user", dto);
		Member updatedMember = userService.updateUserInfo(dto);

		if (updatedMember != null) {
			return "redirect:/main/mypage/userpage"; // 정보가 업데이트되면 마이페이지로 리다이렉트
		} else {
			model.addAttribute("showAlert", false);

			// ${showAlert}가 있는 폼으로 이동
			return "userpage"; // 오류 처리
		}
	}

	
	
	
	
	//////////////////// 비밀번호 변경
	@GetMapping("/main/mypage/userpage/changepwd")
	public String showChangePwd() {
		return "userpage";
	}

	@PostMapping("/main/mypage/userpage/changepwd")
	public String processChangePwd(@Valid UserPasswordChangeDTO dto, Model model, Authentication authentication,
			@AuthenticationPrincipal User user) {

		// new password 끼리 비교
		// Objects.equals(a, b) : a와 b가 같으면 true 반환, 다르면 false 반환
		if (!Objects.equals(dto.getNewPwd(), dto.getComfirmPwd())) {

			model.addAttribute("dto", dto);
			model.addAttribute("differentPassword", "비밀번호가 같지 않습니다.");
			return "/main/mypage/userpage";
		}

		String result = userService.updateMemberPassword(dto, user.getUsername());

		// 현재 비밀번호가 틀렸을 경우(디비와)
		if (result == null) {
			model.addAttribute("dto", dto);
			model.addAttribute("wrongPassword", "비밀번호가 맞지 않습니다.");
			return "/main/mypage/userpage";
		}

		// 비밀번호 변경 시 자동 로그아웃으로 재 로그인 알림! (페이지는 로그인 페이지로)
		return "redirect:/login";
	}

	
	
	
	
	//////////////////// 탈퇴
	// 탈퇴 버튼 누르면 나오는 화면
	@GetMapping("/main/mypage/userpage/withdraw")
	public String showWithdrawForm() {
		// 탈퇴 폼 템플릿을 보여줌
		return "userpage";
	}

	// 탈퇴 처리하는 곳
	@PostMapping("/main/mypage/userpage/withdraw")
	public String processWithdrawForm(@RequestParam String currentPwd, @AuthenticationPrincipal User user,
			Model model) {

		boolean result = userService.deleteByUserId(user.getUsername(), currentPwd);

		if (result) {
			// 탈퇴 후 로그아웃하도록 리다이렉트
			return "redirect:/logout";
		} else {
			model.addAttribute("wrongPassword", "비밀번호가 맞지 않습니다");
		}
		return "redirect:/userpage";
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