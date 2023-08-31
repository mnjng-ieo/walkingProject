package com.walk.aroundyou.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.CourseLike;
import com.walk.aroundyou.domain.User;

public interface CourseLikeRepository 
					extends JpaRepository<CourseLike, Long>{
	
	Optional<CourseLike> findByUserAndCourse(User user, Course course);
}
