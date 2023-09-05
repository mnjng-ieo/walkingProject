package com.walk.aroundyou.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardLike;
import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.dto.BoardLikeDTO;
import com.walk.aroundyou.repository.BoardLikeRepository;
import com.walk.aroundyou.repository.BoardRepository;
import com.walk.aroundyou.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


// service : repository 의 method 에 기반하여, 더 상세한 비즈니스로직 작성


@Service
@AllArgsConstructor
public class BoardLikeService {

	@Autowired
	BoardLikeRepository boardLikeRepository;
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	UserRepository userRepository;
	
	
	
	// 특정 게시물(boardId)에 대한 좋아요(userId) 수
	// : 목록의 요소는 userId 대신 userId와 매칭되는 (프로필 사진 이미지/닉네임)으로 조회된다. (user 권한에서)
	// : 목록의 요소는 userId 그리고 userId와 매칭되는 (프로필 사진 이미지/닉네임)으로 조회된다. (admin 권한에서)
	@Transactional
	public Long countUserIdByBoardId(Board boardId) {
		
        return boardLikeRepository.countUserIdByBoardId(boardId);
    }
	
	
	// 특정 게시물(boardId)에 대한 좋아요(userId) 누른 회원들 목록
	@Transactional
	public List<Long> findUserIdByBoardId(Board boardId) {
		
		return boardLikeRepository.findUserIdByBoardId(boardId); 
	}
	
	
	
	// 마이페이지에서 좋아요 누른 누적 횟수 확인
	@Transactional
	public Long countLikedBoardIdByUserId(User userId) {
		
		return boardLikeRepository.countLikedBoardIdByUserId(userId);
	}
	
	// 마이페이지에서 좋아요한 게시물 목록
	@Transactional
	public List<Long> findLikedBoardIdByUserId(User userId) {
		
        return boardLikeRepository.findLikedBoardIdByUserId(userId);
    }

	

	@Transactional
	public boolean toggleLike(String userId, Long boardId) {
		
		// findByUserIdAndBoardId의 요구조건에 맞추기 위해 객체형으로 세팅
		User user = User.builder()
				.userId(userId)
				.build();
		Board board = Board.builder()
				.boardId(boardId)
				.build();
		
		// 일치하는 데이터를 가져오기
		Optional<BoardLike> boardLikeDTO = boardLikeRepository.findByUserIdAndBoardId(user, board);
		
		// 일치하는 데이터가 있으면 true, 없으면 false
		return boardLikeDTO.isPresent();
	}
	
	@Transactional
	public void deleteBoardLike(BoardLikeDTO boardLikeDTO) {
		
		User user = userRepository.findByUserId(boardLikeDTO.getUserId())
				
				.orElseThrow(() -> new IllegalArgumentException("userId not found :" + boardLikeDTO.getUserId()));
	
		
		Board board = boardRepository.findById(boardLikeDTO.getBoardId())
				.orElseThrow(() -> new IllegalArgumentException("boardId not found :" + boardLikeDTO.getBoardId()));
		

		// 찾은 user와 board로 boardLike 데이터를 찾는다. 
		// 찾는 boardLike 데이터가 없다면, 없는 좋아요를 취소하는 것이므로 에러 반환
		BoardLike boardLike = boardLikeRepository.findByUserIdAndBoardId(user, board)
				.orElseThrow(() -> new IllegalArgumentException("boardLike id not found"));
		
		boardLikeRepository.delete(boardLike);
		
	}


	@Transactional
	public void insertBoardLike(BoardLikeDTO boardLikeDTO) throws Exception {
		
		User user = userRepository.findByUserId(boardLikeDTO.getUserId())
				
				.orElseThrow(() -> new IllegalArgumentException("userId not found :" + boardLikeDTO.getUserId()));
	
		
		Board board = boardRepository.findById(boardLikeDTO.getBoardId())
				.orElseThrow(() -> new IllegalArgumentException("boardId not found :" + boardLikeDTO.getBoardId()));
		
		
		if(boardLikeRepository.findByUserIdAndBoardId(user, board).isPresent()) {
			throw new Exception("Already_LIKED");
		}
		
		
		// DTO로부터 찾은 user와 board로 엔터티를 만든다.
		BoardLike boardLike = new BoardLike();
		boardLike.setBoardId(board);
		boardLike.setUserId(user);
		
		
		
		boardLikeRepository.save(boardLike);
		
	}


	
	// boardId로 board글 삭제
	@Transactional
	public void deleteBoardByBoardId(Long boardId) throws Exception {
		
		Board board = Board.builder()
				.boardId(boardId)
				.build();
		
		boardLikeRepository.deleteByBoardId(board);
		
	}



	
}
