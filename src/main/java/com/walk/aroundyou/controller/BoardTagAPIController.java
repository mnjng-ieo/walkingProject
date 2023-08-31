package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.dto.AddTagRequest;
import com.walk.aroundyou.service.TagService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardTagAPIController {

	private final TagService tagService;
	// 1. 새로운 태그 tag 테이블에 추가하기
	@PostMapping("/api/board/tag")
	public void addTagContent(@RequestBody AddTagRequest request) {
		tagService.save(request);
	}
	
	// 2. 기존 태그 tag 테이블에서 삭제하기(기본메서드 사용)
	@DeleteMapping("/api/board/tag/{tagId}")
	public ResponseEntity<Void> deleteTagContent(@PathVariable Long tagId) {
		tagService.delete(tagId);
		return ResponseEntity.ok().build();
	}
	
	// 3. 게시물에 작성된 해시태그 조회
	@GetMapping("/api/board/tag/{boardId}")
	public List<String> findTagsByBoardId(@PathVariable(name = "boardId") Long boardId) {
		return tagService.findTagsByBoardId(boardId);
	}
	
	
}
