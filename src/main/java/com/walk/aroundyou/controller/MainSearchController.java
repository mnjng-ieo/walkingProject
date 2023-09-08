package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.dto.CourseResponseDTO;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.dto.ICourseResponse;
import com.walk.aroundyou.dto.ITagResponse;
import com.walk.aroundyou.repository.TagRepository;
import com.walk.aroundyou.service.MainSearchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainSearchController {

	@Autowired
	private MainSearchService mainSearchService;
		
	@GetMapping("/searchList")
	public String mainSearch(
			@RequestParam("keyword" ) String keyword, 
			Model model) {
		// 검색 서비스로부터 검색 결과를 가져온다 
		// 1. 태그
		List<ITagResponse> tagResults = 
				mainSearchService.findTagByKeyword(keyword);

		// 2. 코스
		List<ICourseResponse> courseResults = 
				mainSearchService.findCourseByKeyword(keyword);

		// 3. 게시물
		List<IBoardListResponse> boardResults =
				mainSearchService.findBoardByKeyword(keyword);
		
        model.addAttribute("tagResults", tagResults);
        model.addAttribute("courseResults", courseResults);
        model.addAttribute("boardResults", boardResults);
		
		return "mainSearch";
	}
}
