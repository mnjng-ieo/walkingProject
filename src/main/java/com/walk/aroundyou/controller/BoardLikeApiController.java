package com.walk.aroundyou.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardLike;
import com.walk.aroundyou.dto.BoardLikeDTO;
import com.walk.aroundyou.service.BoardLikeService;

import lombok.extern.slf4j.Slf4j;




@RestController
@Slf4j
// @ResponseBody 와 @Controller 의 기능을 내장함

public class BoardLikeApiController {
	

	@Autowired
	BoardLikeService boardLikeService;
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
		
	

		@PostMapping("/boards/liketest")
		public ResponseEntity<BoardLikeDTO> addBoardLike(
				@RequestBody  BoardLikeDTO boardLikeDTO) throws Exception {
			
			String userId = boardLikeDTO.getUserId();
			Long boardId = boardLikeDTO.getBoardId();
			
			String result = "";
			
			boolean isLiked = boardLikeService.toggleLike(userId, boardId);
			
			if(isLiked) {
				// 이미 좋아요 상태라면 좋아요 취소하기
				boardLikeService.deleteBoardLike(boardLikeDTO);
				log.info("DELETE_LIKE");
				result = "좋아요를 취소했습니다";
				
			} else {
				// 좋아요를 누르지 않은 상태라면 좋아요 하기
				boardLikeService.insertBoardLike(boardLikeDTO);
				log.info("INSERT_LIKE");
				result = "좋아요를 추가했습니다";
			}
			return ResponseEntity.ok()
					.body(boardLikeDTO);
			

	}
		
		
		
		
		
		@GetMapping("/boards-delete/{boardId}")
		public void deleteByBoardId(@PathVariable Long boardId) throws Exception {
			
			boardLikeService.deleteBoardByBoardId(boardId);
			log.info("boardId로 게시물 삭제 성공");
		}
		

}
