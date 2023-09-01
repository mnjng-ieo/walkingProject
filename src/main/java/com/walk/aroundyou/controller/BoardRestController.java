package com.walk.aroundyou.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.dto.BoardDetailResponse;
import com.walk.aroundyou.dto.BoardRequest;
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
	//// userId, userNickname, boardType, boardTitle, boardContent 입력
	@PostMapping("/board/editor")
	public Object creatBoard(@RequestBody BoardRequest board) {
//		// 게시물 등록
		if(boardService.save(board)){
			return "저장이 성공하였습니다";
		} else {
			return "저장이 실패하였습니다";
		}
	}
	// 등록된 board_id로 해시태그 이력 추가
	//// 게시판 수정
	@PatchMapping("/board/editor")
	public Object updateBoard(@RequestBody BoardRequest board) {
		if(boardService.update(board)) {
			return "수정이 성공하였습니다";
		} else {
			return "수정이 실패하였습니다";
		}
	}
	// 게시물 수정
	// 해시태그 이력 전체 삭제
	// 해시태그 이력 추가
	
	//// 게시판 삭제
	//// 편의상 임시적으로 게시물 생성에 만든 json을 사용하기 위해 데이터형을 사용
	//// 나중에 Long으로 변경해도 됨
	@DeleteMapping("/board/editor")
	public Object deleteBoard(@RequestBody BoardRequest board) {
		log.info("board의 id : {}", board.toEntity().getBoardId());
		if(boardService.deleteById(board.toEntity().getBoardId())){
			return "삭제가 성공하였습니다";
		} else {
			return "삭제가 실패하였습니다";
		}
		
	}
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
