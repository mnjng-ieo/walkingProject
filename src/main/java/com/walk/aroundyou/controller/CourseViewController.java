package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.walk.aroundyou.dto.CourseResponseDTO;
import com.walk.aroundyou.repository.CourseRepository;
import com.walk.aroundyou.service.CourseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CourseViewController {

	private final CourseService courseService;
	private final CourseRepository courseRepository;
	
	/**
	 * [산책로 목록 조회페이지] 검색 조건에 따른 전체 목록 조회
	 *  ↳ REST로는 다수의 파라미터를 넘겨서 검색 조건이 되도록 했다.
	 *    뷰에서는 경로에 나타나지 않고 AJAX로 처리하면 좋을 것 같은데,
	 *    파라미터였던 검색 조건들을 하나의 dto 객체로 만들어서
	 *    Requestbody에 실는 것이 가능한가?
	 *    일단 조건없이 목록 조회하는 것(courseService.findAll)으로 뷰를 구현해보고 시도해보자.  
	 */
	@GetMapping("/course")
	public String getCourses(Model model) {
		List<CourseResponseDTO> courses = courseService.findAll()
				.stream()
				.map(course -> {
					CourseResponseDTO dto = new CourseResponseDTO(course);
					// 좋아요 수, 언급 수, 댓글 수 추가
					dto.setLikeCnt(
							courseRepository.countCourseLikesByCourseId(
									course.getCourseId()));;
					dto.setMentionCnt(
							courseRepository.countCourseMentionsByCourseId(
									course.getCourseId()));
					dto.setCommentCnt(
							courseRepository.countCourseCommentsByCourseId(
									course.getCourseId()));;
					return dto;
				})
				.toList();
		// 산책로 리스트 저장
		model.addAttribute("courses", courses);  
		
		// courseList.html라는 뷰 조회
		return "courseList";
	}
	
}
