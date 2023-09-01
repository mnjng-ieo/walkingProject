package com.walk.aroundyou.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.AddCommentRequest;
import com.walk.aroundyou.dto.ICommentResponseDto;
import com.walk.aroundyou.dto.UpdateCommentRequest;
import com.walk.aroundyou.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class CommentApiController {

	private final CommentService commentService;

	
/* comment 목록 조회 */
	// board_id로 해당 게시물에 대한 comment 목록 조회 
	@GetMapping("/board/comments/{boardId}")
	public List<ICommentResponseDto> findByBoardId(@PathVariable(name = "boardId") Long boardId){
		log.info("boardId : {}",boardId);	// 로그 확인 
		return commentService.findAllByBoard(boardId);
	}

	// course_id로 해당 게시물에 대한 comment 목록 조회 
	@GetMapping("/course/comments/{courseId}")
	public List<ICommentResponseDto> findByCourseId(@PathVariable(name = "courseId") Long courseId){
		log.info("boardId : {}",courseId);	// 로그 확인 
		return commentService.findAllByBoard(courseId);
	}
	
	
/* comment 작성(추가)  */
	// commentType = BOARD인 comment 추가 
	@PostMapping("/save/board/comment")
	public void saveCommentAsBoard(@RequestBody AddCommentRequest commentReqDto){
		log.info("/save/board/comment 컨트롤러 접근");
		//commentService.saveCommentAsBoard(commentReqDto.toBoardEntity()); 
		commentService.saveCommentAsBoard(commentReqDto);
	}
	// commentType = COURSE인 comment 추가 
	@PostMapping("/save/course/comment")
	public void saveCommentAsCourse(@RequestBody AddCommentRequest commentReqDto){
		commentService.saveCommentAsCourse(commentReqDto); 
	}
	
	
/* comment 삭제 */
	// 컨트롤러에서 commentLike와 comment 삭제하는 서비스를 각각 호출 
	@DeleteMapping("/delete/comment/{commentId}")
	public void deleteComment(@PathVariable(name = "commentId") Long commentId){
		log.info("/delete/board/comment 컨트롤러 접근");
		// comment_id로 조회된 comment_like_id 삭제 
		commentService.deleteCommentLikeByCommentId(commentId);
		// comment_id로 조회된 comment_id 삭제 
		commentService.deleteCommentByCommentId(commentId);
	}

	
/* comment 수정 */
	@PutMapping("/update/comment/{commentId}")
	public void updateComment(
				@PathVariable(name = "commentId") Long commentId, 
				@RequestBody UpdateCommentRequest updateReq){
		commentService.update(commentId, updateReq);	
	}
	
	
/* 해당 산책로(commentType = COURSE)에 관한 comment 개수 조회 메서드 */	
	@GetMapping("/course/{courseId}/countcomment")
	public long countCourseCommentByCourseId(@PathVariable(name = "courseId") Long courseId) {
		log.info("/course/{courseId}/countcomment 컨트롤러 접근");
		// 매개변수인 Long 타입을 객체로 변환 
		Course course = new Course();
		course.setCourseId(courseId);
		
		return commentService.countCourseCommentByCourseId(course);
	}
	
}
