package com.walk.aroundyou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.BoardCourse;
import com.walk.aroundyou.repository.BoardCourseRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardCourseService {
	@Autowired
	BoardCourseRepository boardCourseRepository;

	public void save(BoardCourse boardCourse) {
		boardCourseRepository.save(boardCourse);
	}

	public void deleteByBoardId(Long id) {
		boardCourseRepository.deleteByBoardId(id);
	}
	
	
}
