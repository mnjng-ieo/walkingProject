package com.walk.aroundyou.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainViewController {

	@Autowired
	private BoardService boardService;
	
	// 페이지네이션 사이즈
	private final static int PAGINATION_SIZE = 5;

	@GetMapping("/")
	public String getMain() {
		return "main";
	}
	
	@PostMapping("/")
	public String postMain() {
		
		return "main";
	}
	
	@GetMapping("/board")
	public String getBoard(Optional<String> type, Optional<Integer> page, Model model) {
		// Page 객체 선언
		Page<IBoardListResponse> boardList;
		// page 크기 설정
		int paramPage = 0;
		if(page.isPresent()) {
			paramPage = page.get();
		}
		
		// type 설정
		if(type.isEmpty()) {
			boardList = boardService.findboardAllList(paramPage);
			model.addAttribute("boardList", boardList);
		} else {
			boardList = boardService.findboardAllListByType(type.get(), paramPage);
			model.addAttribute("boardList", boardList);
		}
		
		// pagination 설정
		int totalPages = boardList.getTotalPages();
		int pageStart = getPageStart(paramPage, totalPages);
		int pageEnd = pageStart + PAGINATION_SIZE - 1;
		model.addAttribute("currentPage", paramPage + 1);
		model.addAttribute("pageStart", pageStart);
		model.addAttribute("pageEnd", pageEnd);
		
		// 페이지네이션 설정
		
		return "boardList";
	}
	
	private int getPageStart(int currentPage, int totalPages) {
		log.info("currentPage = {}, totalPages = {}", currentPage, totalPages);
		int result = 1;
		if(totalPages < currentPage + (int)Math.floor(PAGINATION_SIZE/2)) {
			log.info("if문 통과");
			result = totalPages - PAGINATION_SIZE + 1;
		}else if(currentPage > (int)Math.floor(PAGINATION_SIZE/2)) {
			result = currentPage - (int)Math.floor(PAGINATION_SIZE/2) + 1;
			log.info("else if문 통과");
		}
		
		return result;
	}
}
