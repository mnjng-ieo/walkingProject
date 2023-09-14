package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.AddCommentRequest;
import com.walk.aroundyou.dto.ICommentResponseDto;
import com.walk.aroundyou.service.CommentService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j	
public class CommentViewController {
	
	@Autowired
	private CommentService commentService;
	
	// 페이지네이션 사이즈
	private final static int PAGINATION_SIZE = 5;
	
/* 게시물(commentType=BOARD)에 대한 코멘트 리스트 출력 */
	@GetMapping("/board/commentList/{id}")
	public String getCommentOnBoard(@PathVariable(name = "id") Long id, Model model,
			@RequestParam(value = "page", required=false, defaultValue="0") int page) {
		List<ICommentResponseDto> comments = commentService.findByBoardId(id);
		model.addAttribute("comments", comments);
		// pagination 설정
//		int totalPages = comments.getTotalPages();
//		int pageStart = getPageStart(page, 4);
//		int pageEnd = (PAGINATION_SIZE < 4)? 
//						pageStart + PAGINATION_SIZE - 1
//						:4;
//		model.addAttribute("currentPage", page + 1);
//		model.addAttribute("pageStart", pageStart);
//		model.addAttribute("pageEnd", pageEnd);
		return "commentOnBoard";
	}
	
//	@PostMapping("/board/commentList/{id}")
//	public String postCommentOnBoard(
//			@PathVariable(name = "id") Long id
//			, String boardId
//			, String commentId
//			, String userId
//			, String commentContent) {
//		Comment comment = Comment.builder()
//				.commentId(Long.parseLong(commentId))
//				.commentContent(commentContent)
//				.userId(User.builder().userId(userId).build())
//				.commentUpdatedDate(new Timestamp(System.currentTimeMillis()))
//				.build();
//		commentService.updateBoardCommentByCommentId(comment);
//		
//		return "redirect:/board/"+id;
//	}
	
	
/* 상세페이지(commentType=COURSE)에 대한 코멘트 리스트 출력 */
//	@GetMapping("/course/commentList/{courseId}")
//	public String getCommentOnCourse(@PathVariable(name = "courseId") Long courseId, Model model) {
//		List<ICommentResponseDto> comments = commentService.findAllByCourse(courseId);
//		model.addAttribute("comments", comments);
//		
//		return "commentOnCourse";
//	}
	
	
	
/* BOARD - 코멘트 작성하는 입력 폼 */	
	@GetMapping("/board/addComment/{boardId}")
	public String addCommentOnBoard(@PathVariable(name = "boardId") Long boardId, Model model) {
		
		AddCommentRequest request = new AddCommentRequest();
		model.addAttribute("addComment" ,request.toCourseEntity());
		
		return "addCommentOnBoard";
	}

	
/* COURSE - 코멘트 작성하는 입력 폼 */	
	@GetMapping("/course/addComment/{courseId}")
	public String addCommentOnCourse(@PathVariable(name = "courseId") Long courseId, Model model) {
		
		AddCommentRequest request = new AddCommentRequest();
		model.addAttribute("addComment" ,request.toCourseEntity());
		
		return "addCommentOnCourse";
	}
	
	
/* 해당 산책로(commentType = COURSE)에 관한 comment 개수 출력 */
	@GetMapping("/course/commentCnt/{courseId}")
	public String countComment(@PathVariable(name = "courseId") long courseId, Model model) {
		// 서비스의 메서드 매개변수 : Course 타입이므로, 객체 생성시킴
		// ( 이 방식이 코드의 중복으로 가독성이 떨어지긴 함.. )
		Course course = new Course();
		course.setCourseId(courseId);

		long commentCnt = commentService.countCourseCommentByCourseId(course);
		
		model.addAttribute("commentCnt", commentCnt);
		
		return "commentCnt";
	}
	
	// 페이지네이션 시작 페이지를 계산해주는 메소드
	private int getPageStart(int currentPage, int totalPages) {
		log.info("currentPage = {}, totalPages = {}", currentPage, totalPages);
		int result = 1;
		if(Math.max(totalPages, PAGINATION_SIZE) < currentPage + (int)Math.ceil((double)PAGINATION_SIZE/2)) {
			log.info("if문 통과");
			result = totalPages - PAGINATION_SIZE + 1;
		}else if(currentPage > (int)Math.floor((double)PAGINATION_SIZE/2)) {
			result = currentPage - (int)Math.floor((double)PAGINATION_SIZE/2) + 1;
			log.info("else if문 통과");
		}
		
		return result;
	}
}