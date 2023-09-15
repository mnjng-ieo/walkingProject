package com.walk.aroundyou.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.dto.AddCommentRequest;
import com.walk.aroundyou.dto.ICommentResponseDto;
import com.walk.aroundyou.dto.UpdateCommentRequest;
import com.walk.aroundyou.repository.CommentRepository;
import com.walk.aroundyou.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {
	
	@Autowired
	private final CommentRepository commentRepo;
	
	@Autowired
	private final UserRepository userRepository;

	private final static int SIZE_OF_PAGE = 10;
	
/* comment 목록 조회 */
//	// board_id로 조회된 게시물에 대한 comment 목록 조회 
//	public List<ICommentResponseDto> findAllByBoard(Long boardId) {
//		return commentRepo.findByBoardId(boardId);
//	}	
//	
//	// course_id로 조회된 게시물에 대한 comment 목록 조회 
//	public List<ICommentResponseDto> findAllByCourse(Long courseId){
//		return commentRepo.findByCourseId(courseId);
//	}
//	
	///////////////////////////////////////////////////////////////
	////// 당장 안쓰는것같아 모아놓은것
	////////////////////////////////////////////////////////////////
	// 1) comment_id로 comment_like 엔티티 먼저 삭제 
	public void deleteCommentLikeByCommentId(long commentId) {
		commentRepo.deleteCommentByCommentLikeId(commentId);
	}
	/* 해당 산책로(commentType = COURSE)에 관한 comment 개수 조회 메서드 */		
	public long countCourseCommentByCourseId(Course courseId) {
		log.info("/course/{courseId}/countcomment 서비스 접근");
		return commentRepo.countCourseCommentByCourseId(courseId);
	}
	/* comment 수정 */
	@Transactional
	public void update(Long commentId, UpdateCommentRequest updateReq) {
		commentRepo.updateCommentContent(commentId,updateReq);
	}
	//////////////////////////////////////////////////////////////////

	
	
	
	//// 특정 게시판/산책로에서 댓글 불러오기
	// 특정 게시판의 댓글 불러오기
	public List<ICommentResponseDto> findByBoardId(Long boardId) {
	
		return commentRepo.findByBoardId(boardId);
	}
	// 특정 산책로의 댓글 불러오기
	public List<ICommentResponseDto> findByCourseId(Long courseId) {
		
		return commentRepo.findByCourseId(courseId);
	}
	
	/* comment 추가 */
	// BOARD 타입 코멘트 글 추가 메서드 
	public int saveCommentAsBoard(AddCommentRequest comment) {
		Optional<Member> member = userRepository.findById(comment.getUserId().getUserId());
		log.info("/save/board/comment 서비스 접근");
		if(member.isPresent()) {
			log.info("사용자를 찾았어요");			
			comment.setUserNickname(member.get().getUserNickname());
		}else {
			log.info("사용자를 못 찾았어요");	
		}
		int result = commentRepo.saveCommentAsBoard(comment);
		log.info("/save/board/comment 리포지토리에 저장 성공");
		return result;
	}

	// COURSE 타입 코멘트 글 추가 메서드 
	public int saveCommentAsCourse(AddCommentRequest comment) {
		Optional<Member> member = userRepository.findById(comment.getUserId().getUserId());
		log.info("/save/board/comment 서비스 접근");
		if(member.isPresent()) {
			log.info("사용자를 찾았어요");			
			comment.setUserNickname(member.get().getUserNickname());
		}else {
			log.info("사용자를 못 찾았어요");	
		}
		int result = commentRepo.saveCommentAsCourse(comment);
		return result;
	}
	
	
	
/* comment 삭제 */
	// comment_id로 comment 엔티티 삭제 
	public int deleteCommentByCommentId(long commentId, String userId) {
		log.info("/delete/board/comment 서비스 접근");
		try {
			Optional<Comment> checkComment = commentRepo.findById(commentId);
			if(checkComment.isEmpty() || !checkComment.get().getUserId().getUserId().equals(userId)) {
				throw new Exception("잘못된 접근");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return commentRepo.deleteCommentByCommentId(commentId);
	}
	
	
	// 댓글 수정
	public int updateCommentByCommentId(Comment comment){
		try {
			Optional<Comment> checkComment = commentRepo.findById(comment.getCommentId());
			if(checkComment.isEmpty() || !checkComment.get().getUserId().getUserId().equals(comment.getUserId().getUserId())) {
				throw new Exception("잘못된 접근");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commentRepo.updateCommentByCommentId(comment);
	}
	
	
	////// 좋아요 기능 추가
	public int getCommentLikeCntByCommentId(Long commentId) {
		int commentLikeCnt = commentRepo.countCommentLikesByCommentId(commentId);
		
		return commentLikeCnt;
	}
	

/* */
}
