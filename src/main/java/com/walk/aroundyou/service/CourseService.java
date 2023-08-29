package com.walk.aroundyou.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.CourseRequestDto;
import com.walk.aroundyou.repository.CourseRepository;
import com.walk.aroundyou.repository.CourseSpecifications;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseService {

	private final CourseRepository courseRepository;
	
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
			@RequestParam(required = false) String region,
			@RequestParam(required = false) String level,
			@RequestParam(required = false) String distance, 
			@RequestParam(required = false) String sort  // 원래 없었음.
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
		
		//** 정렬 방식 : 산책로명 가나다순이 기본. 나머지 옵션은 버튼 누르면 바뀌도록 구현하고 싶다.
		//            ~ 조건 검색된 결과에 대한 정렬 후작업이라 파라미터로 넣지는 못 함. 어떻게 해야 할까?
			// 산책로명 가나다순으로 정렬
			//Sort sort = Sort.by(Direction.ASC, "wlkCoursFlagNm", "wlkCoursNm");
			
			// 경로상세거리 짧은 순으로 정렬
			//Sort sort = Sort.by(Direction.ASC, "coursDetailLtCn");
			
			// 경로상세거리 먼 순으로 정렬
			//Sort sort = Sort.by(Direction.DESC, "coursDetailLtCn");
	
		return courseRepository.findAll(spec);  // 원래 (spec, sort) 이런 식이었다.
	}
	
	/**
	 * [관리자페이지] 산책로데이터 생성 메소드
	 * entity 객체에 생성된 게시글의 정보가 담기며, 산책로 객체 전체를 리턴
	 * Long 형으로 바꾸고, entity.getId()를 return하면 생성된 산책로의 id(PK)를 리턴한다.
	 */
	@Transactional
	public Course save(CourseRequestDto request) {
		return courseRepository.save(request.toEntity());
	}
	
	/**
	 * [관리자페이지] 산책로데이터 수정 메소드
	 * 해당 매서드의 실행이 종료(commit)되면 JPA의 엔티티 매니저에 의해서
	 * 영속성 컨텍스트에 저장된 엔티티 객체의 값이 변경되어 update 쿼리가 자동 실행됨.  
	 */
	@Transactional
	public Course updateCourse (long id, CourseRequestDto request) {
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
}
