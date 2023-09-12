package com.walk.aroundyou.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardCourse;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.BoardRequest;
import com.walk.aroundyou.dto.IBoardDetailResponse;
import com.walk.aroundyou.service.BoardCourseService;
import com.walk.aroundyou.service.BoardService;
import com.walk.aroundyou.service.TagService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BoardRestController {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private TagService tagService;

	@Autowired
	private BoardCourseService boardCourseService;
	
	// 게시판 목록 출력
	@GetMapping("/api/board")
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
	
	// 게시판 상세 출력
	@GetMapping("/api/board/{id}")
	public Object boardById(@PathVariable("id") Long id) {
		Optional<IBoardDetailResponse> board = boardService.findBoardDetail(id);
		if(board.isEmpty()){
			return "없는 게시물입니다";
		} else {
			return board;
		}
	}
	

	@PutMapping("/api/board-editor")
	public Object creatBoard(@RequestBody BoardRequest board, @AuthenticationPrincipal User user) {
//		// 게시물 등록
		log.info("게시물 등록 시작");
		board.setUserId(user.getUsername());
		Optional<Long> result = boardService.save(board);
		
		if(result.isPresent()){
			log.info("저장 결과가 있어요");	
			Long boardId = result.get();
			// 해시태그 추가 처리
			// 등록된 board_id로 해시태그 이력 추가
			List<String> createTagList = tagService.createTagList(boardId);
	        log.info("createList : {}", createTagList);
	        // for문 사용하여 태그 하나씩 저장 
	        for (String tagContent : createTagList) {
	        	tagService.saveBoardTag(boardId, tagContent);
	        }
	        log.info("해시태그 추가 처리 완료");
	        // 산책로 추가 처리
	        boardCourseService.save(BoardCourse.builder()
	        		.boardId(Board.builder().boardId(boardId).build())
	        		.courseId(Course.builder().courseId(board.getCourseId()).build())
	        		.build());
	        log.info("산책로 추가 처리 완료");
			return "저장이 성공하였습니다";
		} else {
			log.info("저장 결과가 없어요");	
			return "저장이 실패하였습니다";
		}
	}
	//// 게시판 수정
	@PutMapping("/api/board-editor/{id}")
	public Object updateBoard(@PathVariable Long id, @RequestBody BoardRequest board, @AuthenticationPrincipal User user) {
		board.setBoardId(id);
		board.setUserId(user.getUsername());
		if(boardService.update(board)) {
			// 해시태그 수정 처리
			// board_id의 해시태그 이력 일괄 삭제
			tagService.deleteByBoardId(id);
	        log.info("해시태그 삭제 처리 완료");
			// 등록된 board_id로 해시태그 이력 추가
			tagService.deleteByBoardId(id);
			List<String> createTagList = tagService.createTagList(id);
	        log.info("createList : {}", createTagList);
	        // for문 사용하여 태그 하나씩 저장 
	        for (String tagContent : createTagList) {
	        	tagService.saveBoardTag(id, tagContent);
	        }
	        log.info("해시태그 추가 처리 완료");
	        // 산책로 삭제 처리
	        boardCourseService.deleteByBoardId(id);
	        // 산책로 추가 처리
	        boardCourseService.save(BoardCourse.builder()
	        		.boardId(Board.builder().boardId(id).build())
	        		.courseId(Course.builder().courseId(board.getCourseId()).build())
	        		.build());
	        log.info("산책로 추가 처리 완료");
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
	@DeleteMapping("/api/board-editor")
	public Object deleteBoard(@RequestBody BoardRequest board) {
		log.info("board의 id : {}", board.toUpdateEntity().getBoardId());
		if(boardService.deleteById(board.toUpdateEntity().getBoardId())){
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
