package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.dto.CourseResponseDTO;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.dto.ICourseResponseDTO;
import com.walk.aroundyou.dto.ITagResponse;
import com.walk.aroundyou.repository.TagRepository;
import com.walk.aroundyou.service.BoardService;
import com.walk.aroundyou.service.CourseService;
import com.walk.aroundyou.service.MainSearchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainSearchController {

	@Autowired
	private MainSearchService mainSearchService;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CourseService courseService;
	
	// 페이지네이션 사이즈(뷰에 보이는 페이지 수)
	private final static int PAGINATION_SIZE = 5;
		
	@GetMapping("/search")
	public String mainSearch(
			@RequestParam("keyword" ) String keyword,
			
			Model model) {
		// 검색 서비스로부터 검색 결과를 가져온다 
		// 1. 태그
		List<ITagResponse> tagResults = 
				mainSearchService.findTagByKeyword(keyword);

		// 2. 코스
		List<ICourseResponseDTO> courseResults = 
				mainSearchService.findCourseByKeyword(keyword);
		if (courseResults.size() > 5) { // 다섯개 이상이면 
		    courseResults = courseResults.subList(0, 5); // 다섯개 까지만 표시
		}

		// 3. 게시물
		List<IBoardListResponse> boardResults = 
				mainSearchService.findBoardByKeyword(keyword);
		if (boardResults.size() > 5) {
		    boardResults = boardResults.subList(0, 5);
		}
		
		// 전체 개수 구하기
		int totalCourseResults = mainSearchService.countCourseResults(keyword);
		int totalBoardResults = mainSearchService.countBoardResults(keyword);
		
        model.addAttribute("tagResults", tagResults);
        model.addAttribute("courseResults", courseResults);
        model.addAttribute("boardResults", boardResults);
        model.addAttribute("totalCourseResults", totalCourseResults);
        model.addAttribute("totalBoardResults", totalBoardResults);
        model.addAttribute("keyword", keyword);
		
		return "mainSearch";
	}

	// 검색 후 더보기의 게시물 목록 시도 중 
	// 검색하여 출력되는 목록 페이지 구현(좋아요 수, 댓글 수 포함)	
	
	// 메인 페이지 검색 결과에서 게시물 더보기 클릭하면 나오는 페이지(게시물 리스트)
	@GetMapping("/board/search/{keyword}")
	public String searchBoard(
			@PathVariable(name = "keyword",  required=false) String keyword,
			@RequestParam(value = "page", required=false, defaultValue="0") int currentPage,
			@RequestParam(value = "sort", required= false, defaultValue = "boardId") String sort,
			Model model) {
		// Page 객체 선언
		Page<IBoardListResponse> boardList = 
				boardService.findBoardAndCntByKeyword(keyword, currentPage, sort);
		log.info("출력이 되나요? : {}", boardList.isEmpty());
		// pagination 설정
		int totalPages = boardList.getTotalPages();
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
		model.addAttribute("boardList", boardList);
		model.addAttribute("keyword", keyword);
		
		return "searchBoardList";
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
