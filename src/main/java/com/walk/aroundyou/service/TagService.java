package com.walk.aroundyou.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.BoardTag;
import com.walk.aroundyou.dto.AddTagRequest;
import com.walk.aroundyou.repository.BoardTagRepository;
import com.walk.aroundyou.repository.TagRepository;

@Service
public class TagService {
	@Autowired
	private TagRepository tagRepository;
	
	/// TagRepository 사용하여 출력 확인하기
	// 1. 기존 태그 tag 테이블에서 삭제하기
	public void deleteByTagId(Long tagId) {
		tagRepository.deleteByTagId(tagId);
	}
	
	// 2. 새로운 태그 tag 테이블에 추가하기
	public void saveTag(AddTagRequest request) {
		 tagRepository.saveTag(request.getTagContent());
	}
	
	// 3. 게시물 하나의 해시태그 리스트 조회하기
	public List<String> findTagsByBoardId(Long boardId) {
		return tagRepository.findTagsByBoardId(boardId);
	}
	
	/*---------------------------------------------------*/
	
	@Autowired
	private BoardTagRepository boardTagRepository;
	
	/// BoardTagRepository 사용하여 출력 확인하기
	// 1. 게시물 삭제 시 board_tag 테이블에서 삭제하기
	public void deleteByBoardTagId(Long boardTag) { 
		boardTagRepository.deleteByBoardTagId(boardTag);
	}
	
	// 2. 새로운 태그 board_tag 테이블에 추가하기
	public void saveBoardTag(BoardTag boardTag) {
		boardTagRepository.saveBoardTag(boardTag);
	}
	
}
