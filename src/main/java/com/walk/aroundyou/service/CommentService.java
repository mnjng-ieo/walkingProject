package com.walk.aroundyou.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.AddCommentRequest;
import com.walk.aroundyou.dto.ICommentResponseDto;
import com.walk.aroundyou.dto.UpdateCommentRequest;
import com.walk.aroundyou.repository.CommentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {
	
	@Autowired
	private final CommentRepository commentRepo;

	
/* comment 목록 조회 */
	// board_id로 조회된 게시물에 대한 comment 목록 조회 
	public List<ICommentResponseDto> findAllByBoard(Long boardId) {
		return commentRepo.findByBoardId(boardId);
	}	
	
	// course_id로 조회된 게시물에 대한 comment 목록 조회 
	public List<ICommentResponseDto> findAllByCourse(Long courseId){
		return commentRepo.findByCourseId(courseId);
	}
	
	
	
/* comment 추가 */
	// BOARD 타입 코멘트 글 추가 메서드 
	public void saveCommentAsBoard(AddCommentRequest commentReqDto) {
		log.info("/save/board/comment 서비스 접근");
		commentRepo.saveCommentAsBoard(commentReqDto);
		log.info("/save/board/comment 리포지토리에 저장 성공");
	}

	// COURSE 타입 코멘트 글 추가 메서드 
	public void saveCommentAsCourse(AddCommentRequest commentReqDto) {
		commentRepo.saveCommentAsCourse(commentReqDto);
	}
	
	
	
/* comment 삭제 */
	// 1) comment_id로 comment_like 엔티티 먼저 삭제 
	public void deleteCommentLikeByCommentId(long commentId) {
		commentRepo.deleteCommentByCommentLikeId(commentId);
	}
	
	// 2) comment_id로 comment 엔티티 삭제 
	public void deleteCommentByCommentId(long commentId) {
		log.info("/delete/board/comment 서비스 접근");
		commentRepo.deleteCommentByCommentId(commentId);
	}
	
	
	
/* comment 수정 */
	@Transactional
	public void update(Long commentId, UpdateCommentRequest updateReq) {
		commentRepo.updateCommentContent(commentId,updateReq);
	}

	
/* 해당 산책로(commentType = COURSE)에 관한 comment 개수 조회 메서드 */		
	public long countCourseCommentByCourseId(Course courseId) {
		log.info("/course/{courseId}/countcomment 서비스 접근");
		return commentRepo.countCourseCommentByCourseId(courseId);
	}

	
	// 
	public List<ICommentResponseDto> findByBoardId(Long boardId) {
	
		return commentRepo.findByBoardId(boardId);
	}

	public void updateBoardCommentByCommentId(Comment comment) {
		commentRepo.updateBoardCommentByCommentId(comment);
	}
	

/* */
}
