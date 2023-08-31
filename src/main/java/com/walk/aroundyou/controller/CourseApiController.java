package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.CourseRequestDTO;
import com.walk.aroundyou.dto.CourseResponseDTO;
import com.walk.aroundyou.service.CourseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CourseApiController {

	private final CourseService courseService;
	
	// id로 산책로 하나로 조회
	@GetMapping("/courses/{id}")
	public ResponseEntity<CourseResponseDTO> findCourse(@PathVariable long id){
		Course course = courseService.findById(id);
		
		return ResponseEntity.ok()
				.body(new CourseResponseDTO(course));
	}
	
	// 전체 목록 조회
	@GetMapping("/courses")
	public ResponseEntity<List<CourseResponseDTO>> findAllCourses(){
		List<CourseResponseDTO> courses = courseService.findAll()
				.stream()
				.map(CourseResponseDTO::new)
				.toList();
		return ResponseEntity.ok()
				.body(courses);
	}
	
	/**
	 * [산책로 목록 조회페이지] 검색 조건에 따른 전체 목록 조회
	 * 요청에 대해 page 정보까지 반환받으려면 ResponseEntity<List<CourseResponseDTO>>이 아닌,
	 * ResponseEntity<Object> 으로 바꾸자.
	 * 일단 반환을 object로 하면 dto를 어떻게 매핑해야 할지 모르겠어서 그대로 냅뒀다.
	 */
	@GetMapping("/courses/search")
	public ResponseEntity<List<CourseResponseDTO>> findAllCourses(
			// @RequestParam : 요청객체로부터 요청파라미터 자동추출
			@RequestParam(required = false) String region,
		    @RequestParam(required = false) String level,
		    @RequestParam(required = false) String distance,
		    @RequestParam(required = false) String startTime,
		    @RequestParam(required = false) String endTime,
		    @RequestParam(required = false) String searchTargetAttr,
		    @RequestParam(required = false) String searchKeyword,
		    @RequestParam(required = false) String sort,
		    @RequestParam(required = false) int page
			){
		
		List<CourseResponseDTO> courses = courseService.findAllByCondition(
				region, level, distance, startTime, endTime,
				searchTargetAttr, searchKeyword, sort, page)
				.stream()
				.map(CourseResponseDTO::new)
				.toList();
		
		return ResponseEntity.ok()
				.body(courses);
	}

	/**
	 * [관리자페이지] 산책로 생성 요청
	 */
	@PostMapping("/admin/courses")
	public ResponseEntity<Course> addCourse(
			@RequestBody CourseRequestDTO request){
		Course savedCourse = courseService.save(request);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(savedCourse);
	}
	
	/**
	 * [관리자페이지] 산책로 수정 요청
	 */
	@PatchMapping("/admin/courses/{id}")
	public ResponseEntity<Course> updateCourse(
			@PathVariable long id, @RequestBody CourseRequestDTO request){
		Course updatedCourse = courseService.updateCourse(id, request);
		
		return ResponseEntity.ok()
				.body(updatedCourse);
	}
	
	/**
	 * [관리자페이지] 산책로 삭제 요청
	 */
	@DeleteMapping("/admin/courses/{id}")
	public ResponseEntity<Course> deleteCourse(@PathVariable long id){
		courseService.deleteCourse(id);
		
		return ResponseEntity.ok()
				.build();
	}
}
