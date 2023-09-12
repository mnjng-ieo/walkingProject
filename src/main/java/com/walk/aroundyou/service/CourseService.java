package com.walk.aroundyou.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.CourseRequestDTO;
import com.walk.aroundyou.dto.CourseResponseDTO;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.dto.ICourseResponseDTO;
import com.walk.aroundyou.repository.BoardRepository;
import com.walk.aroundyou.repository.CourseRepository;
import com.walk.aroundyou.repository.CourseSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseService {

	// 페이징 처리에 필요한, 한 페이지에 조회되는 데이터 수
	private final static int SIZE_OF_PAGE = 12;
	private final CourseRepository courseRepository;
	private final BoardRepository boardRepository;
	
	/**
	 * [산책로상세조회페이지] id로 산책로 하나 조회
	 */
	public Course findById(long id) {
		courseRepository.updateViewCount(id);
		return courseRepository.findById(id)
				.orElseThrow(() 
						-> new IllegalArgumentException(
								"not found: " + id));
	}
	
	/**
	 * [산책로상세조회페이지] id로 산책로 하나 조회(좋아요, 언급, 댓글 수 포함 + 조회수 1 증가) 
	 */
	public CourseResponseDTO findByIdWithCounts(Long id) {
		// 조회수 1 증가
		courseRepository.updateViewCount(id);
		Course course = courseRepository.findById(id)
				.orElseThrow(() 
				-> new IllegalArgumentException(
						"Course id not found: " + id));

		int likeCnt = courseRepository.countCourseLikesByCourseId(id);
		int mentionCnt = courseRepository.countCourseMentionsByCourseId(id);
		int commentCnt = courseRepository.countCourseCommentsByCourseId(id);
		
		return new CourseResponseDTO(course, likeCnt, mentionCnt, commentCnt);
	}
	
	/**
	 * [산책로상세조회페이지] 산책로 관련 게시물 조회 - 최신순, 조회수, 좋아요 순 정렬 (연서님 작성)
	 */
    public Page<IBoardListResponse> findBoardAndCntByCourseId(Long id, int page, String sort) {
	      // 최신순, 조회수, 좋아요순으로 정렬
	      // HTML의 select option 태그의 value를 같은 이름으로 설정하기
	      Sort customSort;
	      if ("boardViewCount".equals(sort)) {
	         customSort = Sort.by(Direction.DESC, "boardViewCount");
	      } else if("likeCnt".equals(sort)) {
	         customSort = Sort.by(Direction.DESC, "likeCnt");
	      } else {
	         customSort = Sort.by(Direction.DESC, "boardId");
	      }
	      // public static PageRequest of(int pageNumber, int pageSize, Sort sort) 사용
	      return boardRepository.findBoardAndCntByCourseId(id, PageRequest.of(page, SIZE_OF_PAGE, customSort));
	}
	
	/**
	 * [산책로목록조회페이지] 모든 산책로 조회 메서드 : 페이징, 정렬 구현
	 * [메인페이지] 산책로 좋아요순 정렬 메서드
	 */
	public Page<ICourseResponseDTO> findAll(String sort, int page) {
		
		//** 정렬 설정 : 원하는 방식의 정렬 버튼을 누르면 요청파라미터로 넘어가서 정렬되도록 하는 코드.
		Sort customSort;

		// 상세코스거리에 null이 있으면 마치 0처럼 asc에서는 가장 위에, desc에서는 가장 아래에 보인다. 
		if("coursDetailLtCnASC".equals(sort)) { // 상세코스거리 오름차순   coursDetailLtCn
			customSort = Sort.by(Direction.ASC, "coursDetailLtCn");
		} else if("coursDetailLtCnDESC".equals(sort)) { // 상세코스거리 내림차순   coursDetailLtCn
			customSort = Sort.by(Direction.DESC, "coursDetailLtCn");
		} else if("likeCnt".equals(sort)) { // 좋아요 내림차순
			customSort = Sort.by(Direction.DESC, "likeCnt");
		} else if("coursViewCount".equals(sort)) { // 조회수 내림차순
			customSort = Sort.by(Direction.DESC, "coursViewCount");
		} else { // 산책로명 가나다순  "wlkCoursFlagNm", "wlkCoursNm"
			customSort = Sort.by(Direction.ASC, "wlkCoursFlagNm", "wlkCoursNm");
		}
		
		// 페이징 처리 : (페이지 번호, 한 페이지에서 보이는 목록 수(20), 정렬 설정) 
		PageRequest pageRequest = PageRequest.of(page, SIZE_OF_PAGE, customSort);
		
		Page<ICourseResponseDTO> coursePage = 
				courseRepository.findCoursesWithCounts(pageRequest);
		
		return coursePage;
	}
	
	/**
	 * [메인페이지] 산책로 좋아요순 정렬 메서드
	 */
		public Page<ICourseResponseDTO> findCoursesOrderByLikes() {
		
		//** 정렬 설정 : 원하는 방식의 정렬 버튼을 누르면 요청파라미터로 넘어가서 정렬되도록 하는 코드.
		Sort sort = Sort.by(Direction.DESC, "likeCnt");
		
		// 페이징 처리 : (페이지 번호, 한 페이지에서 보이는 목록 수(3), 정렬 설정) 
		PageRequest pageRequest = PageRequest.of(0, 9, sort);
		
		Page<ICourseResponseDTO> coursePage = 
				courseRepository.findCoursesWithCounts(pageRequest);
		
		return coursePage;
	}

	/**
	 * [산책로목록조회페이지] 조건에 따른 산책로 목록 조회 메서드
	 */
	public Page<ICourseResponseDTO> findAllByCondition(
			String region, String level, String distance, 
			String startTime, String endTime, 
			String searchTargetAttr, String searchKeyword, 
			String sort, int page
	){
		//** Specification : 검색 조건
		// -> 각 매개변수의 값 여부에 따라서 검색 조건 추가 되거나 않도록 만들었음.
		// -> 처음에는 검색 조건 없도록 초기화 (null)
		Specification<Course> spec = 
				(root, query, criteriaBuilder) -> null;
		
		//** 드롭박스 선택 검색조건 추가
		// Specification의 and() : 검색 조건을 더하는 메소드
		if (region != null && !("".equals(region)))
			spec = spec.and(
					CourseSpecifications.equalRegion(region));
		if (level != null && !("".equals(level)))
			spec = spec.and(
					CourseSpecifications.equalLevel(level));
		if (distance != null && !("".equals(distance)))
			spec = spec.and(
					CourseSpecifications.equalDistance(distance));
		
		// 드롭박스 선택 검색조건 중 소요시간(ex. '1시간 이내')의 경우
		// 매개변수로 넘어간 문자열 데이터를 "HH:mm:ss" 패턴의 timestamp 형으로 교체
		// 과정 중 필요한 예외 처리해줌. 
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			
			if (startTime != null && endTime != null) {
				Date parsedStartTime = dateFormat.parse(startTime);
				Date parsedEndTime = dateFormat.parse(endTime);
				spec = spec.and(
						CourseSpecifications.betweenTime(parsedStartTime, parsedEndTime));
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid timestamp format");
		}
		
		//** 검색 대상 및 검색 조건 추가
		// 검색키워드의 대상이 되는 컬럼을 뜻하는 searchTargetAttr가 각 키워드와 같다면,
		// total = 산책로명, 주요코스, 소개글 전체 대상으로 키워드와 같은 값 검색
		// title = 산책로명 대상으로 키워드와 같은 값 검색
		// total = 주요코스 대상으로 키워드와 같은 값 검색
		// total = 소개글 대상으로 키워드와 같은 값 검색
		
		if (searchTargetAttr != null) {
			switch(searchTargetAttr) {
			case "total" :
				spec = spec.and(
						CourseSpecifications.likeTotalKeyword(searchKeyword));
				break;
			case "title" :
				spec = spec.and(
						CourseSpecifications.likeTotalKeyword(searchKeyword));
				break;
			case "coursDc" : 
				spec = spec.and(
						CourseSpecifications.likeTotalKeyword(searchKeyword));
				break;
			case "aditDc" : 
				spec = spec.and(
						CourseSpecifications.likeTotalKeyword(searchKeyword));
				break;
			default :
				break;
			}
		}	
		
		//** 정렬 설정 : 원하는 방식의 정렬 버튼을 누르면 요청파라미터로 넘어가서 정렬되도록 하는 코드.
		Sort customSort;

		// 상세코스거리에 null이 있으면 마치 0처럼 asc에서는 가장 위에, desc에서는 가장 아래에 보인다. 
		if("coursDetailLtCnASC".equals(sort)) { // 상세코스거리 오름차순   coursDetailLtCn
			customSort = Sort.by(Direction.ASC, "coursDetailLtCn");
		} else if("coursDetailLtCnDESC".equals(sort)) { // 상세코스거리 내림차순   coursDetailLtCn
			customSort = Sort.by(Direction.DESC, "coursDetailLtCn");
		} else if("likeCnt".equals(sort)) { // 좋아요 내림차순
			customSort = Sort.by(Direction.DESC, "likeCnt");
		} else if("coursViewCount".equals(sort)) { // 조회수 내림차순
			customSort = Sort.by(Direction.DESC, "coursViewCount");
		} else { // 산책로명 가나다순  "wlkCoursFlagNm", "wlkCoursNm"
			customSort = Sort.by(Direction.ASC, "wlkCoursFlagNm", "wlkCoursNm");
		}
		
		// 페이징 처리 : (페이지 번호, 한 페이지에서 보이는 목록 수(20), 정렬 설정) 
		PageRequest pageRequest = PageRequest.of(page, SIZE_OF_PAGE, customSort);
		
		Page<ICourseResponseDTO> coursePage = 
				courseRepository.findCoursesWithCountsAndConditions(spec, pageRequest);
		
		return coursePage;
	}
	
	/**
	 * [관리자페이지] 산책로데이터 생성 메소드
	 * entity 객체에 생성된 게시글의 정보가 담기며, 산책로 객체 전체를 리턴
	 * Long 형으로 바꾸고, entity.getId()를 return하면 생성된 산책로의 id(PK)를 리턴한다.
	 */
	@Transactional
	public Course save(CourseRequestDTO request) {
		return courseRepository.save(request.toEntity());
	}
	
	/**
	 * [관리자페이지] 산책로데이터 수정 메소드
	 * 해당 매서드의 실행이 종료(commit)되면 JPA의 엔티티 매니저에 의해서
	 * 영속성 컨텍스트에 저장된 엔티티 객체의 값이 변경되어 update 쿼리가 자동 실행됨.  
	 */
	@Transactional
	public Course updateCourse (long id, CourseRequestDTO request) {
		Course course = courseRepository.findById(id)
				.orElseThrow(()-> new IllegalArgumentException("Course not found : " + id));
		
		course.update(request.getWlkCoursFlagNm(),
				request.getWlkCoursNm(),
				request.getCoursDc(),
				request.getSignguCn(),
				request.getCoursLevelNm(),
				request.getCoursLtCn(),
				request.getCoursDetailLtCn(),
				request.getAditDc(),
				request.getCoursTimeCn(),
				request.getToiletDc(),
				request.getCvntlNm(),
				request.getLnmAddr(),
				request.getCoursSpotLa(),
				request.getCoursSpotLo()
		);
		return course;
	}
	
	/**
	 * [관리자페이지] 산책로데이터 삭제 메소드
	 */
	public void deleteCourse(long id) {
		courseRepository.deleteById(id);
	}
	
	/**
	 * [메인페이지] 산책로 조회순 정렬 메소드
	 */
	public List<CourseResponseDTO> findAllOrderByViewCnt() {
		Sort sort = Sort.by(Direction.DESC, "coursViewCount");
		List<Course> courseList = courseRepository.findAll(sort);
		return courseList
				.stream()
				.map(CourseResponseDTO::new)
				.collect(Collectors.toList());
	}

}