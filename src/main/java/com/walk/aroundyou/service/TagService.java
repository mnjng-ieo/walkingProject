package com.walk.aroundyou.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.dto.AddTagRequest;
import com.walk.aroundyou.repository.TagRepository;

@Service
public class TagService {
	@Autowired
	private TagRepository tagRepository;
	// 1. 새로운 태그 tag 테이블에 추가하기
	public void save(AddTagRequest request) {
		 tagRepository.saveByTag(request.getTagContent());
	}
	
	// 2. 기존 태그 tag 테이블에서 삭제하기
	public void delete(Long tagId) {
		tagRepository.deleteById(tagId);
	}
	
	// 3. 게시물 하나의 해시태그 리스트 조회하기
	public List<String> findTagsByBoardId(Long boardId) {
		return tagRepository.findTagsByBoardId(boardId);
	}
	

}
