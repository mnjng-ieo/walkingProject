package com.walk.aroundyou.util;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.ICourseResponseDTO;

import jakarta.persistence.criteria.Predicate;

// 검색 조건을 추상화하기 위해 사용하는 클래스 
// 쿼리문의 where 조건에 사용되도록 criteriaBuilder의 메소드를 이용한다.
public class CourseSpecifications{
	
	// Course의 필드에 해당하는 값과 파라미터의 값을 비교하는 쿼리문이 추가되도록 한다.
	// criteriaBuilder의 equal, lessThanOrEqualTo, greaterThanOrEqualTo 등 활용해보자
	
	public static Specification<Course> equalRegion(String region){
		return (root, query, criteriaBuilder) 
				-> criteriaBuilder.equal(root.get("signguCn"), region);
	}
	
	public static Specification<Course> equalLevel(String level){
		return (root, query, criteriaBuilder) 
				-> criteriaBuilder.equal(root.get("coursLevelNm"), level);
	}
	
	public static Specification<Course> equalDistance(String distance){
		return (root, query, criteriaBuilder) 
				-> criteriaBuilder.equal(root.get("coursLtCn"), distance);
	}
	
	public static Specification<Course> betweenTime(Date startTime, Date endTime){
		return (root, query, criteriaBuilder)
				-> criteriaBuilder.between(root.get("coursTimeCn"), startTime, endTime);
	}
	
	// 검색키워드가 특정 컬럼 값에 포함되는지 확인하는 메소드
	public static Specification<Course> likeAttribute(String attributeName, String keyword) {
		return (root, query, criteriaBuilder) -> {
			
			String formattedKeyword;
			
			// 검색 키워드의 공백 제거
			if(keyword == null) {
				formattedKeyword = "";
			} else {
				formattedKeyword = keyword.replace(" ", "");
			}
			// Predicate : 논리적인 조건 나타냄. sql의 where절 역할!
			Predicate predicate = criteriaBuilder.like(
					// 검색 대상이 되는 디비 데이터의 공백 제거
					// function() : 디비 함수 호출 역할
					// 순서대로, 1. 호출 함수 이름, 2. 반환 타입, 3. 대체 값 전달
					//        4. 대체될 before 문자열, 5. 대체될 after 문자열
					criteriaBuilder.function(
							"REPLACE", String.class, root.get(attributeName),
							criteriaBuilder.literal(" "),
							criteriaBuilder.literal("")),
					"%" + formattedKeyword + "%");
			return predicate;
		};
	}
	
	// 검색키워드의 대상이 전체(산책로명, 주요코스, 경로소개글)일 때 
	public static Specification<Course> likeTotalKeyword(String keyword){
		return (root, query, criteriaBuilder) -> {
			Predicate likeWlkCoursFlagNm = 
					likeAttribute("wlkCoursFlagNm", keyword)
					.toPredicate(root, query, criteriaBuilder);
			Predicate likeWlkCoursNm = 
					likeAttribute("wlkCoursNm", keyword)
					.toPredicate(root, query, criteriaBuilder);
			Predicate likeCoursDc = 
					likeAttribute("coursDc", keyword)
					.toPredicate(root, query, criteriaBuilder);
			Predicate likeAditDc = 
					likeAttribute("aditDc", keyword)
					.toPredicate(root, query, criteriaBuilder);
			// criteriaBuilder.or() : 조건들을 or 연산으로 함께 쓰고 싶을 때
			Predicate combinedPredicate = 
					criteriaBuilder.or(
							likeWlkCoursFlagNm, likeWlkCoursNm,
							likeCoursDc, likeAditDc);
			return combinedPredicate;
		};
	}
	
	// 검색키워드의 대상이 산책로명일 때
	public static Specification<Course> likeTitleKeyword(String keyword){
		return (root, query, criteriaBuilder) -> {
			Predicate likeWlkCoursFlagNm = 
					likeAttribute("wlkCoursFlagNm", keyword)
					.toPredicate(root, query, criteriaBuilder);
			Predicate likeWlkCoursNm = 
					likeAttribute("wlkCoursNm", keyword)
					.toPredicate(root, query, criteriaBuilder);
			Predicate combinedPredicate = 
					criteriaBuilder.or(likeWlkCoursFlagNm, likeWlkCoursNm);
			return combinedPredicate;
		};
	}
	
	// 검색키워드의 대상이 주요코스일 때 
	public static Specification<Course> likeCoursDcKeyword(String keyword){
		return likeAttribute("coursDc", keyword);
	}
	
	// 검색키워드의 대상이 경로소개글일 때 
	public static Specification<Course> likeAditDcKeyword(String keyword){
		return likeAttribute("aditDc", keyword);
	}

}