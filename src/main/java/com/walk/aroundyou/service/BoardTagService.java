package com.walk.aroundyou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.BoardTag;
import com.walk.aroundyou.repository.BoardTagRepository;

@Service
public class BoardTagService {

	@Autowired
	private BoardTagRepository boardTagRepository;
	
	/// BoardTagRepository 사용하여 출력 확인하기
	// 1. 게시물 삭제 시 board_tag 테이블에서 삭제하기
	public void deleteByBoardTagId(Long boardTagId) {
		// boardTagRepository.deleteById(boardTagId); // 기본 메서드 사용 
		boardTagRepository.deleteByBoardTagId(boardTagId);
	}
	
	// 2. 새로운 태그 board_tag 테이블에 추가하기
	public void saveBoardTag(BoardTag boardTag) {
		boardTagRepository.saveBoardTag(boardTag);
	}
	

}
