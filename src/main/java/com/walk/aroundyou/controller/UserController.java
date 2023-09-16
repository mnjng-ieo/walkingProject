package com.walk.aroundyou.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domain.UploadImage;
import com.walk.aroundyou.domain.role.UserRole;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.dto.ICourseLikeResponseDTO;
import com.walk.aroundyou.dto.ICourseResponseDTO;
import com.walk.aroundyou.dto.IUserResponse;
import com.walk.aroundyou.dto.UpdateMypageDTO;
import com.walk.aroundyou.dto.UpdateUserpageDTO;
import com.walk.aroundyou.dto.UserPasswordChangeDTO;
import com.walk.aroundyou.dto.UserSignupDTO;
import com.walk.aroundyou.security.AdminAuthorize;
import com.walk.aroundyou.service.UploadImageService;
import com.walk.aroundyou.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	private UploadImageService uploadImageService;
	
	// 페이지네이션 사이즈(뷰에 보이는 페이지 수)
	private final static int PAGINATION_SIZE = 5;

	
	
	///////////////// 로그인 메인 페이지(처음 시작 할 때의 화면)
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
            model.addAttribute("errorMessage", "아이디나 비밀번호가 맞지 않습니다!");
        }
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
	public String processSignup(@Valid UserSignupDTO dto, Errors errors, Model model) {

		if (errors.hasErrors()) {
			// 회원가입 실패 시 입력 데이터 값을 유지
			model.addAttribute("signupDto", dto);

			// 유효성 통과 못한 필드와 메시지를 핸들링
			Map<String, String> validatorResult = userService.validateHandling(errors);

			for (String key : validatorResult.keySet()) {
				model.addAttribute(key, validatorResult.get(key));
			}

			// 회원가입 페이지로 다시 리턴
			return "/signup";
		}

		userService.registerUser(dto);

		// 회원가입이 완료된 이후에 로그인 페이지로 이동
		return "redirect:/login?success=true";
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

		return idlookup;
	}

	
	
	
	
	///////////////////////////// 비밀번호 찾기 - 임시 비밀번호 생성
	@GetMapping("/login/pwdlookup")
	public String showPwdLookupForm() {

		// 아이디 조회 폼을 보여주는 뷰 이름
		return "pwdlookupform";
	}

	// 뷰에서 userEmail을 파라미터로 받아 이메일 유무를 확인하는 서비스 호출
	@PostMapping("/login/pwdlookup/check")
	@ResponseBody
	public boolean checkEmail(@RequestParam(name = "" + "userEmail") String userEmail) {
		log.info("checkEmail 진입");
		// 아이디 조회 폼을 보여주는 뷰 이름
		return userService.checkEmail(userEmail);
	}

	// 임시 비밀번호 생성하고 전송하는 컨트롤러
	@PostMapping("/login/pwdlookup/send")
	@ResponseBody
	public String sendEmail(@RequestParam("userEmail") String userEmail) {
		log.info("sendEmail진입");
		log.info("이메일 : " + userEmail);

		// 임시 비밀번호 생성
		String tmpPwd = userService.getTmpPwd();

		// 임시 비밀번호 저장
		userService.updatePwd(tmpPwd, userEmail);
		
		log.info("임시 비밀번호 : " + tmpPwd);
		log.info("임시 비밀번호 전송 완료");

		return "임시로 발급한 비밀번호는 \"" + tmpPwd + "\"입니다." + "\n" + "자동으로 로그아웃되오니 임시비밀번호 복사 후 재 로그인해주세요!";
	}
	
	

	//////////////////// 마이페이지
	// Principal은 Spring Security에서 인증된 사용자 정보를 제공하는 객체
	// 보통 사용자의 아이디(username)이나 식별자(identifier)와 같은 정보를 포함
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/mypage")
	// Authentication은 이 사용자의 인증 상태와 함께 사용자 정보를 포함하는 래퍼 객체
	public String showMypage(
			Model model, Authentication authentication, 
			@AuthenticationPrincipal User user,
			@RequestParam(value = "page", required=false, defaultValue="0") int currentPage) {
		
		// 헤더에 정보 추가하기 위한 코드
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
			Member currentUser = userService.findByUserId(user.getUsername()).get();
			if (currentUser != null) {
				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
				if (currentUserImage != null) {
					String currentUserImagePath = 
							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
					model.addAttribute("currentUserImagePath", currentUserImagePath);
				}
			}
		}

		if (authentication != null && authentication.isAuthenticated()) {
			// 현재 로그인한 사용자 아이디
			String userId = authentication.getName();
			// 위에 가져온 아이디를 기준으로 사용자 정보 불러옴
			Optional<Member> member = userService.findByUserId(userId);

			if (member.isPresent()) {
				// 이미지 경로 넘기기
				UploadImage uploadImage = 
						uploadImageService.findByUser(member.get());
				String imagePath;
				if (uploadImage != null) {
					imagePath = 
							uploadImageService.findUserFullPathById(
									uploadImage.getFileId());
					log.info("imagePath : " + imagePath);
					model.addAttribute("imagePath", imagePath);
				}	
				// 마이페이지에서 내 게시글, 좋아요 확인하기(연서 추가)
				Page<IBoardListResponse> myBoards = 
					userService.findMyBoardAndCnt(userId, currentPage);
				
				// pagination 설정
				int totalPages = myBoards.getTotalPages();
				int pageStart = getPageStart(currentPage, totalPages);
				int pageEnd = 
						(PAGINATION_SIZE < totalPages)? 
								pageStart + PAGINATION_SIZE - 1
								:totalPages;
						if(pageEnd == 0) {
					          pageEnd = 1;
					    }
				// model에 user 객체 추가해서 뷰로 넘겨줌				
				model.addAttribute("user", member.get());
				
				model.addAttribute("lastPage", totalPages);
				model.addAttribute("currentPage", currentPage);
				model.addAttribute("pageStart", pageStart);
				model.addAttribute("pageEnd", pageEnd);
				model.addAttribute("myBoards", myBoards);
				
				return "mypage";
			} else {
				return "error";
			}
		} else {
			// 로그인되지 않은 경우 처리
			return "login"; // 로그인 페이지로 리다이렉트 또는 다른 처리를 수행
		}
	}
	// 마이페이지에서 내 좋아요한 산책로 확인하기(연서 추가)
	@GetMapping("/mypage-course")
	// Authentication은 이 사용자의 인증 상태와 함께 사용자 정보를 포함하는 래퍼 객체
	public String showMyCourse(
			Model model, Authentication authentication, 
			@AuthenticationPrincipal User user,
			@RequestParam(value = "page", required=false, defaultValue="0") int currentPage) {

		// 헤더에 정보 추가하기 위한 코드
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
			Member currentUser = userService.findByUserId(user.getUsername()).get();
			if (currentUser != null) {
				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
				if (currentUserImage != null) {
					String currentUserImagePath = 
							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
					model.addAttribute("currentUserImagePath", currentUserImagePath);
				}
			}
		}

		if (authentication != null && authentication.isAuthenticated()) {
			// 현재 로그인한 사용자 아이디
			String userId = authentication.getName();
			// 위에 가져온 아이디를 기준으로 사용자 정보 불러옴
			Optional<Member> member = userService.findByUserId(userId);

			if (member.isPresent()) {
				// 이미지 경로 넘기기
				UploadImage uploadImage = 
						uploadImageService.findByUser(member.get());
				String imagePath;
				if (uploadImage != null) {
					imagePath = 
							uploadImageService.findUserFullPathById(
									uploadImage.getFileId());
					log.info("imagePath : " + imagePath);
					model.addAttribute("imagePath", imagePath);
				}	
				// 마이페이지에서 내 좋아요 확인하기(연서 추가)
				Page<ICourseLikeResponseDTO> myCourses =
					userService.findMyCourseAndCnt(userId, currentPage);
				
				// pagination 설정
				int totalPages = myCourses.getTotalPages();
				int pageStart = getPageStart(currentPage, totalPages);
				int pageEnd = 
						(PAGINATION_SIZE < totalPages)? 
								pageStart + PAGINATION_SIZE - 1
								:totalPages;
						if(pageEnd == 0) {
					          pageEnd = 1;
					    }
				// model에 user 객체 추가해서 뷰로 넘겨줌				
				model.addAttribute("user", member.get());
				
				model.addAttribute("lastPage", totalPages);
				model.addAttribute("currentPage", currentPage);
				model.addAttribute("pageStart", pageStart);
				model.addAttribute("pageEnd", pageEnd);
				model.addAttribute("myCourses", myCourses);
				
				return "mypageCourse";
			} else {
				return "error";
			}
		} else {
			// 로그인되지 않은 경우 처리
			return "login"; // 로그인 페이지로 리다이렉트 또는 다른 처리를 수행
		}
	}

	// 마이페이지에서 내 산책로 댓글 확인하기(연서 추가)
	@GetMapping("/mypage/course-commenet")
	// Authentication은 이 사용자의 인증 상태와 함께 사용자 정보를 포함하는 래퍼 객체
	public String showMyCourseComment(
			Model model, Authentication authentication, 
			@AuthenticationPrincipal User user,
			@RequestParam(value = "page", required=false, defaultValue="0") int currentPage) {

		// 헤더에 정보 추가하기 위한 코드
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
			Member currentUser = userService.findByUserId(user.getUsername()).get();
			if (currentUser != null) {
				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
				if (currentUserImage != null) {
					String currentUserImagePath = 
							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
					model.addAttribute("currentUserImagePath", currentUserImagePath);
				}
			}
		}

		if (authentication != null && authentication.isAuthenticated()) {
			// 현재 로그인한 사용자 아이디
			String userId = authentication.getName();
			// 위에 가져온 아이디를 기준으로 사용자 정보 불러옴
			Optional<Member> member = userService.findByUserId(userId);

			if (member.isPresent()) {
				// 이미지 경로 넘기기
				UploadImage uploadImage = 
						uploadImageService.findByUser(member.get());
				String imagePath;
				if (uploadImage != null) {
					imagePath = 
							uploadImageService.findUserFullPathById(
									uploadImage.getFileId());
					log.info("imagePath : " + imagePath);
					model.addAttribute("imagePath", imagePath);
				}	

				Page<ICourseResponseDTO> myCourseComments =
					userService.findMyCourseCommentAndCnt(userId, currentPage);
				
				// pagination 설정
				int totalPages = myCourseComments.getTotalPages();
				int pageStart = getPageStart(currentPage, totalPages);
				int pageEnd = 
						(PAGINATION_SIZE < totalPages)? 
								pageStart + PAGINATION_SIZE - 1
								:totalPages;
						if(pageEnd == 0) {
					          pageEnd = 1;
					    }								
								
				// model에 user 객체 추가해서 뷰로 넘겨줌				
				model.addAttribute("user", member.get());
				
				model.addAttribute("lastPage", totalPages);
				model.addAttribute("currentPage", currentPage);
				model.addAttribute("pageStart", pageStart);
				model.addAttribute("pageEnd", pageEnd);
				model.addAttribute("myCourseComments", myCourseComments);
				
				return "mypageCourseComment";
			} else {
				return "error";
			}
		} else {
			// 로그인되지 않은 경우 처리
			return "login"; // 로그인 페이지로 리다이렉트 또는 다른 처리를 수행
		}
	}
	
	// 마이페이지에서 게시물 내 댓글 확인하기(연서 추가)
	@GetMapping("/mypage/board-commenet")
	// Authentication은 이 사용자의 인증 상태와 함께 사용자 정보를 포함하는 래퍼 객체
	public String showMyBoardComment(
			Model model, Authentication authentication, 
			@AuthenticationPrincipal User user,
			@RequestParam(value = "page", required=false, defaultValue="0") int currentPage) {

		// 헤더에 정보 추가하기 위한 코드
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
			Member currentUser = userService.findByUserId(user.getUsername()).get();
			if (currentUser != null) {
				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
				if (currentUserImage != null) {
					String currentUserImagePath = 
							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
					model.addAttribute("currentUserImagePath", currentUserImagePath);
				}
			}
		}

		if (authentication != null && authentication.isAuthenticated()) {
			// 현재 로그인한 사용자 아이디
			String userId = authentication.getName();
			// 위에 가져온 아이디를 기준으로 사용자 정보 불러옴
			Optional<Member> member = userService.findByUserId(userId);

			if (member.isPresent()) {
				// 이미지 경로 넘기기
				UploadImage uploadImage = 
						uploadImageService.findByUser(member.get());
				String imagePath;
				if (uploadImage != null) {
					imagePath = 
							uploadImageService.findUserFullPathById(
									uploadImage.getFileId());
					log.info("imagePath : " + imagePath);
					model.addAttribute("imagePath", imagePath);
				}	

				Page<IBoardListResponse> myBoardComments =
					userService.findMyBoardCommentAndCnt(userId, currentPage);
				
				// pagination 설정
				int totalPages = myBoardComments.getTotalPages();
				int pageStart = getPageStart(currentPage, totalPages);
				int pageEnd = 
						(PAGINATION_SIZE < totalPages)? 
								pageStart + PAGINATION_SIZE - 1
								:totalPages;
						if(pageEnd == 0) {
					          pageEnd = 1;
					    }
				// model에 user 객체 추가해서 뷰로 넘겨줌				
				model.addAttribute("user", member.get());
				
				model.addAttribute("lastPage", totalPages);
				model.addAttribute("currentPage", currentPage);
				model.addAttribute("pageStart", pageStart);
				model.addAttribute("pageEnd", pageEnd);
				model.addAttribute("myBoardComments", myBoardComments);
				
				return "mypageBoardComment";
			} else {
				return "error";
			}
		} else {
			// 로그인되지 않은 경우 처리
			return "login"; // 로그인 페이지로 리다이렉트 또는 다른 처리를 수행
		}
	}
	
	// pagination의 시작 숫자 얻는 메소드(연서 추가)
	private int getPageStart(int currentPage, int totalPages) {
		int result = 1;
		if(totalPages < currentPage + (int)Math.floor(PAGINATION_SIZE/2)) {
			// 시작페이지의 최소값은 1!
			result = Math.max(1, totalPages - PAGINATION_SIZE + 1);
		} else if (currentPage > (int)Math.floor(PAGINATION_SIZE/2)) {
			result = currentPage - (int)Math.floor(PAGINATION_SIZE/2) + 1;
		}
		return result;
	}
	

	// 변경 후 불러오는 정보 -> 마이페이지 닉네임 변경 시 유효성검증 (0916 동희 추가)
	///////// 0913 지수 추가 - 이미지 업로드 처리
	@PreAuthorize("isAuthenticated()") // 로그인한 사용자에게만 메서드가 호출된다
	@PostMapping("/mypage")
	public String processMypage(
			@RequestParam("userId") String userId,
			@ModelAttribute @Valid UpdateMypageDTO dto, Errors errors,
			@RequestParam(value="file", required=false) MultipartFile file,
			@RequestParam(value="ifNewImageExists", defaultValue="0") int ifNewImageExists,
			Model model, @AuthenticationPrincipal User user
			) throws IOException {
		
		if(errors.hasErrors()) {
			
			// 마이페이지 변경 실패 시 입력 데이터 값을 유지
			model.addAttribute("user", dto);
			
			// 유효성 통과 못한 필드와 메시지를 핸들링
			Map<String, String> validatorResult = userService.validateHandling(errors);
			for(String key : validatorResult.keySet()) {
				model.addAttribute(key, validatorResult.get(key));
			}
			return "mypage";
		}

	// 헤더에 정보 추가하기 위한 코드
	if (user != null) {
		model.addAttribute("loginId", user.getUsername());
		Member currentUser = userService.findByUserId(user.getUsername()).get();
		if (currentUser != null) {
			UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
			if (currentUserImage != null) {
				String currentUserImagePath = 
						uploadImageService.findUserFullPathById(currentUserImage.getFileId());
				model.addAttribute("currentUserImagePath", currentUserImagePath);
			}
		}
	}

		model.addAttribute("user", dto);
		
		// 파일 업로드 처리
		// 이미지를 수정할 때는 우선 기존 이미지를 삭제하고 다시 저장하는 순서를 겪는다.
		// 먼저 이미지 파일이 새로 업로드 되었는지 확인
		// ↳ 만약 이미지 파일이 새로 업로드 되지 않았다면 이미지 관련 로직을 건너뛰고 나머지 산책로 데이터만 업데이트
		
		Optional<Member> existedUser = userService.findByUserId(userId);
		UploadImage existedImage = uploadImageService.findByUser(existedUser.get());
		
		// 수정페이지에서 최종 업로드 취소 상태로 수정 요청했을 시
		if(ifNewImageExists == 0) {
			if(existedImage != null) {
				uploadImageService.deleteImage(existedImage);
			}
		}
		
		Member updatedUser = userService.updateMypage(userId, dto);
		if(file != null && !file.isEmpty()) {
			log.info("이미지가 새로 업로드되었어요.");
			if(existedImage != null) {
				log.info("이미지가 수정되었을 경우 삭제부터 합니다."); 
				uploadImageService.deleteImage(existedImage);
			} else {
				log.info("원래 이미지가 없을 경우 삭제 없이 업로드됩니다.");
			}
			// 이미지 업로드 로직
			UploadImage uploadImage =
					uploadImageService.saveUserImage(file, updatedUser);
			log.info("uploadImage의 original 이름 : " + uploadImage.getOriginalFileName());
			
			// 뷰에 이미지 경로 넘기기
			String imagePath;
			imagePath = uploadImageService.findUserFullPathById(
					uploadImage.getFileId());
			log.info("imagePath : " + imagePath);
			model.addAttribute("imagePath", imagePath);
			
		} else {
			log.info("이미지의 수정이 없는 경우입니다.");
			if(existedImage != null) {
        		log.info("기존 이미지를 유지합니다.");
        	}
		}

		if (updatedUser != null) {
			return "redirect:/mypage"; // 정보가 업데이트되면 마이페이지로 리다이렉트
		} else {
			model.addAttribute("showAlert", false);

			// ${showAlert}가 있는 폼으로 이동
			return "mypage"; // 오류 처리
		}
	}
	
	
	
	
	//////////////////관리자 페이지(연서 작성)
	@GetMapping("/admin/users")
	@AdminAuthorize
	public String showUsers(
	Model model,
	@RequestParam(value = "page", required=false, defaultValue="0") int currentPage) {
	
	Page<IUserResponse> users = userService.findAllUsers(currentPage);
	
	// pagination 설정
	int totalPages = users.getTotalPages();
	int pageStart = getPageStart(currentPage, totalPages);
	int pageEnd = 
		(PAGINATION_SIZE < totalPages)? 
				pageStart + PAGINATION_SIZE - 1
				:totalPages;
		if(pageEnd == 0) {
	       pageEnd = 1;
	 }
	
	model.addAttribute("lastPage", totalPages);
	model.addAttribute("currentPage", currentPage);
	model.addAttribute("pageStart", pageStart);
	model.addAttribute("pageEnd", pageEnd);
	model.addAttribute("users", users);		
	
	return "adminUsers";
	}

	
	

	//////////////////// 유저페이지
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/mypage/userpage")
	
	// import org.springframework.security.core.userdetails.User; 추가!
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

	// 유저페이지 - 처리하는 페이지
	@PreAuthorize("isAuthenticated()") // 로그인한 사용자에게만 메서드가 호출된다
	@PostMapping("/mypage/userpage")
	public String processuserpage(@Valid UpdateUserpageDTO dto, Errors errors, Model model, @AuthenticationPrincipal User user) {
		
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
		}
		
		if(errors.hasErrors()) {
			// 유저페이지 변경 실패 시 입력 데이터 값을 유지
			model.addAttribute("user", dto);
			
			// 유효성 통과 못한 필드와 메시지를 핸들링
			Map<String, String> validatorResult = userService.validateHandling(errors);
			for(String key : validatorResult.keySet()) {
				model.addAttribute(key, validatorResult.get(key));
			}
			return "userpage";
		}
			
		// 유효성 검사 통과 시의 실행 코드
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
	@GetMapping("/mypage/userpage/changepwd")
	public String showChangePwd(@AuthenticationPrincipal User user, Model model) {
		
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
		}
		
		return "changepwd";
	}
	
	@PostMapping("/mypage/userpage/changepwd")
    public String updatePassword(@Valid UserPasswordChangeDTO dto, Errors errors, Model model, Authentication authentication, @AuthenticationPrincipal User user) {
		
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
		}
		
		if (errors.hasErrors()) {
			// 회원가입 실패 시 입력 데이터 값을 유지
			model.addAttribute("dto", dto);
			
			// 유효성 통과 못한 필드와 메시지를 핸들링
			Map<String, String> validatorResult = userService.validateHandling(errors);
			
			for (String key : validatorResult.keySet()) {
				model.addAttribute(key, validatorResult.get(key));
			}
			return "changepwd"; // 유효성 검사 오류 시 다시 폼 페이지로
		}
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String result = userService.updateMemberPassword(dto, userDetails.getUsername());

        // 현재 비밀번호가 틀렸을 경우
        if (result == null) {
            model.addAttribute("dto", dto);
            model.addAttribute("wrongPassword", "입력하신 비밀번호와 저장된 비밀번호가 일치하지 않습니다.");
            
            return "changepwd";
        }
		
        // new password 비교
        if (!Objects.equals(dto.getNewPwd(), dto.getComfirmPwd())) {
            model.addAttribute("dto", dto);
            model.addAttribute("differentPassword", "새로 입력하신 비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
            return "changepwd";
        }

        

        // 성공하면 로그아웃
        return "redirect:/logout";
    }
	
	
	
	
	
	//////////////////// 탈퇴
	// 탈퇴 버튼 누르면 나오는 화면
	@GetMapping("/mypage/userpage/withdraw")
	public String showWithdrawForm(@AuthenticationPrincipal User user, Model model) {
		
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
		}
		
		// 탈퇴 폼 템플릿을 보여줌
		return "withdraw";
	}
	
	@PostMapping("/mypage/userpage/withdraw")
	//@ResponseBody
	public String processWithdrawForm(@RequestParam String checkPwd, @AuthenticationPrincipal User user,
			Model model) {
		
		boolean result = userService.deleteByUserId(user.getUsername(), checkPwd);
		if (result) {
			// 탈퇴 후 로그아웃하도록 리다이렉트
			return "redirect:/logout";
		} else {
			// success : 쿼리 문자열 파라미터
			// 로직을 처리하거나 사용자에게 메시지를 전달하는 데 사용
			model.addAttribute("inputPwd", "비밀번호를 다시 입력해주세요!");
			return "redirect:/mypage/userpage/withdraw?fail=true";
		}
	}
	
	
	

	/////////////////// 관리자가 강퇴하는 곳 -> 관리자사이트
	@PostMapping("/admin/ban")
	public String banUser(@RequestParam String userId) {

		// 입력 받은 아이디를 찾아서 그 아이디를 삭제시키기

		// 관리자가 강퇴 처리
		userService.deleteByAdmin(userId, UserRole.ADMIN);

		// 강퇴 후 관리자 대시보드로 리다이렉트
		return "redirect:/admin/dashboard";
	}

}
