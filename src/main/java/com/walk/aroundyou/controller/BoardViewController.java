package com.walk.aroundyou.controller;

import java.sql.Timestamp;
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

import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domainenum.BoardType;
import com.walk.aroundyou.dto.BoardRequest;
import com.walk.aroundyou.dto.IBoardDetailResponse;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.dto.ICommentResponseDto;
import com.walk.aroundyou.service.BoardService;
import com.walk.aroundyou.service.CommentService;
import com.walk.aroundyou.service.CourseService;
import com.walk.aroundyou.service.TagService;

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
	
	/////////////// 댓글
	// 댓글 
	
	// 댓글 수정
	@PostMapping("/board-comment/{id}")
	public String postCommentOnBoard(
			@PathVariable(name = "id") Long id
			, String boardId
			, String commentId
			, String userId
			, String commentContent) {
		Comment comment = Comment.builder()
				.commentId(Long.parseLong(commentId))
				.commentContent(commentContent)
				.userId(Member.builder().userId(userId).build())
				.commentUpdatedDate(new Timestamp(System.currentTimeMillis()))
				.build();
		commentService.updateBoardCommentByCommentId(comment);
		
		return "redirect:/board/"+id;
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
