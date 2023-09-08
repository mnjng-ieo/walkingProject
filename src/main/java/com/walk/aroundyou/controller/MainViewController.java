package com.walk.aroundyou.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.dto.ICourseResponseDTO;
import com.walk.aroundyou.repository.CourseRepository;
import com.walk.aroundyou.service.CourseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainViewController {
	
	private final CourseService courseService;
	
	/**
	 * [메인페이지] 산책로 인기순(조회순) 정렬 메소드
	 */	
	@GetMapping("/")
	public String getBestCourses(
			@RequestParam(name="sort", required= false) String sort,
			@RequestParam(name="page", required= false, 
			defaultValue = "0") int currentPage, 
			Model model) {
		
		Page<ICourseResponseDTO> coursePage = 
				courseService.findCoursesOrderByLikes();
		
		// 산책로 리스트 저장
		model.addAttribute("courses", coursePage);  
	
		return "main";
	}
	
}
