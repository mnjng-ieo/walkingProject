package com.walk.aroundyou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.walk.aroundyou.service.BoardCourseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BoardCourseController {

	@Autowired
	private BoardCourseService boardCourseService;
	
	
}