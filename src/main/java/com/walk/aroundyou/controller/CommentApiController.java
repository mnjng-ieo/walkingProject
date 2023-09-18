package com.walk.aroundyou.controller;

import java.sql.Timestamp;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.dto.AddCommentRequest;
import com.walk.aroundyou.dto.UpdateCommentRequest;
import com.walk.aroundyou.service.CommentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class CommentApiController {

	private final CommentService commentService;

	
/* comment 목록 조회 */
//	// board_id로 해당 게시물에 대한 comment 목록 조회 
//	@GetMapping("/board/comments/{boardId}")
//	public List<ICommentResponseDto> findByBoardId(@PathVariable(name = "boardId") Long boardId){
//		log.info("boardId : {}",boardId);	// 로그 확인 
//		return commentService.findAllByBoard(boardId);
//	}
//
//	// course_id로 해당 게시물에 대한 comment 목록 조회 
//	@GetMapping("/course/comments/{courseId}")
//	public List<ICommentResponseDto> findByCourseId(@PathVariable(name = "courseId") Long courseId){
//		log.info("boardId : {}",courseId);	// 로그 확인 
//		return commentService.findAllByBoard(courseId);
//	}
//	
	
	///////////////////////////////////////////////////////////////
	////// 당장 안쓰는것같아 모아놓은것
	////////////////////////////////////////////////////////////////
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
//	@DeleteMapping("/delete/comment/{commentId}")
//	public void deleteComment(@PathVariable(name = "commentId") Long commentId){
//		log.info("/delete/board/comment 컨트롤러 접근");
//		// comment_id로 조회된 comment_like_id 삭제 
//		//commentService.deleteCommentLikeByCommentId(commentId); 주석 처리
//		// comment_id로 조회된 comment_id 삭제 
//		commentService.deleteCommentByCommentId(commentId);
//	}
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
	
	
	//////////////////////////////////////////////////////////////////


	
	
	/// 9/12 코멘트 컨트롤러 수정

	// 댓글 삭제
	@DeleteMapping("/api/comment/{commentId}")
	public ResponseEntity<?> deleteComment(
			@PathVariable(name = "commentId") Long commentId
			, @AuthenticationPrincipal User user){
		int result = commentService.deleteCommentByCommentId(commentId,user.getUsername());
		if(result != 1) {
			throw new RuntimeException("댓글 삭제를 실패하였습니다.");
		}
		return ResponseEntity.ok().body(result);
	}
	
	// 게시판 댓글 등록
	@PostMapping("/api/comment/board/{boardId}")
	public ResponseEntity<?> createBoardComment(@PathVariable(name = "boardId") Long boardId
			, @RequestBody AddCommentRequest comment
			, @AuthenticationPrincipal User user){
		log.info("/api/comment/board/{boardId} 컨트롤러 접근");
		comment.setUserId(Member.builder().userId(user.getUsername()).build());
		comment.setBoardId(Board.builder().boardId(boardId).build());
		comment.setCommentCreatedDate(new Timestamp(System.currentTimeMillis()));
		
		int result = commentService.saveCommentAsBoard(comment);
		if(result != 1) {
			throw new RuntimeException("댓글 등록을 실패하였습니다.");
		}
		return ResponseEntity.ok().body(result);
	}
	// 댓글 수정
	@PutMapping("/api/comment/{commentId}")
	public ResponseEntity<?> postCommentOnBoard(
			@PathVariable(name = "commentId") Long commentId
			, @RequestBody AddCommentRequest commentDto
			, @AuthenticationPrincipal User user) {
		log.info("수정메소드");
		/// 실행오류나는중
		Comment comment = Comment.builder()
				.commentId(commentId)
				.commentContent(commentDto.getCommentContent())
				.userId(Member.builder().userId(user.getUsername()).build())
				.commentUpdatedDate(new Timestamp(System.currentTimeMillis()))
				.build();
		int result = commentService.updateCommentByCommentId(comment);		
		if(result != 1) {
			throw new RuntimeException("댓글 수정을 실패하였습니다.");
		}
		return ResponseEntity.ok().body(result);
	}
		
		
	// 산책로 댓글 등록
	@PostMapping("/api/comment/course/{courseId}")
	public ResponseEntity<?> createCourseComment(@PathVariable(name = "courseId") Long courseId
			, @RequestBody AddCommentRequest comment
			, @AuthenticationPrincipal User user){
		log.info("/api/comment/board/{boardId} 컨트롤러 접근");
		comment.setUserId(Member.builder().userId(user.getUsername()).build());
		comment.setCourseId(Course.builder().courseId(courseId).build());
		comment.setCommentCreatedDate(new Timestamp(System.currentTimeMillis()));
		
		int result = commentService.saveCommentAsCourse(comment);
		if(result != 1) {
			throw new RuntimeException("댓글 등록을 실패하였습니다.");
		}
		return ResponseEntity.ok().body(result);
	}
	
}
