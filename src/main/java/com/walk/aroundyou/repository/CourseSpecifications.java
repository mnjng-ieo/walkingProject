package com.walk.aroundyou.repository;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.walk.aroundyou.domain.Course;

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
	
	public static Specification<Course> likeTotalKeyword(String keyword){
		return (root, query, criteriaBuilder)
				-> criteriaBuilder.or(
			criteriaBuilder.like(root.get("wlkCoursFlagNm"), "%" + keyword + "%"),
			criteriaBuilder.like(root.get("wlkCoursNm"), "%" + keyword + "%"),
			criteriaBuilder.like(root.get("coursDc"), "%" + keyword + "%"),
		    criteriaBuilder.like(root.get("aditDc"), "%" + keyword + "%")
		);
	}
	
	public static Specification<Course> likeTitleKeyword(String keyword){
		return (root, query, criteriaBuilder)
				-> criteriaBuilder.or(
			criteriaBuilder.like(root.get("wlkCoursFlagNm"), "%" + keyword + "%"),
			criteriaBuilder.like(root.get("wlkCoursNm"), "%" + keyword + "%")
		);
	}
	
	public static Specification<Course> likeCoursDcKeyword(String keyword){
		return (root, query, criteriaBuilder)
				-> criteriaBuilder.like(root.get("coursDc"), "%" + keyword + "%");
	}
	
	public static Specification<Course> likeAditDcKeyword(String keyword){
		return (root, query, criteriaBuilder)
				-> criteriaBuilder.like(root.get("aditDc"), "%" + keyword + "%");
	}
}
