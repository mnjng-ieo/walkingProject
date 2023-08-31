package com.walk.aroundyou.service;

import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.CourseLike;
import com.walk.aroundyou.domain.User;
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
	 * 산책로 좋아요 추가
	 */
	@Transactional
	public void insertLike(CourseLikeRequestDTO courseLikeRequestDTO) {
		
		// 좋아요를 한 user의 id와 course의 id를 전달받아 각각 데이터를 찾는다!
		User user = userRepository.findById(courseLikeRequestDTO.getUserId())
				.orElseThrow(() -> new IllegalArgumentException(
						"user id not found : " + courseLikeRequestDTO.getUserId()));
		Course course = courseRepository.findById(courseLikeRequestDTO.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException(
						"course id not found : " + courseLikeRequestDTO.getCourseId()));
		
		// CourseLike 엔티티 클래스에 @Builder 어노테이션 추가해줌
		// DTO로부터 찾은 user와 course로 엔티티를 만든다.
		CourseLike courseLike = CourseLike.builder()
				.course(course)
				.user(user)
				.build();
		
		// 디비에 최종 저장한다.
		courseLikeRepository.save(courseLike);
	}
	
	/**
	 * 산책로 좋아요 삭제
	 */
	@Transactional
	public void deleteLike(CourseLikeRequestDTO courseLikeRequestDTO) {
		
		// 좋아요 취소를 한 user의 id와 course의 id를 전달받아 각각 데이터를 찾는다!
		User user = userRepository.findById(courseLikeRequestDTO.getUserId())
				.orElseThrow(() -> new IllegalArgumentException(
						"user id not found : " + courseLikeRequestDTO.getUserId()));
		Course course = courseRepository.findById(courseLikeRequestDTO.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException(
						"course id not found : " + courseLikeRequestDTO.getCourseId()));
		
		// 찾은 user와 course로 courseLike 데이터를 찾는다. 
		CourseLike courseLike = 
				courseLikeRepository.findByUserAndCourse(user, course)
				.orElseThrow(() -> new IllegalArgumentException(
						"courseLike id not found"));
		
		// 해당 courseLike를 최종 삭제한다.
		courseLikeRepository.delete(courseLike);
	}
	
}
