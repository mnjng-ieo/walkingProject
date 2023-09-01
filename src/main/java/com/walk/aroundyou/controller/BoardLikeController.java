package com.walk.aroundyou.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.service.BoardLikeService;
import com.walk.aroundyou.service.UserService;

@Controller
@RequestMapping("/mypage")
public class BoardLikeController {

	@Autowired
	private BoardLikeService boardLikeService;
	
	@Autowired
	private UserService userservice;
	
	@GetMapping("/mylike")
	public String likedBoard(Model model, @RequestParam User userId, @RequestParam String user) {
		//userId는 클라이언트로부터 전달된 값
		// 이 값을 기반으로 Usre객체를 찾거나 생성하는 로직 필요
		
		// user 찾기
		Optional<User> users = userservice.findByUserId(user);
		
		// 사용자가 좋아요한 게시물 목록
		List<Long> likeBoard = boardLikeService.findLikedBoardByUser(userId);
				
		// 조회된 게시물 ID목록을 모델에 담아서 View로 전달
		 model.addAttribute(likeBoard);

		return "like"; 
		}		
				
}
