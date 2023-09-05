package com.walk.aroundyou.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.dto.CommentLikeRequestDto;
import com.walk.aroundyou.service.CommentLikeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment/{commentId}")
public class CommentLikeApiController {
	
	private final CommentLikeService commentLikeService;
	
	@PostMapping
	public ResponseEntity<CommentLikeRequestDto> AddCourseLike(
			@PathVariable long commentId,
			Principal principal,
			@RequestBody CommentLikeRequestDto request) throws Exception {
		
		request.setCommentId(commentId);
		//String userId = principal.getName();
		String userId = "wayid1";	// 테스트용 
		//request.setUserId(principal.getName());
		request.setUserId(userId);
		
		// 조회한 좋아요 상태를 확인 
		boolean isLiked = commentLikeService.isCommentLiked(userId, commentId);
		
		if(isLiked) {
			// 좋아요 상태라면 좋아요 취소하기 
			commentLikeService.deleteCommentLike(request);
			log.info("DELETE CommentLike");
		} else {
			// 좋아요 아닌 상태라면 좋아요 하기 
			commentLikeService.insertCommentLike(request);
			log.info("INSERT CommentLike");
		}
		
		return ResponseEntity.ok()
						.body(request);
	}
			
}
