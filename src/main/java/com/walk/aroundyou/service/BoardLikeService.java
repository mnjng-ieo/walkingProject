package com.walk.aroundyou.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardLike;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.CourseLike;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.dto.BoardLikeRequestDTO;
import com.walk.aroundyou.dto.CourseLikeRequestDTO;
import com.walk.aroundyou.repository.BoardLikeRepository;
import com.walk.aroundyou.repository.BoardRepository;
import com.walk.aroundyou.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

	private final BoardLikeRepository boardLikeRepository;
	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	
	/**
	 * board_like 테이블에 데이터 있으면 true(1) 반환, 없으면 false(0) 반환
	 */
	
	public boolean isBoardLiked(String userId, Long boardId) {
		Member user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new IllegalArgumentException(
						"user id not found : " + userId));
		Board board = boardRepository.findById(boardId)
				.orElseThrow(() -> new IllegalArgumentException(
						"board id not found : " + boardId));
		Optional<BoardLike> boardLike = 
				boardLikeRepository.findByUserIdAndBoardId(user, board);
		return boardLike.isPresent();  
	}	
	
	/**
	 * 한 명의 유저가 게시물에 좋아요 추가
	 * @throws Exception 
	 */
	@Transactional
	public void insertLike(BoardLikeRequestDTO boardLikeRequestDTO) throws Exception {
		
		// 좋아요를 한 user의 id와 board의 id를 전달받아 각각 데이터를 찾는다!
		Member user = userRepository.findByUserId(boardLikeRequestDTO.getUserId())
				.orElseThrow(() -> new IllegalArgumentException(
						"user id not found : " + boardLikeRequestDTO.getUserId()));
		Board board = boardRepository.findById(boardLikeRequestDTO.getBoardId())
				.orElseThrow(() -> new IllegalArgumentException(
						"board id not found : " + boardLikeRequestDTO.getBoardId()));
		
		// boardLike에 데이터가 이미 있는 경우, 이미 좋아요 된 상태이므로 에러 반환
		if(boardLikeRepository.findByUserIdAndBoardId(user, board).isPresent()) {
			throw new Exception("ALREADY_LIKED");
		}
		
		// DTO로부터 찾은 user와 board로 엔티티를 만든다.
		BoardLike boardLike = BoardLike.builder()
				.boardId(board)
				.userId(user)
				.build();
		
		// 디비에 최종 저장한다.
		boardLikeRepository.save(boardLike);
	}

	
	/**
	 * 한 명의 유저가 산책로에 좋아요 삭제
	 */
	@Transactional
	public void deleteLike(BoardLikeRequestDTO boardLikeRequestDTO) {
		
		// 좋아요 취소를 한 user의 id와 board의 id를 전달받아 각각 데이터를 찾는다!
		Member user = userRepository.findByUserId(boardLikeRequestDTO.getUserId())
				.orElseThrow(() -> new IllegalArgumentException(
						"user id not found : " + boardLikeRequestDTO.getUserId()));
		Board board = boardRepository.findById(boardLikeRequestDTO.getBoardId())
				.orElseThrow(() -> new IllegalArgumentException(
						"board id not found : " + boardLikeRequestDTO.getBoardId()));

		
		// 찾은 user와 board로 boardLike 데이터를 찾는다. 
		// 찾는 boardLike 데이터가 없다면, 없는 좋아요를 취소하는 것이므로 에러 반환
		BoardLike boardLike = 
				boardLikeRepository.findByUserIdAndBoardId(user, board)
				.orElseThrow(() -> new IllegalArgumentException(
						"boardLike id not found"));
		
		// 해당 boardLike를 최종 삭제한다.
		boardLikeRepository.delete(boardLike);
	}


}