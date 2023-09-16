package com.walk.aroundyou.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.CourseLike;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.dto.CourseLikeRequestDTO;
import com.walk.aroundyou.repository.CourseLikeRepository;
import com.walk.aroundyou.repository.CourseRepository;
import com.walk.aroundyou.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseLikeService {

	private final CourseLikeRepository courseLikeRepository;
	private final UserRepository userRepository;
	private final CourseRepository courseRepository;
	
	/**
	 * course_like 테이블에 데이터 있으면 true(1) 반환, 없으면 false(0) 반환
	 */
	public boolean isCourseLiked(String userId, Long courseId) {
		
		Member user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new IllegalArgumentException(
						"user id not found : " + userId));
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException(
						"course id not found : " + courseId));
		
		Optional<CourseLike> courseLike = 
				courseLikeRepository.findByUserIdAndCourseId(user, course);
		return courseLike.isPresent();   
	}
	
	/**
	 * 한 명의 유저가 산책로에 좋아요 추가
	 * @throws Exception 
	 */
	@Transactional
	public void insertLike(CourseLikeRequestDTO courseLikeRequestDTO) throws Exception {
		
		// 좋아요를 한 user의 id와 course의 id를 전달받아 각각 데이터를 찾는다!
		Member user = userRepository.findByUserId(courseLikeRequestDTO.getUserId())
				.orElseThrow(() -> new IllegalArgumentException(
						"user id not found : " + courseLikeRequestDTO.getUserId()));
		Course course = courseRepository.findById(courseLikeRequestDTO.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException(
						"course id not found : " + courseLikeRequestDTO.getCourseId()));
		
		// courseLike에 데이터가 이미 있는 경우, 이미 좋아요 된 상태이므로 에러 반환
		if(courseLikeRepository.findByUserIdAndCourseId(user, course).isPresent()) {
			throw new Exception("ALREADY_LIKED");
		}
		
		// CourseLike 엔티티 클래스에 @Builder 어노테이션 추가해줌
		// DTO로부터 찾은 user와 course로 엔티티를 만든다.
		CourseLike courseLike = CourseLike.builder()
				.courseId(course)
				.userId(user)
				.build();
		
		// 디비에 최종 저장한다.
		courseLikeRepository.save(courseLike);
	}

	
	/**
	 * 한 명의 유저가 산책로에 좋아요 삭제
	 */
	@Transactional
	public void deleteLike(CourseLikeRequestDTO courseLikeRequestDTO) {
		
		// 좋아요 취소를 한 user의 id와 course의 id를 전달받아 각각 데이터를 찾는다!
		Member user = userRepository.findByUserId(courseLikeRequestDTO.getUserId())
				.orElseThrow(() -> new IllegalArgumentException(
						"user id not found : " + courseLikeRequestDTO.getUserId()));
		Course course = courseRepository.findById(courseLikeRequestDTO.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException(
						"course id not found : " + courseLikeRequestDTO.getCourseId()));

		
		// 찾은 user와 course로 courseLike 데이터를 찾는다. 
		// 찾는 courseLike 데이터가 없다면, 없는 좋아요를 취소하는 것이므로 에러 반환
		CourseLike courseLike = 
				courseLikeRepository.findByUserIdAndCourseId(user, course)
				.orElseThrow(() -> new IllegalArgumentException(
						"courseLike id not found"));
		
		// 해당 courseLike를 최종 삭제한다.
		courseLikeRepository.delete(courseLike);
	}	
}