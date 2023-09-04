package com.walk.aroundyou.controller;



import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import com.walk.aroundyou.dto.BoardLikeDTO;
import com.walk.aroundyou.service.BoardLikeService;




@RestController
@Slf4j
// @ResponseBody 와 @Controller 의 기능을 내장함

public class BoardLikeController {
	

//	@Autowired
//	BoardLikeService boardLikeService;
//	
//	@Autowired
//	BoardLikeDTO boardLikeDTO;


	
	
	
		// Controller/RestController 에서 
		// GetMapping일 시, @RequestBody 를 통해서는 파라미터를 받을 수 없다.
		// @RequestParam, @PathVariable, @Param을 통해 받는다. 
		// @Param은 주로 데이터베이스에 여러 개의 파라미터를 넘겨줄 때 사용
	
	
	
		// 특정 게시물(boardId)에 대한 
			// "좋아요 갯수" & "(좋아요 누른)회원들 목록" 둘 다 보여주기
//		@GetMapping("/boards/{boardId}/show")
//		public void showByBoardId(Board boardId){
//			
//			boardLikeService.countUserIdByBoardId(boardId);
//			boardLikeService.findUserIdByBoardId(boardId);
//			
//			return ;
//		}
		
		
		private String userId;

		@PostMapping("/boards/{boardId}")
		public ResponseEntity<BoardLikeDTO> addBoardLike(
				@PathVariable long boardId, 
				Principal principal, 
				@RequestBody  BoardLikeDTO boardLikeDTO, BoardLikeService boardLikeService) throws Exception {
			
			boardLikeDTO.setUserId(userId);
			boardLikeDTO.setBoardId(boardId);
			
			
			
			boolean isLiked = boardLikeService.toggleLike(userId, boardId);
			
			if(isLiked) {
				// 이미 좋아요 상태라면 좋아요 취소하기
				boardLikeService.deleteLike(boardLikeDTO);
				log.info("DELETE_LIKE");
				
			} else {
				// 좋아요를 누르지 않은 상태라면 좋아요 하기
				boardLikeService.insertLike(boardLikeDTO);
				log.info("INSERT_LIKE");
			}
			return ResponseEntity.ok()
					.body(boardLikeDTO);
			
			
	

	}

}
