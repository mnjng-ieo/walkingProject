//package com.walk.aroundyou.controller;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.domain.Page;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.walk.aroundyou.domain.Course;
//import com.walk.aroundyou.domain.Member;
//import com.walk.aroundyou.domain.Tag;
//import com.walk.aroundyou.domain.UploadImage;
//import com.walk.aroundyou.dto.IBoardListResponse;
//import com.walk.aroundyou.dto.ICourseResponseDTO;
//import com.walk.aroundyou.repository.CourseRepository;
//import com.walk.aroundyou.service.CourseService;
//import com.walk.aroundyou.service.TagService;
//import com.walk.aroundyou.service.UploadImageService;
//import com.walk.aroundyou.service.UserService;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@RequiredArgsConstructor
//@Controller
//@Slf4j
//public class MainViewController {
//	
//	private final CourseService courseService;
//	private final TagService tagService;
//	private final CourseRepository courseRepo;
//	private final UserService userService;
//	private final UploadImageService uploadImageService;
//	
//	@GetMapping("/")
//	public String getMain(
//			@AuthenticationPrincipal User user, Model model) {
//		// 헤더에 정보 추가하기 위한 코드
//		if (user != null) {
//			model.addAttribute("loginId", user.getUsername());
//			Member currentUser = userService.findByUserId(user.getUsername()).get();
//			if (currentUser != null) {
//				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
//				if (currentUserImage != null) {
//					String currentUserImagePath = 
//							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
//					model.addAttribute("currentUserImagePath", currentUserImagePath);
//				}
//			}
//		}
//		
//		// 헤더에 정보 추가하기 위한 코드
//		if (user != null) {
//			model.addAttribute("loginId", user.getUsername());
//			Member currentUser = userService.findByUserId(user.getUsername()).get();
//			if (currentUser != null) {
//				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
//				if (currentUserImage != null) {
//					String currentUserImagePath = 
//							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
//					model.addAttribute("currentUserImagePath", currentUserImagePath);
//				}
//			}
//		}
//		
//		// BEST 9개 출력
//		Page<ICourseResponseDTO> coursePage = courseService.findCoursesOrderByLikes();
//		
//		// 가장 많이 사용된 태그 메인화면에 출력하기 
//		List<String> hotTagList = tagService.findTagsByBoardTagId();
//
//		// 좋아요 순이 가장 많은 태그(=hotTagList.get(0))의 게시물 출력됨 
//		List<IBoardListResponse> tagBoardList = tagService.findBoardAndCntByMainTagDefault(hotTagList.get(0));
//		
//		model.addAttribute("courses", coursePage);  
//		model.addAttribute("hotTagList", hotTagList);
//		model.addAttribute("tagBoardList", tagBoardList);
//
//		return "main";
//	}
//	
//	// Ajax로 처리됨 
//	@GetMapping("/api/tag/board")
//	public String getTagBoard(@RequestParam(name="tagContent") String tagContent, Model model) {
//		// tag_content으로 tag_id 추출하기
//		Tag tagId = tagService.findIdByTagContent(tagContent);
//		// 위에서 추출한 tag_id로 관련된 게시물 출력
//		Page<IBoardListResponse> tagBoardList = tagService.findBoardAndCntByMainTagId(tagId);
//		
//		model.addAttribute("tagBoardList", tagBoardList);
//		model.addAttribute("tagContent", tagContent);
//		
//		return "board/tagBoardList";
//	}
//	
//	// 지도 테스트용 임시 API
////	@GetMapping("/map-test")
////	public String getMapTest(Model model, Long id) {
////		Optional<Course> course = courseRepo.findById(id);
////		model.addAttribute("course", course.get());
////		
////		String courseFlag = course.get().getWlkCoursFlagNm();
////		List<Course> courseNames = courseRepo.findCourseNamesByCourseFlagName(courseFlag);
////		log.info(""+courseNames.size());
////		model.addAttribute("courseNames", courseNames);
////		return "mapTest";
////	}
//
//}
