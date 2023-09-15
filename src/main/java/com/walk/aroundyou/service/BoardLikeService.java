package com.walk.aroundyou.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardLike;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.repository.BoardLikeRepository;

@Service
public class BoardLikeService {

	@Autowired
	private BoardLikeRepository boardLikeRepository;
	
	
	// 특정 게시물 좋아요 개수
	public Long countLike(Board boardId) {
		
        return boardLikeRepository.countByBoardId(boardId);
    }
	
	
	// 마이페이지에서 좋아요한 게시물 조회
	public List<Long> findLikedBoardByUser(Member member) {
		
        return boardLikeRepository.findLikedBoardByUserId(member);
    }

	
	// 사용자가 게시물 좋아요 표시 클릭 시 선택 + 해제
	public boolean toggleLike(Member member, Board board) {
		
		Optional<BoardLike> existingLike = boardLikeRepository.findByUserIdAndBoardId(member, board);
		
		// 값으로 채워져 있는지 여부 판단
		// 현재는 값이 없으면 true, 있으면 false
		// 값이 있을때(좋아요한 게시물일떄) true로 나오길 원하면 아래를 사용
		// return !existingLike.isEmpty();
		return existingLike.isEmpty();
	}
	
}
