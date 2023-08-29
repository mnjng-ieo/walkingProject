package com.walk.aroundyou.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.repository.BoardCourseRepository;

@Service
public class BoardCourseService {
	
	@Autowired
    private BoardCourseRepository boardCourseRepository;

	public List<Board> findCourse(Course courseId){
		return boardCourseRepository.findByCourseId(courseId);
		
	}
	
}
