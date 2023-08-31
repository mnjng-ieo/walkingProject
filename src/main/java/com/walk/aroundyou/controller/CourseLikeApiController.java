package com.walk.aroundyou.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.CourseLikeRequestDTO;
import com.walk.aroundyou.dto.CourseRequestDTO;
import com.walk.aroundyou.service.CourseLikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
//@RequestMapping이 클래스 위에 선언될 경우, 메소드에 사용한 설정으로 덮어쓰인다.
//이걸 수정하거나 없애면 어떨까?
@RequestMapping("/courses")
public class CourseLikeApiController {
	
	private final CourseLikeService courseLikeService;

	// @RequestBody 대신 @PathVarable 을 사용하는 게 나을까?
	
	// @RequestBody : POST 방식의 HTTP 요청할 때
	//                응답에 해당하는 JSON 형식의 값을, 애너테이션이 붙은 대상 객체에 매핑
	@PostMapping("/courses/{courseId}")
	public ResponseEntity<Void> AddLikeCourse(
			@RequestBody CourseLikeRequestDTO request) {
		courseLikeService.insertLike(request);
		return ResponseEntity.status(HttpStatus.CREATED)
				.build();
	}
	
	@DeleteMapping("/courses/{courseId}")
	public ResponseEntity<Void> DeleteLikeCourse(
			@RequestBody CourseLikeRequestDTO request){
		courseLikeService.deleteLike(request);
		return ResponseEntity.ok()
				.build();
	}
}
