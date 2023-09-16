package com.walk.aroundyou.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.CommentLike;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.dto.CommentLikeRequestDTO;
import com.walk.aroundyou.repository.CommentLikeRepository;
import com.walk.aroundyou.repository.CommentRepository;
import com.walk.aroundyou.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

	private final CommentLikeRepository commentLikeRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	
	/**
	 * comment_like 테이블에 데이터 있으면 true(1) 반환, 없으면 false(0) 반환
	 */
	
	public boolean isCommentLiked(String userId, Long commentId) {
		Member user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new IllegalArgumentException(
						"user id not found : " + userId));
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new IllegalArgumentException(
						"course id not found : " + commentId));
		Optional<CommentLike> commentLike = 
				commentLikeRepository.findByUserIdAndCommentId(user, comment);
		return commentLike.isPresent();  
	}	
	
	/**
	 * 한 명의 유저가 게시물에 좋아요 추가
	 * @throws Exception 
	 */
	@Transactional
	public void insertLike(CommentLikeRequestDTO commentLikeRequestDTO) throws Exception {
		
		// 좋아요를 한 user의 id와 comment의 id를 전달받아 각각 데이터를 찾는다!
		Member user = userRepository.findByUserId(commentLikeRequestDTO.getUserId())
				.orElseThrow(() -> new IllegalArgumentException(
						"user id not found : " + commentLikeRequestDTO.getUserId()));
		Comment comment = commentRepository.findById(commentLikeRequestDTO.getCommentId())
				.orElseThrow(() -> new IllegalArgumentException(
						"course id not found : " + commentLikeRequestDTO.getCommentId()));
		
		// commentLike에 데이터가 이미 있는 경우, 이미 좋아요 된 상태이므로 에러 반환
		if(commentLikeRepository.findByUserIdAndCommentId(user, comment).isPresent()) {
			throw new Exception("ALREADY_LIKED");
		}
		
		// DTO로부터 찾은 user와 course로 엔티티를 만든다.
		CommentLike commentLike = CommentLike.builder()
				.commentId(comment)
				.userId(user)
				.build();
		
		// 디비에 최종 저장한다.
		commentLikeRepository.save(commentLike);
	}

	
	/**
	 * 한 명의 유저가 산책로에 좋아요 삭제
	 */
	@Transactional
	public void deleteLike(CommentLikeRequestDTO commentLikeRequestDTO) {
		
		// 좋아요 취소를 한 user의 id와 course의 id를 전달받아 각각 데이터를 찾는다!
		Member user = userRepository.findByUserId(commentLikeRequestDTO.getUserId())
				.orElseThrow(() -> new IllegalArgumentException(
						"user id not found : " + commentLikeRequestDTO.getUserId()));
		Comment comment = commentRepository.findById(commentLikeRequestDTO.getCommentId())
				.orElseThrow(() -> new IllegalArgumentException(
						"course id not found : " + commentLikeRequestDTO.getCommentId()));

		
		// 찾은 user와 course로 courseLike 데이터를 찾는다. 
		// 찾는 courseLike 데이터가 없다면, 없는 좋아요를 취소하는 것이므로 에러 반환
		CommentLike commentLike = 
				commentLikeRepository.findByUserIdAndCommentId(user, comment)
				.orElseThrow(() -> new IllegalArgumentException(
						"courseLike id not found"));
		
		// 해당 courseLike를 최종 삭제한다.
		commentLikeRepository.delete(commentLike);
	}


}