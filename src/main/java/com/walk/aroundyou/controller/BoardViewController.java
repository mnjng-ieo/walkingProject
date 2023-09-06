package com.walk.aroundyou.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.domainenum.BoardType;
import com.walk.aroundyou.dto.BoardRequest;
import com.walk.aroundyou.dto.IBoardDetailResponse;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.service.BoardService;
import com.walk.aroundyou.service.TagService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BoardViewController {
	
	
	@Autowired
	private TagService tagService;

	@Autowired
	private BoardService boardService;
	
	// 페이지네이션 사이즈
	private final static int PAGINATION_SIZE = 5;
	
	// 게시물 리스트
	@GetMapping("/board")
	public String getBoard(Optional<String> type, Optional<Integer> page, Model model) {
		// Page 객체 선언
		Page<IBoardListResponse> boardList;
		// page 크기 설정
		int paramPage = 0;
		if(page.isPresent()) {
			paramPage = page.get();
		}
		
		// type 설정
		if(type.isEmpty()) {
			boardList = boardService.findboardAllList(paramPage);
			model.addAttribute("boardList", boardList);
		} else {
			boardList = boardService.findboardAllListByType(type.get(), paramPage);
			model.addAttribute("boardList", boardList);
		}
		
		// pagination 설정
		int totalPages = boardList.getTotalPages();
		int pageStart = getPageStart(paramPage, totalPages);
		int pageEnd = pageStart + PAGINATION_SIZE - 1;
		model.addAttribute("currentPage", paramPage + 1);
		model.addAttribute("pageStart", pageStart);
		model.addAttribute("pageEnd", pageEnd);
		
		// 페이지네이션 설정
		
		return "boardList";
	}
	
	
	// 게시물 조회
	@GetMapping("/board/{id}")
	public String getBoardDetail(@PathVariable Long id, Model model) {
		// 게시글 내용 불러오기
		Optional<IBoardDetailResponse> board = boardService.findBoardDetail(id);
		model.addAttribute("board", board.get());
		
		// 해시태그 리스트 불러오기
		List<String> boardTagList = 
			tagService.findTagsByBoardId(id);
		model.addAttribute("boardTagList", boardTagList);
		
		return "boardDetail";
	}
	
	
	// 게시물 삭제
	@PostMapping("/board/{id}")
	public String getBoardDelete(@PathVariable Long id, Model model) {
		boardService.deleteById(id);
		return "redirect:/board";
	}
	
	// 게시물 작성 폼
	@GetMapping("/board-editor")
	public String getBoardForm() {
		return "boardForm";
	}
	
	// 게시물 작성
	@PostMapping("/board-editor")
	public String postBoardForm(String boardType, String boardTitle, String boardContent) {
		// 뷰에서 유저 데이터 불러오는 로직 대체
		BoardRequest form = BoardRequest.builder()
				.boardType(BoardType.valueOf(boardType))
				.boardTitle(boardTitle)
				.boardContent(boardContent)
				.userId("wayid10")
				.userNickname("가나다")
				.build();

		log.info(form.toString());
		if(boardService.save(form)) {
			log.info("게시물 저장 성공");
			return "redirect:/board";			
		} else {
			log.info("게시물 저장 실패");
			return "redirect:/board-editor";
		}	
	}
	
	
	// 게시물 수정 폼
	@GetMapping("/board-editor/{id}")
	public String getBoardFormById(@PathVariable Long id, Model model) {
		Optional<IBoardDetailResponse> board = boardService.findBoardDetail(id);
		model.addAttribute("board", board.get());
		return "boardFormById";
	}
	
	// 게시물 수정
	@PostMapping("/board-editor/{id}")
	public String postBoardFormById(String boardType, String boardTitle, String boardContent,@PathVariable Long id) {
		// 뷰에서 유저 데이터 불러오는 로직 대체
		BoardRequest form = BoardRequest.builder()
				.boardId(id)
				.boardType(BoardType.valueOf(boardType))
				.boardTitle(boardTitle)
				.boardContent(boardContent)
				.userId("wayid10")
				.userNickname("가나다")
				.build();

		log.info(form.toString());
		if(boardService.save(form)) {
			log.info("게시물 저장 성공");
			return "redirect:/board/"+id;			
		} else {
			log.info("게시물 저장 실패");
			return "redirect:/board-editor/"+id;
		}	
	}
	
	
	// 하나의 게시물에 포함된 해시태그 리스트 출력하기
		@GetMapping("/tagList/{boardId}")
		// @PathVariable 어노테이션을 사용하여 URL에서 추출한 boardId를 파라미터로 전달
		public String tagListInBoardContent(@PathVariable Long boardId, Model model) {
			log.info("/tagList/{boardId} 접근 .... ");
			// 존재하지 않는 boardId를 조회할때도 대비하기, 아직 구현하지 않음
			Optional<Board> boardContent = boardService.findById(boardId);
			List<String> boardTagList = 
				tagService.findTagsByBoardId(boardId);
			model.addAttribute("boardContent", boardContent.get().getBoardContent());
			model.addAttribute("boardTagList", boardTagList);
			return "boardTag";
		}

	
	// 검색하여 출력되는 목록 페이지 구현(좋아요 수, 댓글 수 포함)
	@GetMapping("/searchBoard/{tagContent}")
	public String searchBoardAndCnt(@PathVariable("tagContent") String tagContent, Model model) {
		Tag tagId = tagService.findIdByTagContent(tagContent);
		List<IBoardListResponse> tagBoardList = 
			tagService.findBoardAndCntByTagId(tagId.getTagId());
		model.addAttribute("tagBoardList", tagBoardList);
		return "searchBoardList";
	}
	
	// 페이지네이션 시작 페이지를 계산해주는 컨트롤러
	private int getPageStart(int currentPage, int totalPages) {
		log.info("currentPage = {}, totalPages = {}", currentPage, totalPages);
		int result = 1;
		if(totalPages < currentPage + (int)Math.ceil((double)PAGINATION_SIZE/2)) {
			log.info("if문 통과");
			result = totalPages - PAGINATION_SIZE + 1;
		}else if(currentPage > (int)Math.floor((double)PAGINATION_SIZE/2)) {
			result = currentPage - (int)Math.floor((double)PAGINATION_SIZE/2) + 1;
			log.info("else if문 통과");
		}
		
		return result;
	}
	
}
