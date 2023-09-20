package com.walk.aroundyou.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domain.UploadImage;
import com.walk.aroundyou.domainenum.UserRole;
import com.walk.aroundyou.dto.CourseResponseDTO;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.dto.ICommentResponseDto;
import com.walk.aroundyou.service.BoardService;
import com.walk.aroundyou.service.CommentService;
import com.walk.aroundyou.service.CourseLikeService;
import com.walk.aroundyou.service.CourseService;
import com.walk.aroundyou.service.UploadImageService;
import com.walk.aroundyou.service.UserService;

import jakarta.persistence.EnumType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
public class CourseViewController {

	private final CourseService courseService;
	private final CourseLikeService courseLikeService;
	private final UploadImageService uploadImageService;
	private final CommentService commentService;
	private final UserService userService;
	private final BoardService boardService;
	
	// 페이지네이션 사이즈
	private final static int PAGINATION_SIZE = 5;
	
	/**
	 * [산책로 목록 조회페이지] 전체 목록 조회
	 *  ↳ REST로는 다수의 파라미터를 넘겨서 검색 조건이 되도록 했다.
	 *    뷰에서는 경로에 나타나지 않고 AJAX로 처리!
	 *    이 메소드는 ajax와 관계없이 전체 조회 첫 페이지 요청
	 */
	@GetMapping("/course")
	public String getCourses(
			@RequestParam(name="sort", required= false) String sort,
			@RequestParam(name="page", required= false, 
			defaultValue = "0") int currentPage, 
			Model model
			, @AuthenticationPrincipal User user) {
		// 헤더에 정보 추가하기 위한 코드
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
			Member currentUser = userService.findByUserId(user.getUsername()).get();
			if (currentUser != null) {
		        model.addAttribute("currentUser", currentUser);
				model.addAttribute("currentUserRole", currentUser.getRole().getRoleName());
				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
				if (currentUserImage != null) {
					String currentUserImagePath = 
							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
					model.addAttribute("currentUserImagePath", currentUserImagePath);
				}
			}
		} 
		
		Page<CourseResponseDTO> coursePage = 
				courseService.findAll(sort, currentPage);
		
		// pagination 설정
		int totalPages = coursePage.getTotalPages();
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
		
		// 산책로 리스트 저장
		model.addAttribute("courses", coursePage);  
		
		// 이미지 경로 넘기기
		// for 문을 돌려서 리스트 항목 각각의 이미지 경로를 얻어보자.
		// imagePaths 리스트의 순서와 coursePage 순서는 일치한다!
		// -> page 객체라 한 번 로드되는 courseResponseDTO는 12개 뿐이다.
		List<String> imagePaths = new ArrayList<>();
		for (CourseResponseDTO courseResponseDTO : coursePage.getContent()) {
			//UploadImage uploadImage = courseResponseDTO.getCourseImageId();
			Course course = courseService.findById(courseResponseDTO.getCourseId());
			UploadImage uploadImage = uploadImageService.findByCourse(course);
			if (uploadImage != null) {
				String imagePath = 
						uploadImageService.findCourseFullPathById(
								uploadImage.getFileId());
				imagePaths.add(imagePath);
			} else {
				// 여기 기본 이미지를 어떤 걸로 해야할지 약간 고민스럽다. 
				imagePaths.add("/images/defaultCourseMainImg.jpg");
			}
		}
		// 모델에 이미지 경로 리스트 추가
		model.addAttribute("imagePaths", imagePaths);
		
