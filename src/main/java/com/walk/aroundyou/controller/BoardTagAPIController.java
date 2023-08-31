package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.domain.BoardTag;
import com.walk.aroundyou.dto.AddTagRequest;
import com.walk.aroundyou.service.BoardTagService;
import com.walk.aroundyou.service.TagService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardTagAPIController {

	private final TagService tagService;
	private final BoardTagService boardTagService;
	
	/// TagRepository 사용하여 출력 확인하기
	// 1. 기존 태그 tag 테이블에서 삭제하기(기본메서드 사용)
	@DeleteMapping("/api/board/tag/{tagId}")
	public ResponseEntity<Void> deleteByTagId(@PathVariable Long tagId) {
		tagService.deleteByTagId(tagId);
		return ResponseEntity.ok().build();
	}
	
	// 2. 새로운 태그 tag 테이블에 추가하기
	@PostMapping("/api/board/tag")
	public void saveTag(@RequestBody AddTagRequest request) {
		tagService.saveTag(request);
	}
	
	// 3. 게시물에 작성된 해시태그 조회
	@GetMapping("/api/board/tag/{boardId}")
	public List<String> findTagsByBoardId(@PathVariable(name = "boardId") Long boardId) {
		return tagService.findTagsByBoardId(boardId);
	}
	
	/*------------------------------------------------------*/
	/// BoardTagRepository 사용하여 출력 확인하기
	
	// 1. 게시물 삭제 시 board_tag 테이블에서 삭제하기(기본 메서드 사용)
	@DeleteMapping("/api/board/boardTag/{boardTagId}")
	public ResponseEntity<Void> deleteByBoardTagId(@PathVariable Long boardTagId) {
		boardTagService.deleteByBoardTagId(boardTagId);
		return ResponseEntity.ok().build();
	}
	
	// 2. 새로운 태그 board_tag 테이블에 추가하기
	@PostMapping("/api/board/boardTag")
	public void saveBoardTag(@RequestBody BoardTag boardTag) {
		boardTagService.saveBoardTag(boardTag);
	}
	
}
