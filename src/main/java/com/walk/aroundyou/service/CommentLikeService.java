package com.walk.aroundyou.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.CommentLike;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.dto.CommentLikeRequestDto;
import com.walk.aroundyou.repository.CommentLikeRepository;
import com.walk.aroundyou.repository.CommentRepository;
import com.walk.aroundyou.repository.UserRepository;

import jakarta.transaction.Transactional;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
	
	private final CommentLikeRepository commentLikeRepo;
	private final CommentRepository commentRepo;
	private final UserRepository userRepo;
	
	
	// comment_like 테이블에 데이터 있으면 true(1) 반환, 없으면 false(0) 반환 
	public boolean isCommentLiked(String userId, Long commentId) {
		
		// UserRepository의 findByUserId 메서드를 이용해 User 객체 조회 
		Member user = userRepo.findByUserId(userId)
						.orElseThrow(() -> new IllegalArgumentException(
								"Could not found user id : " + userId ));
		
		// CommentRepository의 findById 메서드를 이용해 Comment 객체 조회 
		Comment comment = commentRepo.findById(commentId)
						.orElseThrow(() -> new IllegalArgumentException(
								"Could not found commentId : " + commentId ));
	
		Optional<CommentLike> commentLike = 
							commentLikeRepo.findByUserIdAndCommentId(user, comment);
		
		// Optional의 isPresent() 메서드로 commentLike가 존재할 경우, true 반환 
		return commentLike.isPresent();						
	}
	
	
	// insertCommentLike 메서드 - 좋아요를 하는 기능 
	@Transactional
	public void insertCommentLike(CommentLikeRequestDto commentLikeRequestDto) throws Exception {
		
		// UserRepository의 findByUserId 메서드를 이용해 User 객체 조회 
		Member user = userRepo.findByUserId(commentLikeRequestDto.getUserId())
						.orElseThrow(() -> new NotFoundException("Could not found user id : "+ commentLikeRequestDto.getUserId()));
		
		// CommentRepository의 findById 메서드를 이용해 Comment 객체 조회 
		Comment comment = commentRepo.findById(commentLikeRequestDto.getCommentId())
						.orElseThrow(() -> new NotFoundException("Could not found course id : " + commentLikeRequestDto.getCommentId()));
	
		// 이미 좋아요인 상태면, 에러 반환 
		if(commentLikeRepo.findByUserIdAndCommentId(user, comment).isPresent()) {
			throw new Exception();
		}
	
		// * CommentLike 엔티티 클래스에 @Builder 어노테이션 추가 
		// 이미 좋아요한 상태가 아닌 경우에만 해당 CommentLike 엔티티를 생성해 저장한다.
		CommentLike commentLike = CommentLike.builder()
									.userId(user)
									.commentId(comment)
									.build();
		
		// 해당 commentLike 저장 
		commentLikeRepo.save(commentLike);
	}
	
	
	// deleteCommentLike 메서드 - 좋아요를 취소하는 기능 
	@Transactional
	public void deleteCommentLike(CommentLikeRequestDto commentLikeRequestDto) throws NotFoundException {
		
		// UserRepository의 findByUserId 메서드를 이용해 User 객체 조회 
		Member user = userRepo.findByUserId(commentLikeRequestDto.getUserId())
						.orElseThrow(() -> new NotFoundException("Could not found user id : "+ commentLikeRequestDto.getUserId()));

		// CommentRepository의 findById 메서드를 이용해 Comment 객체 조회 
		Comment comment = commentRepo.findById(commentLikeRequestDto.getCommentId())
						.orElseThrow(() -> new NotFoundException("Could not found course id : " + commentLikeRequestDto.getCommentId()));
	
		// 위에서 조회한 user와 comment로 해당 CommentLike 조회 
		CommentLike commentLike = commentLikeRepo.findByUserIdAndCommentId(user, comment)
						.orElseThrow(() -> new NotFoundException("Could not found commentLike"));
		
		// 해당 commentLike 삭제 
		commentLikeRepo.delete(commentLike);
	}
	
	
	
}