		// courseList.html라는 뷰 조회
		return "/course/courseList";
	}

	/**
	 * [산책로 목록 조회페이지] 검색 조건에 따른 전체 목록 조회
	 * : 메뉴를 클릭했을 때 나오는 첫 화면과 별개로 조건에 따른 AJAX 요청 경로 처리
	 */
	@GetMapping("/course/search")
	public String getCoursesByConditions(
			@RequestParam(name = "region", required = false) String region, 
			@RequestParam(name = "level", required = false) String level, 
			@RequestParam(name = "time", required = false) String time,
			@RequestParam(name = "distance", required = false) String distance, 
			@RequestParam(name = "searchTargetAttr", required = false) String searchTargetAttr,
			@RequestParam(name = "searchKeyword", required = false) String searchKeyword, 
			@RequestParam(name= "sort", required= false) String sort,
			@RequestParam(name= "page", required= false, 
						  defaultValue = "0") int currentPage,
			Model model, 
			@AuthenticationPrincipal User user) {
		// 헤더에 정보 추가하기 위한 코드
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
			Member currentUser = userService.findByUserId(user.getUsername()).get();
			if (currentUser != null) {
		        model.addAttribute("currentUser", currentUser);
				model.addAttribute("currentUserRole", currentUser.getRole().getRoleName());
				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
				if (currentUserImage != null) {
					String currentUserImagePath = 
							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
					model.addAttribute("currentUserImagePath", currentUserImagePath);
				}
			}
		}
		String startTime = null;
		String endTime = null; 
		
		if(time == null) {
			
		} else if(time.equals("1")) {
			startTime = "00:00:00";
			endTime = "00:59:00";
	    } else if (time.equals("2")) {
	    	startTime = "01:00:00";
	    	endTime = "01:59:00";
	    } else if (time.equals("3")) {
	    	startTime = "02:00:00";
	    	endTime = "02:59:00";
	    } else if (time.equals("4")) {
	    	startTime = "03:00:00";
	    	endTime = "03:59:00";
	    } else if (time.equals("5")) {
	    	startTime = "04:00:00";
	    	endTime = "99:00:00";
	    }
	
		Page<CourseResponseDTO> coursePage = 
				courseService.findAllByCondition(
				region, level, distance, startTime, endTime,
				searchTargetAttr, searchKeyword, sort, currentPage);
		
		// pagination 설정
		// 헷갈리지 말자 : currentPage는 0부터 시작!
		int totalPages = coursePage.getTotalPages();
		int pageStart = getPageStart(currentPage, totalPages);
		int pageEnd = 
				(PAGINATION_SIZE < totalPages)? 
						pageStart + PAGINATION_SIZE - 1
						:totalPages;

	    if(pageEnd == 0) {
	    	pageEnd = 1;
	    }
		model.addAttribute("lastPage", totalPages);
		// 이전, 이후 버튼에 문제있어서 currentPage + 1 값을 수정했다.
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("pageStart", pageStart);
		model.addAttribute("pageEnd", pageEnd);
		
		// 산책로 리스트 저장
		model.addAttribute("courses", coursePage);  
		
		// 이미지 경로 넘기기
		// for 문을 돌려서 리스트 항목 각각의 이미지 경로를 얻어보자.
		// imagePaths 리스트의 순서와 coursePage 순서는 일치한다!
		// -> page 객체라 한 번 로드되는 courseResponseDTO는 12개 뿐이다.
		List<String> imagePaths = new ArrayList<>();
		for (CourseResponseDTO courseResponseDTO : coursePage.getContent()) {
			//UploadImage uploadImage = courseResponseDTO.getCourseImageId();
			Course course = courseService.findById(courseResponseDTO.getCourseId());
			UploadImage uploadImage = uploadImageService.findByCourse(course);
			if (uploadImage != null) {
				String imagePath = 
						uploadImageService.findCourseFullPathById(
								uploadImage.getFileId());
				imagePaths.add(imagePath);
			} else {
				// 여기 기본 이미지를 어떤 걸로 해야할지 약간 고민스럽다. 
				imagePaths.add("/images/defaultCourseMainImg.jpg");
			}
		}
		
		// 모델에 이미지 경로 리스트 추가
		model.addAttribute("imagePaths", imagePaths);
		
		// 파라미터 값을 모델에 저장 (페이지네이션에 쓰임) - 알아요 쓸데없이 긴 거ㅠㅠ
		model.addAttribute("region", region);
		model.addAttribute("level", level);
		model.addAttribute("distance", distance);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("searchTargetAttr", searchTargetAttr);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("sort", sort);
		
		// courseList.html라는 뷰 조회
		return "/course/courseList";
	}

	
	/**
	 * [산책로 상세페이지] 유저 정보 기반해서 서비스 사용 가능
	 * + 연서님이 작성한 게시판 페이징 기능 관련 코드 추가
	 */
	@GetMapping("/course/{courseId}")
	public String getCourse(
			@PathVariable Long courseId, 
			//Principal principal,
			@RequestParam(name = "page", required=false, defaultValue="0") int currentPage,
	        @RequestParam(name = "sort", required= false, defaultValue = "boardId") String sort,
			Model model, 
			@AuthenticationPrincipal User user) {
		// 조회한 좋아요 상태 확인
		boolean isLiked = false;
		
		// 헤더에 정보 추가하기 위한 코드 + 좋아요 상태 
		if (user != null) {
			String userId = user.getUsername(); // 실제 로그인한 유저 정보
			//model.addAttribute("userId", userId);
			model.addAttribute("loginId", userId);
			isLiked = courseLikeService.isCourseLiked(userId, courseId);
			log.info("userId = {}", userId);
			Member currentUser = userService.findByUserId(user.getUsername()).get();
			log.info("currentUser = {}", currentUser.toString());
			if (currentUser != null) {
				// 유저정보 보내기
		        model.addAttribute("currentUser", currentUser);
				model.addAttribute("currentUserRole", currentUser.getRole().getRoleName());
				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
				// 유저 닉네임 보내기
				String currentUserNickname = currentUser.getUserNickname();
				model.addAttribute("currentUserNickname", currentUserNickname);
				// 유저 이미지 보내기
				if (currentUserImage != null) {
					String currentUserImagePath = 
							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
					model.addAttribute("currentUserImagePath", currentUserImagePath);
				}
			}
		} else {
		}		
		model.addAttribute("isLiked", isLiked);
		
		
		CourseResponseDTO courseResponseDTO = 
				courseService.findByIdWithCounts(courseId);
		model.addAttribute("course", courseResponseDTO);
		model.addAttribute("courseId", courseId);
		
		
		// 지도 모아보기 관련 코드 추가
		String courseFlag = courseResponseDTO.getWlkCoursFlagNm();
        List<Course> courseNames = courseService.findCourseNameByWlkCoursFlagNm(courseFlag);
        log.info(""+courseNames.size());
        model.addAttribute("courseNames", courseNames);
		
		// 이미지 경로 넘기기
		//UploadImage uploadImage = courseResponseDTO.getCourseImageId();
		Course course = courseService.findById(courseId);
		UploadImage uploadImage = uploadImageService.findByCourse(course);
		String imagePath;
		if (uploadImage != null) {
			imagePath = 
					uploadImageService.findCourseFullPathById(
							uploadImage.getFileId());
			log.info("imagePath : " + imagePath);
			model.addAttribute("imagePath", imagePath);
		} 
		
		////// 9/14 댓글 리스트 불러오기(값 없을 때 체크)
		// + 댓글 목록 유저 이미지 (0916 추가)
		List<ICommentResponseDto> comments = commentService.findByCourseId(courseId);
		List<String> commentMemberImagePaths = new ArrayList<>();
		
		if(!comments.isEmpty()) {			
			log.info("comment 값이 있음");	
			model.addAttribute("comments", comments);
			
			// 해당 댓글 목록이 비어있지 않다면 사용자 목록을 구해오고, 그 순서대로 이미지의 리스트를 얻자...
			for(ICommentResponseDto comment : comments) {
				Member commentMember = userService.findByUserId(comment.getUserId()).get();
				UploadImage commentMemberImage = uploadImageService.findByUser(commentMember);
				if(commentMemberImage != null) {
					String commentMemberImagePath = 
							uploadImageService.findUserFullPathById(commentMemberImage.getFileId());
					commentMemberImagePaths.add(commentMemberImagePath);	
				} else {
					commentMemberImagePaths.add("/images/defaultUserImage.png");
				}
			model.addAttribute("commentMemberImagePaths", commentMemberImagePaths);
			}
		}
		
		// 게시글 출력 용도
		Page<IBoardListResponse> courseBoardList = 
		         courseService.findBoardAndCntByCourseId(courseId, currentPage, sort);
		model.addAttribute("courseBoardList", courseBoardList);
		      
		// *** 게시물 작성자 이미지 경로 가져오기 - 0916 추가
		if(courseBoardList != null && !courseBoardList.isEmpty()) {
			List<String> writerMemberImagePaths = new ArrayList<>();
			
			for(IBoardListResponse boardResponseDTO : courseBoardList.getContent()) {
				Board board = boardService.findById(boardResponseDTO.getBoardId()).get();
				if (board != null) {
					// 작성자 이미지 불러오기
					UploadImage writerMemberImage = 
							uploadImageService.findByUser(board.getUserId());
					String writerMemberImagePath = (writerMemberImage != null) ?
							uploadImageService.
							findUserFullPathById(writerMemberImage.getFileId()) :
								"/images/defaultUserImage.png";
					writerMemberImagePaths.add(writerMemberImagePath);
				}
				model.addAttribute("wirterMemberImagePaths", writerMemberImagePaths);
			}
		}
	    // pagination 설정
	    int totalPages = courseBoardList.getTotalPages();
	    int pageStart = getPageStart(currentPage, totalPages);
	    int pageEnd = (PAGINATION_SIZE < totalPages)? 
	                  pageStart + PAGINATION_SIZE - 1
	                  :totalPages;
	    if(pageEnd == 0) {
	    	pageEnd = 1;
	    }
	    model.addAttribute("lastPage", totalPages);
	    model.addAttribute("currentPage", currentPage);
	    model.addAttribute("pageStart", pageStart);
	    model.addAttribute("pageEnd", pageEnd);
	    model.addAttribute("sort", sort);
		
		return "/course/course";
	}
	
	
	/**
	 * [관리자 페이지] 산책로 데이터 관리 - 산책로 목록 조회
	 * : 관리자만 볼 수 있는 탭의 산책로 데이터 관리 메뉴에서는
	 *   사용자와 달리 더 심플하게 구성되어 있다.
	 */
	@GetMapping("/admin/courses")
	public String adminGetCourses(
			@RequestParam(name = "region", required = false) String region, 
			@RequestParam(name = "level", required = false) String level, 
			@RequestParam(name = "time", required = false) String time,
			@RequestParam(name = "distance", required = false) String distance, 
			@RequestParam(name = "searchTargetAttr", required = false) String searchTargetAttr,
			@RequestParam(name = "searchKeyword", required = false) String searchKeyword, 
			@RequestParam(name="sort", required= false) String sort,
			@RequestParam(name="page", required= false, 
			defaultValue = "0") int currentPage,
			Model model, 
			@AuthenticationPrincipal User user) {
		// 헤더에 정보 추가하기 위한 코드
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
			Member currentUser = userService.findByUserId(user.getUsername()).get();
			if (currentUser != null && currentUser.getRole() == UserRole.valueOf("ADMIN")) {
		        model.addAttribute("currentUser", currentUser);
				model.addAttribute("currentUserRole", currentUser.getRole().getRoleName());
				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
				if (currentUserImage != null) {
					String currentUserImagePath = 
							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
					model.addAttribute("currentUserImagePath", currentUserImagePath);
				}
			} else {
				return "redirect:/";
			}
		} else {
			return "redirect:/";
		}
			
			String startTime = null;
			String endTime = null; 
			
			if(time == null) {
				
			} else if(time.equals("1")) {
				startTime = "00:00:00";
				endTime = "00:59:00";
		    } else if (time.equals("2")) {
		    	startTime = "01:00:00";
		    	endTime = "01:59:00";
		    } else if (time.equals("3")) {
		    	startTime = "02:00:00";
		    	endTime = "02:59:00";
		    } else if (time.equals("4")) {
		    	startTime = "03:00:00";
		    	endTime = "03:59:00";
		    } else if (time.equals("5")) {
		    	startTime = "04:00:00";
		    	endTime = "99:00:00";
		    }
			
		Page<CourseResponseDTO> coursePage = 
				courseService.findAllByCondition(
				region, level, distance, startTime, endTime,
				searchTargetAttr, searchKeyword, 
				sort, currentPage);
		
		// pagination 설정
		// 헷갈리지 말자 : currentPage는 0부터 시작!
		int totalPages = coursePage.getTotalPages();
		int pageStart = getPageStart(currentPage, totalPages);
		int pageEnd = 
				(PAGINATION_SIZE < totalPages)? 
						pageStart + PAGINATION_SIZE - 1
						:totalPages;
		if(pageEnd == 0) {
	    	pageEnd = 1;
	    }
		model.addAttribute("lastPage", totalPages);
		// 이전, 이후 버튼에 문제있어서 currentPage + 1 값을 수정했다.
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("pageStart", pageStart);
		model.addAttribute("pageEnd", pageEnd);
		
		// 산책로 리스트 저장
		model.addAttribute("courses", coursePage);  
		
		// 이미지 경로 넘기기
		// for 문을 돌려서 리스트 항목 각각의 이미지 경로를 얻어보자.
		// imagePaths 리스트의 순서와 coursePage 순서는 일치한다!
		// -> page 객체라 한 번 로드되는 courseResponseDTO는 12개 뿐이다.
		List<String> imagePaths = new ArrayList<>();
		for (CourseResponseDTO courseResponseDTO : coursePage.getContent()) {
			//UploadImage uploadImage = courseResponseDTO.getCourseImageId();
			Course course = courseService.findById(courseResponseDTO.getCourseId());
			UploadImage uploadImage = uploadImageService.findByCourse(course);
			if (uploadImage != null) {
				String imagePath = 
						uploadImageService.findCourseFullPathById(
								uploadImage.getFileId());
				imagePaths.add(imagePath);
			} else {
				// 여기 기본 이미지를 어떤 걸로 해야할지 약간 고민스럽다. 
				imagePaths.add("/images/defaultCourseMainImg.jpg");
			}
		}
		
		// 모델에 이미지 경로 리스트 추가
		model.addAttribute("imagePaths", imagePaths);
		
		// 파라미터 값을 모델에 저장 (페이지네이션에 쓰임) - 알아요 쓸데없이 긴 거ㅠㅠ
		model.addAttribute("region", region);
		model.addAttribute("level", level);
		model.addAttribute("distance", distance);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("searchTargetAttr", searchTargetAttr);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("sort", sort);
		
		// courseList.html라는 뷰 조회
		return "/course/adminCourseList";
	}

	/**
	 * [관리자 페이지] 산책로 데이터 관리 - 상세 조회 페이지
	 */
	@GetMapping("/admin/courses/{id}")
	public String adminGetCourse(@PathVariable Long id, Model model, 
			@AuthenticationPrincipal User user) {
		// 헤더에 정보 추가하기 위한 코드
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
			Member currentUser = userService.findByUserId(user.getUsername()).get();
			if (currentUser != null && currentUser.getRole() == UserRole.valueOf("ADMIN")) {
		        model.addAttribute("currentUser", currentUser);
				model.addAttribute("currentUserRole", currentUser.getRole().getRoleName());
				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
				if (currentUserImage != null) {
					String currentUserImagePath = 
							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
					model.addAttribute("currentUserImagePath", currentUserImagePath);
				}
			} else {
				return "redirect:/";
			}
		} else {
			return "redirect:/";
		}
		
		CourseResponseDTO courseResponseDTO = courseService.findByIdWithCounts(id);
		
		// 이미지 경로 넘기기
		//UploadImage uploadImage = courseResponseDTO.getCourseImageId();
		Course course = courseService.findById(id);
		UploadImage uploadImage = uploadImageService.findByCourse(course);
		
		String imagePath;
		if (uploadImage != null) {
			String savedImageName = uploadImage.getSavedFileName();
			model.addAttribute("savedImageName", savedImageName);
			
			imagePath = 
					uploadImageService.findCourseFullPathById(
							uploadImage.getFileId());
			log.info("imagePath : " + imagePath);
			model.addAttribute("imagePath", imagePath);
		} 
		model.addAttribute("course", courseResponseDTO);
		
		return "/course/adminCourse";
	}

	/**
	 * [관리자 페이지] 산책로 데이터 관리 - 산책로 생성
	 */
	@GetMapping("/admin/courses/new-course")
	public String adminNewCourse(Model model, 
			@AuthenticationPrincipal User user) {
		// 헤더에 정보 추가하기 위한 코드
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
			Member currentUser = userService.findByUserId(user.getUsername()).get();
			if (currentUser != null && currentUser.getRole() == UserRole.valueOf("ADMIN")) {
		        model.addAttribute("currentUser", currentUser);
				model.addAttribute("currentUserRole", currentUser.getRole().getRoleName());
				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
				if (currentUserImage != null) {
					String currentUserImagePath = 
							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
					model.addAttribute("currentUserImagePath", currentUserImagePath);
				}
			}
		}
		model.addAttribute("course", new CourseResponseDTO());
		
		return "/course/adminInsertCourse";
	}
	
	/**
	 * [관리자 페이지] 산책로 데이터 관리 - 산책로 수정 폼!
	 */
	@GetMapping("/admin/courses/update/{id}")
	public String adminNewCourse(
			@PathVariable Long id, Model model, 
			@AuthenticationPrincipal User user) {
		// 헤더에 정보 추가하기 위한 코드
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
			Member currentUser = userService.findByUserId(user.getUsername()).get();
			if (currentUser != null && currentUser.getRole() == UserRole.valueOf("ADMIN")) {
		        model.addAttribute("currentUser", currentUser);
				model.addAttribute("currentUserRole", currentUser.getRole().getRoleName());
				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
				if (currentUserImage != null) {
					String currentUserImagePath = 
							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
					model.addAttribute("currentUserImagePath", currentUserImagePath);
				}
			} else {
				return "redirect:/";
			}
		} else {
			return "redirect:/";
		}
		
		Course course = courseService.findById(id);
		
		// 이미지 경로 넘기기
		//UploadImage uploadImage = course.getCourseImageId();
		UploadImage uploadImage = uploadImageService.findByCourse(course);
		
		String imagePath;
		if (uploadImage != null) {
			imagePath = 
					uploadImageService.findCourseFullPathById(
							uploadImage.getFileId());
			log.info("imagePath : " + imagePath);
			model.addAttribute("imagePath", imagePath);
		} 
		model.addAttribute("course", new CourseResponseDTO(course));
		
		return "/course/adminUpdateCourse";
	}
	
	/**
	 *  pagination의 첫번째 숫자 얻는 메소드
	 */
	private int getPageStart(int currentPage, int totalPages) {
		
		log.info("currentPage = {}, totalPages = {}", currentPage, totalPages);
		int result = 1;
		if (Math.max(totalPages, PAGINATION_SIZE) < currentPage + (int) Math.ceil((double) PAGINATION_SIZE / 2)) {
			log.info("if문 통과");
			result = totalPages - PAGINATION_SIZE + 1;
		} else if (currentPage > (int) Math.floor((double) PAGINATION_SIZE / 2)) {
			result = currentPage - (int) Math.floor((double) PAGINATION_SIZE / 2) + 1;
			log.info("else if문 통과");
		}
		
		return result;
	}
}

	
