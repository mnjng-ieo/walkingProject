package com.walk.aroundyou.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.walk.aroundyou.domainenum.BoardType;
import com.walk.aroundyou.dto.BoardRequest;
import com.walk.aroundyou.dto.IBoardDetailResponse;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainViewController {

	@Autowired
	private BoardService boardService;
	
	// 페이지네이션 사이즈
	private final static int PAGINATION_SIZE = 5;

	@GetMapping("/")
	public String getMain() {
		return "main";
	}
	
	@PostMapping("/")
	public String postMain() {
		
		return "main";
	}
	
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
		Optional<IBoardDetailResponse> board = boardService.findBoardDetail(id);
		model.addAttribute("board", board.get());
		return "boardDetail";
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
	
	// 게시물 삭제
	@PostMapping("/board/{id}")
	public String getBoardDelete(@PathVariable Long id, Model model) {
		boardService.deleteById(id);
		return "redirect:/board";
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
