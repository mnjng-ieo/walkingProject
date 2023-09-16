package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.dto.BoardRequest;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.service.BoardService;
import com.walk.aroundyou.service.TagService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class BoardTagAPIController {

	@Autowired
	private TagService tagService;
	
	@Autowired
	private BoardService boardService;
	
	/// TagRepository 사용하여 출력 확인하기
	// 1. 기존 태그 tag 테이블에서 삭제하기(기본메서드 사용)
	@DeleteMapping("/api/board/tag/{tagId}")
	public ResponseEntity<Void> deleteByTagId(@PathVariable Long tagId) {
		tagService.deleteByTagId(tagId);
		return ResponseEntity.ok().build();
	}
	
	// 2. 새로운 태그 tag 테이블에 추가하기(tag 테이블에만 추가하는 버전)
	@PostMapping("/api/board/tag")
	public void saveTag(@RequestBody String tagContent) {
		tagService.saveTag(tagContent); 
	}

	
	// 3. 게시물에 작성된 해시태그 조회
	@GetMapping("/api/board/tag/{boardId}")
	// 컴파일 후에 @PathVariable 에 있는 변수를 못찾는 오류 발생 시 @PathVariable 에 name 을 명시
	// Name for argument type [java.lang.String] not available ~
	public List<String> findTagsByBoardId(@PathVariable(name = "boardId") Long boardId) {
		return tagService.findTagsByBoardId(boardId);
	}
	
	// 4. 게시글에 저장된 해시태그 파싱하기
	// 4-1. 해시태그 리스트 확인
	// 새로운 게시글이 저장되었을 때 작성된 해시태그 뽑아 tag, board_tag에 저장하기위한 용도
	@GetMapping("/api/board/hashtag/{boardId}")
	public List<String> createTagList(@PathVariable(name = "boardId") Long boardId) {
		return tagService.createTagList(boardId);
	}
	
	// 4-2. 파싱된 해시태그 리스트 저장하기
	// 게시물 저장하면 해시태그 파싱하여 태그도 저장하기
//	@PostMapping("/board/post")
//	public ResponseEntity<Board> createBoard(@RequestBody BoardRequest board) {
//		// 저장된 게시물의 boardId 가져오기
//        Long boardId = boardService.save(board);
//        log.info("boardId : {}", boardId);
//        // 게시물에서 해시태그 파싱하여 리스트로 저장
//        List<String> createTagList = tagService.createTagList(boardId);
//        log.info("createList : {}", createTagList);
//        // for문 사용하여 태그 하나씩 저장 
//        for (String tagContent : createTagList) {
//        	tagService.saveBoardTag(boardId, tagContent);
//        }
//        return ResponseEntity.ok().build();
//    } 

	// 5. 동일한 tag_id를 가진 board_tag_id의 tag_content 출력
	// 메인화면에 지금 핫한 해시태그에 출력될 내용
	@GetMapping("api/main")
	public List<String> findTagsByBoardTagId() {
		return tagService.findTagsByBoardTagId();
	}	
	
	// 6. 해시태그 클릭 시 게시물 목록 페이지 출력하는데 좋아요 수, 댓글 수를 포함한 게시물 출력(연습용)
//	@GetMapping("/api/findBoardAndCntByTagId/{tagId}")
//	public Page<IBoardListResponse> findBoardAndCntByTagId(
//			@PathVariable("tagId") Tag tagId,
//			@RequestParam(value="page", defaultValue="1") int page) {
//		return tagService.findBoardAndCntByTagId(tagId, page);
//		
//	}
	
	/*------------------------------------------------------*/
	/// BoardTagRepository 사용하여 출력 확인하기
	
	// 1. 게시물 삭제 시 board_tag 테이블에서 삭제하기(기본 메서드 사용)
	@DeleteMapping("/api/board/boardTag/{boardId}")
	public ResponseEntity<Void> deleteByBoardId(@PathVariable Long boardId) {
		tagService.deleteByBoardId(boardId);
		return ResponseEntity.ok().build();
	}
	
	// 2. 새로운 게시물의 태그 board_tag 테이블에 추가하기
	@PostMapping("/api/board/boardTag")
	public void saveBoardTag(@RequestParam Long boardId, @RequestParam String tagContent) {
		/// 수정하는 경우에 추가
		/// tagService.deleteByBoardId(boardTag.getBoardTagId());
		log.info("/api/board/boardTag Post 매핑 들어옴....");
		log.info("boardTag : {}, tagContent : {}", boardId, tagContent);
		tagService.saveBoardTag(boardId, tagContent);
	}
	
	// 3. 수정한 게시물의 태그 board_tag 테이블에 추가하기(삭제 후 추가)
	@PatchMapping("/api/board/boardTag/{boardId}")
	public void updateBoardTag(@PathVariable Long boardId, @RequestBody BoardRequest board) {
		board.setBoardId(boardId);
		boardService.update(board);
		tagService.deleteByBoardId(boardId);
		List<String> createTagList = tagService.createTagList(boardId);
        // for문 사용하여 태그 하나씩 저장 
        for (String tagContent : createTagList) {
        	tagService.saveBoardTag(boardId, tagContent);
        }
	}
	
}
