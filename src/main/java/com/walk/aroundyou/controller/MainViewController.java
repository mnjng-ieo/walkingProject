package com.walk.aroundyou.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.repository.CourseRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainViewController {

	@Autowired
	CourseRepository courseRepo;
	
	@GetMapping("/")
	public String getMain() {
		return "main";
	}
	
	@PostMapping("/")
	public String postMain() {
		
		return "main";
	}
	// 지도 테스트용 임시 API
	@GetMapping("/map-test")
	public String getMapTest(Model model, Long id) {
		Optional<Course> course = courseRepo.findById(id);
		model.addAttribute("course", course.get());
		
		String courseFlag = course.get().getWlkCoursFlagNm();
		List<Course> courseNames = courseRepo.findCourseNamesByCourseFlagName(courseFlag);
		log.info(""+courseNames.size());
		model.addAttribute("courseNames", courseNames);
		return "mapTest";
	}

}
