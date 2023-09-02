package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.service.TagService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class BoardTagAPIController {

	private final TagService tagService;
	
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
	
	
	// 4. 게시글에 저장된 해시태그 파싱하기(실패)

	// 5. 동일한 tag_id를 가진 board_tag_id의 tag_content 출력
	// 메인화면에 지금 핫한 해시태그에 출력될 내용
	@GetMapping("api/main")
	public List<String> findTagsByBoardTagId() {
		return tagService.findTagsByBoardTagId();
	}	
	
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
	@PatchMapping("/api/board/boardTag")
	public void updateBoardTag(@RequestParam Long boardId, @RequestParam String tagContent) {
		tagService.deleteByBoardId(boardId);
		tagService.saveBoardTag(boardId, tagContent);
	}
	
}
