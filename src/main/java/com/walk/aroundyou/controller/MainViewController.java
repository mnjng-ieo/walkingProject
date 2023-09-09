package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.dto.ICourseResponseDTO;
import com.walk.aroundyou.service.CourseService;
import com.walk.aroundyou.service.TagService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainViewController {
	
	private final CourseService courseService;
	private final TagService tagService;
	
	/**
	 * [메인페이지] 산책로 인기순(조회순) 정렬 메소드
	 */	
//	@GetMapping("/")
//	public String getBestCourses(
//			@RequestParam(name="sort", required= false) String sort,
//			@RequestParam(name="page", required= false, 
//			defaultValue = "0") int currentPage, 
//			Model model) {
//		
//		Page<ICourseResponseDTO> coursePage = 
//				courseService.findCoursesOrderByLikes();
//		
//		// 산책로 리스트 저장
//		model.addAttribute("courses", coursePage);  
//	
//		return "main";
//	}
//	
//	// 가장 많이 사용된 태그 메인화면에 출력하기
//	@GetMapping("/")
//	public String getMainHotTag(Model model) {
//		List<String> hotTagList = 
//			tagService.findTagsByBoardTagId();
//		model.addAttribute("hotTagList", hotTagList);
//		return "main";
//	}
	
	@GetMapping("/")
	public String getMain(
			@RequestParam(name="sort", required= false) String sort,
			@RequestParam(name="page", required= false, 
			defaultValue = "0") int currentPage, 
			Model model) {
		
		// BEST 9개 출력
		Page<ICourseResponseDTO> coursePage = courseService.findCoursesOrderByLikes();
		
		// 가장 많이 사용된 태그 메인화면에 출력하기 
		List<String> hotTagList = tagService.findTagsByBoardTagId();
			
		model.addAttribute("courses", coursePage);  
		model.addAttribute("hotTagList", hotTagList);
		
		return "main";
	}
	
}
