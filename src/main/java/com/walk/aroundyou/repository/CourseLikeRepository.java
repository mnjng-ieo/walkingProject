package com.walk.aroundyou.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.CourseLike;
import com.walk.aroundyou.domain.Member;

public interface CourseLikeRepository 
					extends JpaRepository<CourseLike, Long>{
	
	// user와 course 조합으로 CourseLike 테이블 찾기
	Optional<CourseLike> findByUserIdAndCourseId(Member user, Course course);
}
