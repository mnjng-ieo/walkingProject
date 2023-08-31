package com.walk.aroundyou.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.dto.BoardDetailResponse;
import com.walk.aroundyou.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BoardRestController {

	@Autowired
	private BoardService boardService;
	
	
	// 게시판 목록 출력
//	@GetMapping("/board ")
//	public Object boardAllList() {
//		return BoardService.findboardAllList(); 
//	}
	// 게시판 목록 출력
	@GetMapping("/board")
	public ResponseEntity<Object> boardAllList(Optional<String> type, Optional<Integer> page) {
		int paramPage = 0;
		if(page.isPresent()) {
			paramPage = page.get();
		}
		if(type.isEmpty()) {
			return ResponseEntity.ok().body(boardService.findboardAllList(paramPage));
		} else {
			return ResponseEntity.ok().body(boardService.findboardAllListByType(type.get(), paramPage));
		}
	}
	
//	@GetMapping("/board")
//	public Object boardAllList(String type, Optional<Integer> page) {
//		int paramPage = 0;
//		if(page.isPresent()) {
//			paramPage = page.get();
//		}
//		return boardService.findboardAllListByType(type, paramPage); 
//	}
	// 게시판 상세 출력
	@GetMapping("/board/{id}")
	public Object boardById(@PathVariable("id") Long id) {
		Optional<BoardDetailResponse> board = boardService.findBoardDetail(id);
		if(board.isEmpty()){
			return "없는 게시물입니다";
		} else {
			return board;
		}
	}
	
	
	//// 게시판 등록
//	@PostMapping("/board/editor")
//	public Object creatBoard(@RequestBody BoardRequest board) {
//		// 게시물 등록
//		// 
//	}
	// 등록된 board_id로 해시태그 이력 추가
	//// 게시판 수정
	// 게시물 수정
	// 해시태그 이력 전체 삭제
	// 해시태그 이력 추가
	//// 게시판 삭제
	// 댓글 좋아요 삭제
	// 댓글 삭제
	// 해시태그 이력 전체 삭제
	// 게시물 삭제
	
	
	
//	@GetMapping("/board/{id}")
//	public Object boardById(@PathVariable("id") Long id) {
//		Optional<BoardDetailResponse> board = boardService.findBoardAndCntById(id);
//		if(board.isEmpty()){
//			return "없는 게시물입니다";
//		} else {
//			return board;
//		}
//	}
//	
//	@GetMapping("/board/{id}")
//	public Object boardDetail() {
//		
//	}
}
