package com.walk.aroundyou.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.walk.aroundyou.dto.IBoardDetailResponse;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.repository.BoardRepository;
import com.walk.aroundyou.repository.TagRepository;
import com.walk.aroundyou.service.BoardService;
import com.walk.aroundyou.service.TagService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class TagViewController {
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private TagRepository tagRepository;
	
	@Autowired 
	private BoardService boardService;
	
	// 페이지네이션 사이즈(뷰에 보이는 페이지 수)
	private final static int PAGINATION_SIZE = 10;
		
	
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
	
	// 게시판 페이지 검색 뷰 구현
	@GetMapping("/search/boardCondition")
	public String boardCondition() {
		return "boardSearchCondition";
	}

	
	// 검색하여 출력되는 목록 페이지 구현(좋아요 수, 댓글 수 포함)
	@GetMapping("/tag/search/{tagContent}")
	public String searchBoardAndCnt(
				@PathVariable(name="tagContent") String tagContent,
				@RequestParam(value = "page", required=false, defaultValue="0") int currentPage,
				@RequestParam(value = "sort", required= false, defaultValue = "boardId") String sort,
				Model model) {
		// 검색된 tagContent가 가진 tagId 조회하기
		Tag tagId = tagRepository.findIdByTagContent(tagContent);
		// 조회된 게시물 목록
		Page<IBoardListResponse> tagBoardList = 
			tagService.findBoardAndCntByTagId(tagId, currentPage, sort);
		// pagination 설정
		int totalPages = tagBoardList.getTotalPages();
		int pageStart = getPageStart(currentPage, totalPages);
		int pageEnd = 
				(PAGINATION_SIZE < totalPages)? 
						pageStart + PAGINATION_SIZE - 1
						:totalPages;
		model.addAttribute("lastPage", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("pageStart", pageStart);
		model.addAttribute("pageEnd", pageEnd);
		model.addAttribute("sort", sort);
		// 선택된 태그가 포함된 게시물 리스트
		model.addAttribute("tagBoardList", tagBoardList);
		// 선택된 태그를 사용하여 정렬에 사용
		model.addAttribute("tagContent", tagContent);
		return "searchTagBoardList";
	}
	
	// pagination의 시작 숫자 얻는 메소드
	private int getPageStart(int currentPage, int totalPages) {
		int result = 1;
		if(totalPages < currentPage + (int)Math.floor(PAGINATION_SIZE/2)) {
			// 시작페이지의 최소값은 1!
			result = Math.max(1, totalPages - PAGINATION_SIZE + 1);
		} else if (currentPage > (int)Math.floor(PAGINATION_SIZE/2)) {
			result = currentPage - (int)Math.floor(PAGINATION_SIZE/2) + 1;
		}
		return result;
	}
	
}
