package com.walk.aroundyou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.AddCommentRequest;
import com.walk.aroundyou.dto.ICommentResponseDto;
import com.walk.aroundyou.service.CommentService;

@Controller
public class CommentViewController {
	
	@Autowired
	private CommentService commentService;
	
	
	@GetMapping("/")
	public String getMain() {
		return "main";
	}
	
	
/* 게시물(commentType=BOARD)에 대한 코멘트 리스트 출력 */
	@GetMapping("/board/commentList/{boardId}")
	public String getCommentOnBoard(@PathVariable(name = "boardId") Long boardId, Model model) {
		List<ICommentResponseDto> comments = commentService.findByBoardId(boardId);
		model.addAttribute("comments", comments);
		 
		return "commentOnBoard";
	}
	
	
/* 상세페이지(commentType=COURSE)에 대한 코멘트 리스트 출력 */
	@GetMapping("/course/commentList/{courseId}")
	public String getCommentOnCourse(@PathVariable(name = "courseId") Long courseId, Model model) {
		List<ICommentResponseDto> comments = commentService.findAllByCourse(courseId);
		model.addAttribute("comments", comments);
		
		return "commentOnCourse";
	}
	
	
	
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
	
}
