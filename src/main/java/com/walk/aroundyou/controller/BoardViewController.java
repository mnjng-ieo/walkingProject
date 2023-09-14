package com.walk.aroundyou.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.UploadImage;
import com.walk.aroundyou.dto.IBoardDetailResponse;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.dto.ICommentResponseDto;
import com.walk.aroundyou.service.BoardService;
import com.walk.aroundyou.service.CommentService;
import com.walk.aroundyou.service.CourseService;
import com.walk.aroundyou.service.TagService;
import com.walk.aroundyou.service.UploadImageService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BoardViewController {
	
	
	@Autowired
	private TagService tagService;

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CommentService commentService;	
	
	@Autowired
	private CourseService courseService;	
	
	@Autowired
	private UploadImageService uploadImageService;
	
	// 페이지네이션 사이즈
	private final static int PAGINATION_SIZE = 5;
	
	// 게시물 리스트(게시판 타입 선택 가능)
	@GetMapping("/board")
	public String getBoard(
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "type", required = false) String type, 
			@RequestParam(name = "sort", required = false) String sort,
			@RequestParam(value = "page", required=false, defaultValue="0") int page, 
			Model model) {
		
		// Page 객체 선언
		// 타입 선택 여부에 따라 실행되는 함수가 달라지기 때문에 객체만 선언
		Page<IBoardListResponse> boardList;
		
		// type 설정
		if(type == null) {
			boardList = boardService.findBoardAndCntByKeyword(keyword, page, sort);
			model.addAttribute("boardList", boardList);
		} else {
			boardList = boardService.findBoardAndCntByKeywordAndType(type, keyword, page, sort);
			model.addAttribute("boardList", boardList);
		}
		
		log.info("리스트가 비어있나요? : {}", boardList.isEmpty());
		log.info("키워드는? : {}", keyword);
		
		// pagination 설정
		int totalPages = boardList.getTotalPages();
		int pageStart = getPageStart(page, totalPages);
		int pageEnd = 
				(PAGINATION_SIZE < totalPages)? 
						pageStart + PAGINATION_SIZE - 1
						:totalPages;
		
		model.addAttribute("lastPage", totalPages);
		model.addAttribute("currentPage", page + 1);
		model.addAttribute("pageStart", pageStart);
		model.addAttribute("pageEnd", pageEnd);
		model.addAttribute("sort", sort);
		model.addAttribute("boardList", boardList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("type", type);
		
		// 이미지 경로 넘기기 - 0913 지수 작성
		// for 문을 돌려서 리스트 항목 각각의 이미지 경로를 얻어보자.
		List<List<String>> boardsImagesPaths = new ArrayList<>();
		for (IBoardListResponse boardResponseDTO : boardList.getContent()) {
			Board board = boardService.findById(boardResponseDTO.getBoardId()).get();
			List<UploadImage> uploadImages = uploadImageService.findByBoard(board);
			if (uploadImages != null && !uploadImages.isEmpty()) {
				List<String> boardImagePaths = 
						uploadImageService.findBoardFullPathsById(uploadImages);
				boardsImagesPaths.add(boardImagePaths);
			} // else ; 뷰에서 기본 이미지가 들어가면 된다.
		}
		// 모델에 이미지 경로 리스트의 리스트 추가
		model.addAttribute("boardsImagesPaths", boardsImagesPaths);
		
		return "boardList";
	}
	
	// 게시물 검색 결과 화면
	@GetMapping("/board/search")
	public String getSearchBoard(
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "boardType", defaultValue = "ALL") String type,
	        @RequestParam(name = "searchType", defaultValue = "boardTitleAndContent") String searchType,
			@RequestParam(name = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue="0") int page, 
			Model model) {
		
		// Page 객체 선언
		Page<IBoardListResponse> boardList;
				
		// type 설정
		if(type != null && !type.equals("ALL")) {
			boardList = boardService.findBoardAndCntByKeywordAndTypeAndSearchType(type, searchType, keyword, page, sort);
		} else {
			boardList = boardService.findBoardAndCntByKeywordAndSearchType(keyword, searchType, page, sort);
		}
		model.addAttribute("boardList", boardList);
		
		log.info("리스트가 비어있나요? : {}", boardList.isEmpty());
		log.info("키워드는? : {}", keyword);
		
		// pagination 설정
		int totalPages = boardList.getTotalPages();
		int pageStart = getPageStart(page, totalPages);
		int pageEnd = 
				(PAGINATION_SIZE < totalPages)? 
						pageStart + PAGINATION_SIZE - 1
						:totalPages;
		
		model.addAttribute("lastPage", totalPages);
		model.addAttribute("currentPage", page + 1);
		model.addAttribute("pageStart", pageStart);
		model.addAttribute("pageEnd", pageEnd);
		model.addAttribute("sort", sort);
		model.addAttribute("boardList", boardList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("selectedBoardType", type);
        model.addAttribute("selectedSearchType", searchType);
		
     // 이미지 경로 넘기기 - 0913 지수 작성
	// for 문을 돌려서 리스트 항목 각각의 이미지 경로를 얻어보자.
	List<List<String>> boardsImagesPaths = new ArrayList<>();
	for (IBoardListResponse boardResponseDTO : boardList.getContent()) {
		Board board = boardService.findById(boardResponseDTO.getBoardId()).get();
		List<UploadImage> uploadImages = uploadImageService.findByBoard(board);
		if (uploadImages != null && !uploadImages.isEmpty()) {
			List<String> boardImagePaths = 
					uploadImageService.findBoardFullPathsById(uploadImages);
			boardsImagesPaths.add(boardImagePaths);
		} // else ; 뷰에서 기본 이미지가 들어가면 된다.
	}
	// 모델에 이미지 경로 리스트의 리스트 추가
	model.addAttribute("boardsImagesPaths", boardsImagesPaths);
        
		return "boardConditionList";
	}
	
	// 게시물 조회(09/09 - 연서 수정)
	@GetMapping("/board/{id}")
	public String getBoardDetail(@PathVariable Long id, Model model) {
		// 게시글 내용 불러오기
		Optional<IBoardDetailResponse> board = boardService.findBoardDetail(id);
		log.info("board가 있나요? : {}", board.isPresent());
		model.addAttribute("board", board.get());
		
		// 해시태그 리스트 불러오기(값 없을 때 체크)
		List<String> boardTagList = 
			tagService.findTagsByBoardId(id);
		if(!boardTagList.isEmpty()) {		
			log.info("tag 값이 있음");			
			model.addAttribute("boardTagList", boardTagList);
		}
		
		// 댓글 리스트 불러오기(값 없을 때 체크)
		List<ICommentResponseDto> comments = commentService.findByBoardId(id);
		
		if(!comments.isEmpty()) {			
			log.info("comment 값이 있음");	
			model.addAttribute("comments", comments);
		}
		
		// 산책로 정보 불러오기
		Optional<Course> course = courseService.findByBoardId(id);
		if(course.isPresent()) {			
			log.info("course 값이 있음");
			model.addAttribute("course", course.get());
		} else {
			log.info("course 값이 없음");	
		}
		
		// 이미지 경로 넘기기 - 0913 지수 작성
		// for 문을 돌려서 리스트 항목 각각의 이미지 경로를 얻어보자.
		Board existedBoard = boardService.findById(id).get();
		List<UploadImage> uploadImages = uploadImageService.findByBoard(existedBoard);
		List<String> ImagePaths = new ArrayList<>();
		if(uploadImages != null && !uploadImages.isEmpty()) {
			ImagePaths = uploadImageService.findBoardFullPathsById(uploadImages);
			model.addAttribute("imagePaths", ImagePaths);
		}
		
		log.info("컨트롤러 끝");
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
	public String getBoardForm(Model model, Optional<Long> course) {
		// 산책로 지역 선택 항목 가져오기
		List<String> allSignguCn = courseService.findAllSignguCn();
		model.addAttribute("allSignguCn", allSignguCn);
		if(course.isPresent()) {
			Course resultCourse = courseService.findById(course.get());
			model.addAttribute("courseId", resultCourse.getCourseId());
			model.addAttribute("wlkCoursFlagNm", resultCourse.getWlkCoursFlagNm());
			model.addAttribute("wlkCoursNm", resultCourse.getWlkCoursNm());
		}
		
		return "boardForm";
	}
	
	// 게시물 수정 폼
	@GetMapping("/board-editor/{id}")
	public String getBoardFormById(@PathVariable Long id, Model model) {
		Optional<IBoardDetailResponse> board = boardService.findBoardDetail(id);
		model.addAttribute("board", board.get());
		
		// 이미지 경로 넘기기 - 0913 지수 작성
		// for 문을 돌려서 리스트 항목 각각의 이미지 경로를 얻어보자.
		Board existedBoard = boardService.findById(id).get();
		List<UploadImage> uploadImages = uploadImageService.findByBoard(existedBoard);
		List<String> ImagePaths = new ArrayList<>();
		if(uploadImages != null && !uploadImages.isEmpty()) {
			ImagePaths = uploadImageService.findBoardFullPathsById(uploadImages);
			model.addAttribute("imagePaths", ImagePaths);
		}
		
		List<String> allSignguCn = courseService.findAllSignguCn();
		model.addAttribute("allSignguCn", allSignguCn);
		Optional<Course> course = courseService.findByBoardId(id);
		if(course.isPresent()) {
			log.info("course 값이 있음");
			Course resultCourse = course.get();
			model.addAttribute("courseId", resultCourse.getCourseId());
			model.addAttribute("wlkCoursFlagNm", resultCourse.getWlkCoursFlagNm());
			model.addAttribute("wlkCoursNm", resultCourse.getWlkCoursNm());
		} else {
			log.info("course 값이 없음");	
		}
		if(course.isPresent()) {
		}
		return "boardFormById";
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
	
	
	// 댓글 삭제
	@DeleteMapping("/api/comment/{commentId}")
	@ResponseBody
	public void deleteComment(@PathVariable(name = "commentId") Long commentId){
		log.info("/delete/board/comment 컨트롤러 접근");
		// comment_id로 조회된 comment_like_id 삭제 
		//commentService.deleteCommentLikeByCommentId(commentId); 주석 처리
		// comment_id로 조회된 comment_id 삭제 
		commentService.deleteCommentByCommentId(commentId);
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