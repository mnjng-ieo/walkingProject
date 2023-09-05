package com.walk.aroundyou.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.dto.BoardRequest;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.repository.BoardRepository;
import com.walk.aroundyou.repository.TagRepository;
import com.walk.aroundyou.service.BoardService;
import com.walk.aroundyou.service.TagService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ViewController {
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private TagRepository tagRepository;
		
	
	@GetMapping("/")
	public String getMain() {
		return "main";
	}
	
	// 가장 많이 사용된 태그 메인화면에 출력하기
	@GetMapping("/hotTag")
	public String getMainHotTag(Model model) {
		List<String> hotTagList = 
			tagService.findTagsByBoardTagId();
		model.addAttribute("hotTagList", hotTagList);
		return "hotTag";
	}
	
	// 하나의 게시물에 포함된 해시태그 리스트 출력하기
	@GetMapping("/tagList/{boardId}")
	// @PathVariable 어노테이션을 사용하여 URL에서 추출한 boardId를 파라미터로 전달
	public String tagListInBoardContent(@PathVariable Long boardId, Model model) {
		log.info("/tagList/{boardId} 접근 .... ");
		// 존재하지 않는 boardId를 조회할때도 대비하기, 아직 구현하지 않음
		Optional<Board> boardContent = boardRepository.findById(boardId);
		List<String> boardTagList = 
			tagService.findTagsByBoardId(boardId);
		model.addAttribute("boardContent", boardContent.get().getBoardContent());
		model.addAttribute("boardTagList", boardTagList);
		return "boardTag";
	}
		
	// 하나의 해시태그가 가진 게시물 리스트 출력
	@GetMapping("/tagBoardList/{tagContent}")
	// @PathVariable 어노테이션을 사용하여 URL에서 추출한 boardId를 파라미터로 전달
	public String boardListIntagContent(@PathVariable("tagContent") String tagContent, Model model) {
		log.info("tagContent = {}", tagContent);
		// 존재하지 않는 tagContent를 조회할때도 대비하기, 아직 구현하지 않음
		List<Board> tagBoardList = 
			tagService.findBoardByTag(tagContent);
		model.addAttribute("tagBoardList", tagBoardList);
		return "tagBoardList"; // 카드 뷰(사용 안함)
	}
	
	// 게시판 페이지 검색 뷰 구현
	@GetMapping("/search/boardCondition")
	public String boardCondition() {
		return "boardSearchCondition";
	}
	
	// 해시태그 클릭하여 출력되는 목록 페이지 구현(미사용 - 좋아요 수, 댓글 수 없음)
//	@GetMapping("/searchBoard/{tagContent}")
//	public String searchBoard(@PathVariable("tagContent") String tagContent, Model model) {
//		List<Board> tagBoardList = 
//			tagService.findBoardByTag(tagContent);
//			model.addAttribute("tagBoardList", tagBoardList);
//		return "searchBoardList";
//	}
	
	// 검색하여 출력되는 목록 페이지 구현(좋아요 수, 댓글 수 포함)
	@GetMapping("/searchBoard/{tagContent}")
	public String searchBoardAndCnt(@PathVariable("tagContent") String tagContent, Model model) {
		Tag tagId = tagRepository.findIdByTagContent(tagContent);
		List<IBoardListResponse> tagBoardList = 
			tagService.findBoardAndCntByTagId(tagId);
		model.addAttribute("tagBoardList", tagBoardList);
		return "searchBoardList";
	}
}
