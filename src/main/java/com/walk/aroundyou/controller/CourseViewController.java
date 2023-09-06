package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.CourseResponseDTO;
import com.walk.aroundyou.repository.CourseRepository;
import com.walk.aroundyou.service.CourseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
public class CourseViewController {

	private final CourseService courseService;
	private final CourseRepository courseRepository;
	
	// 페이지네이션 사이즈
	private final static int PAGINATION_SIZE = 5;
	
	/**
	 * [산책로 목록 조회페이지] 전체 목록 조회
	 *  ↳ REST로는 다수의 파라미터를 넘겨서 검색 조건이 되도록 했다.
	 *    뷰에서는 경로에 나타나지 않고 AJAX나 POST 요청으로 처리하면 좋을 것 같은데,
	 *    파라미터였던 검색 조건들을 하나의 dto 객체로 만들어서
	 *    Requestbody에 실는 것이 가능한가? ... 
	 *    시도에 오랫동안 실패하고 처음 전체 조회 화면과 검색 결과를 분리하여
	 *    GET 방식 요청 처리로 만들었다.
	 */
	@GetMapping("/course")
	public String getCourses(
			@RequestParam(name="page", required= false, 
			defaultValue = "0") int currentPage, Model model) {
		
		Page<Course> coursePage = courseService.findAll(currentPage);
		List<CourseResponseDTO> courses = coursePage
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
		
		// pagination 설정
		int totalPages = coursePage.getTotalPages();
		int pageStart = getPageStart(currentPage, totalPages);
		int pageEnd = 
				(PAGINATION_SIZE < totalPages)? 
						pageStart + PAGINATION_SIZE - 1
						:totalPages;
		model.addAttribute("lastPage", totalPages);
		model.addAttribute("currentPage", currentPage + 1);
		model.addAttribute("pageStart", pageStart);
		model.addAttribute("pageEnd", pageEnd);
		
		// 산책로 리스트 저장
		model.addAttribute("courses", courses);  
		
		// courseList.html라는 뷰 조회
		return "courseList";
	}

	/**
	 * [산책로 목록 조회페이지] 검색 조건에 따른 전체 목록 조회
	 * : 메뉴를 클릭했을 때 나오는 첫 화면과 별개로 POST 요청을 처리해야 할 것 같아서 만들었다.
	 *   아직 완성 처리되지 않음.
	 */
	@GetMapping("/course/search")
	public String getCoursesByConditions(
			@RequestParam(name = "region", required = false) String region, 
			@RequestParam(name = "level", required = false) String level, 
			@RequestParam(name = "time", required = false) String time,
			@RequestParam(name = "distance", required = false) String distance, 
			@RequestParam(name = "searchTargetAttr", required = false) String searchTargetAttr,
			@RequestParam(name = "searchKeyword", required = false) String searchKeyword, 
			@RequestParam(name="sort", required= false) String sort,
			@RequestParam(name="page", required= false, 
			defaultValue = "0") int currentPage,
			Model model) {
			
			String startTime = null;
			String endTime = null; 
			
			if(time == null) {
				
			} else if(time.equals("1")) {
				startTime = "00:00:00";
				endTime = "00:59:00";
		    } else if (time.equals("2")) {
		    	startTime = "01:00:00";
		    	endTime = "01:59:00";
		    } else if (time.equals("3")) {
		    	startTime = "02:00:00";
		    	endTime = "02:59:00";
		    } else if (time.equals("4")) {
		    	startTime = "03:00:00";
		    	endTime = "03:59:00";
		    } else if (time.equals("5")) {
		    	startTime = "04:00:00";
		    	endTime = "99:00:00";
		    }
			
		Page<Course> coursePage = 
				courseService.findAllByCondition(
				region, level, distance, startTime, endTime,
				searchTargetAttr, searchKeyword, sort, currentPage);
		
		List<CourseResponseDTO> courses = coursePage
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
		
		// pagination 설정
		int totalPages = coursePage.getTotalPages();
		int pageStart = getPageStart(currentPage, totalPages);
		int pageEnd = 
				(PAGINATION_SIZE < totalPages)? 
						pageStart + PAGINATION_SIZE - 1
						:totalPages;
		model.addAttribute("lastPage", totalPages);
		model.addAttribute("currentPage", currentPage + 1);
		model.addAttribute("pageStart", pageStart);
		model.addAttribute("pageEnd", pageEnd);
		
		// 산책로 리스트 저장
		model.addAttribute("courses", courses);  
		
		// 파라미터 값을 모델에 저장 (페이지네이션에 쓰임) - 알아요 쓸데없이 긴 거ㅠㅠ
		model.addAttribute("region", region);
		model.addAttribute("level", level);
		model.addAttribute("distance", distance);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("searchTargetAttr", searchTargetAttr);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("sort", sort);
		
		// courseList.html라는 뷰 조회
		return "courseList";
	}
	
	/**
	 * [산책로 상세페이지] : 주영언니가 맡아서 윗부분 만들기로!
	 */
	@GetMapping("/course/{id}")
	public String getCourse(@PathVariable Long id, Model model) {
		CourseResponseDTO courseResponseDTO = courseService.findByIdWithCounts(id);
		model.addAttribute("course", courseResponseDTO);
		
		return "course";
	}
	
	/**
	 *  pagination의 첫번째 숫자 얻는 메소드
	 */
	private int getPageStart(int currentPage, int totalPages) {
		int result = 1; 
		if(totalPages < currentPage + (int)Math.floor(PAGINATION_SIZE/2)) {
			// 시작페이지의 최소값은 1!
			result = Math.max(1, totalPages - PAGINATION_SIZE + 1);
		} else if (currentPage > (int)Math.floor(PAGINATION_SIZE/2)) {
			result = currentPage - (int)Math.floor(PAGINATION_SIZE/2) + 1;
		}
		return result;
	}
	
//	/**
//	 * pagination의 마지막 숫자 얻는 메소드
//	 */
//	private int getPageEnd(int pageStart, int totalPages) {
//		int result = pageStart + PAGINATION_SIZE - 1;
//		if(totalPages < result) {
//			result = totalPages;
//			
//		}
//		return result;
//	}
	
}
