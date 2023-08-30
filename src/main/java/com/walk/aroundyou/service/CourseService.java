package com.walk.aroundyou.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.CourseRequestDTO;
import com.walk.aroundyou.repository.CourseRepository;
import com.walk.aroundyou.repository.CourseSpecifications;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseService {

	private final CourseRepository courseRepository;
	
	/**
	 * [산책로상세조회페이지] id로 산책로 하나 조회
	 */
	public Course findById(long id) {
		return courseRepository.findById(id)
				.orElseThrow(() 
						-> new IllegalArgumentException(
								"not found: " + id));
	}
	
	/**
	 * [산책로목록조회페이지] 모든 산책로 조회 메서드
	 */
	public List<Course> findAll() {
		// 산책로명 가나다순으로 정렬
		Sort sort = Sort.by(Direction.ASC, "wlkCoursFlagNm", "wlkCoursNm");
		return courseRepository.findAll(sort);
	}
	// ↳ 어차피 동적으로 작성되면 findAll 메소드 하나로도 될 것 같다.
	//   일단 ajax로 경로 설정하기 전에는 findAll() 호출을 통해 '/courses'로는 전체 조회가 바로 보이도록 했다.
	
	/**
	 * [산책로목록조회페이지] 조건에 따른 조회 메서드
	 */
	public List<Course> findAllByCondition(
			String region, String level, String distance, 
			String startTime, String endTime, 
			String total, String title, String coursDc, 
			String aditDc, String sort
	){
		Specification<Course> spec = 
				(root, query, criteriaBuilder) -> null;
		
		if (region != null)
			spec = spec.and(
					CourseSpecifications.equalRegion(region));
		if (level != null)
			spec = spec.and(
					CourseSpecifications.equalLevel(level));
		if (distance != null)
			spec = spec.and(
					CourseSpecifications.equalDistance(distance));
		
		try {
			// 매개변수로 넘어간 문자열 데이터를 "HH:mm:ss" 패턴의 timestamp 형으로 교체
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
		
		if (total != null)
			spec = spec.and(
					CourseSpecifications.likeTotalKeyword(total));
		if (title != null)
			spec = spec.and(
					CourseSpecifications.likeTitleKeyword(title));
		if (coursDc != null)
			spec = spec.and(
					CourseSpecifications.likeCoursDcKeyword(coursDc));
		if (aditDc != null)
			spec = spec.and(
					CourseSpecifications.likeAditDcKeyword(aditDc));
		
		// 원하는 방식의 정렬 버튼을 누르면 요청파라미터로 넘어가서 정렬되도록 하는 코드.
		Sort customSort;

		// 상세코스거리에 null이 있으면 마치 0처럼 asc에서는 가장 위에, desc에서는 가장 아래에 보인다. 
		if("coursDetailLtCnASC".equals(sort)) { // 상세코스거리 오름차순
			customSort = Sort.by(Direction.ASC, "coursDetailLtCn");
		} else if("coursDetailLtCnDESC".equals(sort)) { // 상세코스거리 내림차순
			customSort = Sort.by(Direction.DESC, "coursDetailLtCn");
		} else { // 산책로명 가나다순
			customSort = Sort.by(Direction.ASC, "wlkCoursFlagNm", "wlkCoursNm");
		}
	
		return courseRepository.findAll(spec, customSort);
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
}
