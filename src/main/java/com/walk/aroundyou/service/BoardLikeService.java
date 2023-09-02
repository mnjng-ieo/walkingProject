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

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// service : repository 의 method 에 기반하여, 더 상세한 비즈니스로직 작성


@Service
@AllArgsConstructor
@NoArgsConstructor
public class BoardLikeService {

	@Autowired
	BoardLikeRepository boardLikeRepository;
	
	@Autowired
	BoardLikeDTO boardLikeDTO;
	
	
	
	// 특정 게시물(boardId)에 대한 좋아요(userId) 수
	// : 목록의 요소는 userId 대신 userId와 매칭되는 (프로필 사진 이미지/닉네임)으로 조회된다. (user 권한에서)
	// : 목록의 요소는 userId 그리고 userId와 매칭되는 (프로필 사진 이미지/닉네임)으로 조회된다. (admin 권한에서)
	public Long countUserIdByBoardId(Board boardId) {
		
        return boardLikeRepository.countUserIdByBoardId(boardId);
    }
	
	
	// 특정 게시물(boardId)에 대한 좋아요(userId) 누른 회원들 목록
	public List<Long> findUserIdByBoardId(Board boardId) {
		
		return boardLikeRepository.findUserIdByBoardId(boardId); 
	}
	
	
	
	
	// 마이페이지에서 좋아요 누른 누적 횟수 확인
	public Long countLikedBoardIdByUserId(User userId) {
		
		return boardLikeRepository.countLikedBoardIdByUserId(userId);
	}
	
	// 마이페이지에서 좋아요한 게시물 목록
	public List<Long> findLikedBoardIdByUserId(User userId) {
		
        return boardLikeRepository.findLikedBoardIdByUserId(userId);
    }

	
	
	// 사용자가 게시물 좋아요 표시 클릭 시 선택 + 해제
	public boolean toggleLike(User userId, Board boardId) {
		
		Optional<BoardLike> boardLikeDTO = boardLikeRepository.findByUserIdAndBoardId(userId, boardId);
		
		
		
		
		
		// boolean의 디폴트는 false
		
		// 현재는 값이 없으면(null) true, 있으면 false
		// 값이 있을때(좋아요한 게시물일 때) true로 나오길 원하면 아래를 사용
		// return !existingLike.isEmpty();
		return boardLikeDTO.isEmpty();
	}



	

	
}
