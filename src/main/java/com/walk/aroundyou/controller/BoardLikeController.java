package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.dto.BoardLikeDTO;
import com.walk.aroundyou.service.BoardLikeService;



@RestController
// @ResponseBody 와 @Controller 의 기능을 내장함

public class BoardLikeController {
	

	@Autowired
	BoardLikeService boardLikeService;
	
	@Autowired
	BoardLikeDTO boardLikeDTO;

	private Board boardId;
	
	
		// Controller/RestController 에서 
		// GetMapping일 시, @RequestBody 를 통해서는 파라미터를 받을 수 없다.
		// @RequestParam, @PathVariable, @Param을 통해 받는다. 
		// @Param은 주로 데이터베이스에 여러 개의 파라미터를 넘겨줄 때 사용
	
	
	
		// 특정 게시물(boardId)에 대한 
			// "좋아요 갯수" & "(좋아요 누른)회원들 목록" 둘 다 보여주기
		@GetMapping("/")
		public void showByBoardId(){
			
			boardLikeService.countUserIdByBoardId(boardId);
			boardLikeService.findUserIdByBoardId(boardId);
			
			return ;
		}
		
		

	}


