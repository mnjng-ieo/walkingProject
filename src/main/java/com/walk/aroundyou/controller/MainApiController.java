package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.dto.CourseResponseDTO;
import com.walk.aroundyou.service.MainService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MainApiController {

	private final MainService mainService;
	
	@GetMapping("api/search")
	public ResponseEntity<List<CourseResponseDTO>> findMainCourseByKeyword(
			@RequestParam(required = false) String keyword){
		
		List<CourseResponseDTO> courses = 
				mainService.findCourseByKeyword(keyword);
		return ResponseEntity.ok()
				.body(courses);
	}
}
