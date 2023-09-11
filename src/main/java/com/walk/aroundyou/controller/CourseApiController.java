package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.CourseRequestDTO;
import com.walk.aroundyou.dto.CourseResponseDTO;
import com.walk.aroundyou.dto.ICourseResponseDTO;
import com.walk.aroundyou.repository.CourseRepository;
import com.walk.aroundyou.service.CourseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class CourseApiController {
	
	private final CourseService courseService;
	
	// id로 산책로 하나로 조회
	@GetMapping("/api/courses/{id}")
	public ResponseEntity<Course> findCourse(@PathVariable long id){
		Course course = courseService.findById(id);
		
		return ResponseEntity.ok()
				.body(course);
	}

	/**
	 * [산책로 상세 조회페이지] 좋아요, 언급, 댓글 수 포함 산책로 상세 정보 조회
	 */
	@GetMapping("/api/courses/detail/{id}")
	public ResponseEntity<CourseResponseDTO> findDetailCourse(@PathVariable Long id){
		log.info("courses/detail/{id} 들어감");
		CourseResponseDTO courseResponseDTO = courseService.findByIdWithCounts(id);
		return ResponseEntity.ok().body(courseResponseDTO);
	}
	
	/**
	 * [산책로 목록 조회페이지] 검색 조건에 따른 전체 목록 조회
	 * 요청에 대해 page 정보까지 반환받으려면 ResponseEntity<List<CourseResponseDTO>>이 아닌,
	 * ResponseEntity<Object> 으로 바꾸자.
	 * 일단 반환을 object로 하면 dto를 어떻게 매핑해야 할지 모르겠어서 그대로 냅뒀다.
	 */
	@GetMapping("/api/courses/search")
	public ResponseEntity<Page<CourseResponseDTO>> findAllCourses(
			// @RequestParam : 요청객체로부터 요청파라미터 자동추출
			@RequestParam(required = false) String region,
		    @RequestParam(required = false) String level,
		    @RequestParam(required = false) String distance,
		    @RequestParam(required = false) String startTime,
		    @RequestParam(required = false) String endTime,
		    @RequestParam(required = false) String searchTargetAttr,
		    @RequestParam(required = false) String searchKeyword,
		    @RequestParam(required = false) String sort,
		    @RequestParam(required = false, defaultValue = "0") Integer page
			){
			
		Page<CourseResponseDTO> coursePage = 
				courseService.findAllByCondition(
				region, level, distance, startTime, endTime,
				searchTargetAttr, searchKeyword, sort, page);
		
		return ResponseEntity.ok()
				.body(coursePage);
	}

	/**
	 * [관리자페이지] 산책로 생성 요청
	 */
	@PostMapping("/api/admin/courses")
	public ResponseEntity<Course> addCourse(
			@RequestBody CourseRequestDTO request){
		Course savedCourse = courseService.save(request);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(savedCourse);
	}
	
	/**
	 * [관리자페이지] 산책로 수정 요청
	 */
	@PatchMapping("/api/admin/courses/{id}")
	public ResponseEntity<Course> updateCourse(
			@PathVariable long id, @RequestBody CourseRequestDTO request){
		Course updatedCourse = courseService.updateCourse(id, request);
		
		return ResponseEntity.ok()
				.body(updatedCourse);
	}
	
	/**
	 * [관리자페이지] 산책로 삭제 요청
	 */
	@DeleteMapping("/api/admin/courses/{id}")
	public ResponseEntity<Course> deleteCourse(@PathVariable long id){
		courseService.deleteCourse(id);
		
		return ResponseEntity.ok()
				.build();
	}
	
	// 지역에 따른 산책로이름(산책로 큰분류) 가져오기
	@GetMapping("/api/courses/flagname")
	public ResponseEntity<List<String>> getWlkCoursFlagNm(String signguCn){
		log.info("getWlkCoursFlagNm() 컨트롤러 접근");
		return ResponseEntity.ok().body(courseService.findFlagNameBySignguCn(signguCn));
	}
	// 산책로이름에 따른 코스이름(산책로 작은분류) 정보 가져오기
	@GetMapping("/api/courses/coursename")
	public ResponseEntity<List<Course>> getWlkCoursNm(String wlkCoursFlagNm){
		log.info("getWlkCoursNm() 컨트롤러 접근");
		return ResponseEntity.ok().body(courseService.findCourseNameByWlkCoursFlagNm(wlkCoursFlagNm));
	}
}
