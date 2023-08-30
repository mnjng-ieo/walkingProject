package com.walk.aroundyou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.service.BoardService;

@RestController
public class BoardRestController {

	@Autowired
	private BoardService BoardService;
	
	
	// 게시판 목록 출력
//	@GetMapping("/board ")
//	public Object boardAllList() {
//		return BoardService.findboardAllList(); 
//	}
	// 게시판 목록 출력
	@GetMapping("/board")
	public Object boardAllList(int page, int size) {
		return BoardService.findboardAllList(page, size); 
	}
	@GetMapping("/board")
	public Object boardAllList() {
		return BoardService.findboardAllList(1, 20); 
	}
//	
//	@GetMapping("/board/{id}")
//	public Object boardDetail() {
//		
//	}
}
