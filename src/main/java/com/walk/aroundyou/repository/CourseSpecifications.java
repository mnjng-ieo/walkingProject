package com.walk.aroundyou.repository;

import org.springframework.data.jpa.domain.Specification;

import com.walk.aroundyou.domain.Course;

public class CourseSpecifications{
	
	// Course의 필드에 해당하는 값과 파라미터의 값을 비교하는 쿼리문이 추가되도록 한다.
	// startTime과 endTime은 아직 추가하지 않았음.
	// criteriaBuilder의 lessThanOrEqualTo, greaterThanOrEqualTo 등 활용해보자
	
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
	
}
