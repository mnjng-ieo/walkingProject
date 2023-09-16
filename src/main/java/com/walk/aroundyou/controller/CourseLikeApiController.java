package com.walk.aroundyou.controller;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.dto.CourseLikeRequestDTO;
import com.walk.aroundyou.service.CourseLikeService;
import com.walk.aroundyou.service.CourseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping이 클래스 위에 선언될 경우, 메소드에 사용한 설정으로 덮어쓰인다.
@RequestMapping("/api/courses/{courseId}")
public class CourseLikeApiController {
	
	private final CourseLikeService courseLikeService;
	private final CourseService courseService;

	// 원래 POST/DELETE 요청으로 분리해서 좋아요와 좋아요 취소 요청을 따로 처리했는데
	// 같은 동작(경로) 안에서 둘 다 되도록 isLiked를 활용해서 코드를 수정했습니다. 
	
	/**
	 * [산책로 상세페이지] 산책로 좋아요/좋아요 취소 요청 처리
	 * - Principal : 스프링 시큐리티에서 인증한 사용자 정보를 매핑하는 객체
	 * 				보통 사용자의 아이디나 식별자 같은 정보 포함
	 * 				현재 로그인한 사용자에 대한 정보를 얻기 위해 매개변수로 사용 가능
	 *              로그인한 상태에서 좋아요 버튼 누르는 거니까 principal 사용
	 * - @RequestBody : POST 방식의 HTTP 요청할 때
	 *                  응답에 해당하는 JSON 형식의 값을, 애너테이션이 붙은 대상 객체에 매핑
	 */         
	@PostMapping
	public ResponseEntity<CourseLikeRequestDTO> AddCourseLike(
			@PathVariable long courseId,
			@AuthenticationPrincipal User user,
			@RequestBody CourseLikeRequestDTO request) throws Exception {
		request.setCourseId(courseId);
		
		//String userId = principal.getName();     // 실제 로그인한 유저 정보
		//String userId = "wayid1";                  // 테스트용. 직접 부여
		String userId = user.getUsername();
		request.setUserId(userId);  
		
		// 조회한 좋아요 상태를 확인
		boolean isLiked = courseLikeService.isCourseLiked(userId, courseId);
		
		if (isLiked) {
			// 좋아요 상태라면 좋아요 취소하기
			courseLikeService.deleteLike(request);
			log.info("DELETE_LIKE");
		} else {
			// 좋아요 아닌 상태라면 좋아요 하기
			courseLikeService.insertLike(request);
			log.info("INSERT_LIKE");
		}
		
		return ResponseEntity.ok()
				.body(request);
	}
	
	/**
	 * 좋아요 수 업데이트
	 */
	@GetMapping("/like-count")
	public ResponseEntity<?> getLikeCount(
			@PathVariable Long courseId) {
		int likeCount = 
				courseService.getCourseLikeCntByCourseId(courseId);
		
		// likeCount 변수 값을 JSON 객체로 매핑시킴
		return ResponseEntity.ok()
				.body(Collections.singletonMap("likeCount", likeCount));
	}
}