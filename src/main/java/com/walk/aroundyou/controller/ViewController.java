package com.walk.aroundyou.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.repository.BoardRepository;
import com.walk.aroundyou.service.TagService;

@Controller
public class ViewController {
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@GetMapping("/")
	public String getMain() {
		return "main";
	}
	
	// 가장 많이 사용된 태그 메인화면에 출력하기
	@GetMapping("/hotTag")
	public String getMainHotTag(Model model) {
		List<String> tagList = 
			tagService.findTagsByBoardTagId();
		model.addAttribute("tagList", tagList);
		return "maintag";
	}
	
	// 하나의 게시물에 해당하는 해시태그 리스트 출력하기
	@GetMapping("/tagList/{boardId}")
	// @PathVariable 어노테이션을 사용하여 URL에서 추출한 boardId를 파라미터로 전달
	public String tagListInBoardContent(@PathVariable Long boardId ,Model model) {
		// 존재하지 않는 boardId를 조회할때도 대비하기, 아직 구현하지 않음
		Optional<Board> boardContent = boardRepository.findById(boardId);
		List<String> tagList = 
			tagService.findTagsByBoardId(boardId);
		model.addAttribute("boardContent", boardContent.get().getBoardContent());
		model.addAttribute("tagList", tagList);
		return "boardtag";
	}
	

}
