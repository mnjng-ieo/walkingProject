package com.walk.aroundyou.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardTag;
import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.repository.BoardTagRepository;
import com.walk.aroundyou.repository.TagRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TagService {
	@Autowired
	private TagRepository tagRepository;
	
	/// TagRepository 사용하여 출력 확인하기
	// 1. 기존 태그 tag 테이블에서 삭제하기[관리자용]
	public void deleteByTagId(Long tagId) {
		tagRepository.deleteByTagId(tagId);
	}
	
	// 2. 새로운 태그 tag 테이블에 추가하기
	// saveTag메소드 DTO 사용에서 엔티티 클래스 활용하는 방식으로 변경
	/// 변경 전
//	public void saveTag(AddTagRequest request) {
//		tagRepository.saveTag(request.getTagContent());
//	}
	/// 변경 후
	// 동일한 태그가 있으면 tag 테이블에 저장하지 않는 방식
	public Tag saveTag(String tagContent) { 
		Tag hashTag = new Tag();
		hashTag.setTagContent(tagContent);
		
		if(tagRepository.existsByTagContent(tagContent)) {
			// 해당 tagContent가 존재하는 경우 저장하지 않음
		} else {
			// 해당 tagContent가 존재하지 않는 경우 저장
			tagRepository.saveTag(tagContent);
		}
		return tagRepository.findIdByTagContent(tagContent);
	}
	/// 또 변경해보기
//	public void saveTagAndBoardTag(List<String> tags) {
//		for (String tag : tags) {
//			boolean existingTag = tagRepository.existsByTagContent(tag);
//			
//			if(existingTag == false) {
//				Tag newTag = new Tag();
//				newTag.setTagContent(tag);
//				tagRepository.saveTag(tag);
//				BoardTag newBoardTag = new BoardTag();
//				boardTagRepository.saveBoardTag(newBoardTag);
//			}
//			
//			BoardTag newBoardTag = new BoardTag();
//			boardTagRepository.saveBoardTag(newBoardTag);
//			
//		}
//	}

	
	// 3. 게시물 하나의 해시태그 리스트 조회하기
	public List<String> findTagsByBoardId(Long boardId) {
		return tagRepository.findTagsByBoardId(boardId);
	}
	
	// 4. 태그를 테이블에 저장할 때 존재하는 태그인지 조회하고 저장하기
	// 4-1. 게시글에 저장된 해시태그 파싱하기
	public void createTagList(Board post) {
		// 정규식 사용
		// # : 으로 시작하는(해시태그를 나타냄)
		// (\\S+) : 첫 번째 그룹에 하나 이상의 공백이 아닌 문자가 하나 이상 나온다는 의미
		// 정규식을 활용해 문자열을 검증, 탐색을 돕는 Pattern, Matcher 클래스
		Pattern MY_PATTERN = Pattern.compile("#(\\S+)"); // 패턴 생성
		Matcher mat = MY_PATTERN.matcher(post.getBoardContent()); // 게시물 가져오기
		List<String> tagList = new ArrayList<>(); // 배열 생성
		
		while(mat.find()) { // 패턴이 일치하는 다음 문자열 찾기
			// List컬렉션의 add메소드 사용
			// Matcher 클래스의 group() : 매칭되는 문자열 중 첫번째 그룹의 문자열 반환
			tagList.add((mat.group(1))); 
		}
		System.out.println("Success! -----> " + tagList);
		log.info("생성된 TagList : {}", tagList.toString());
	}
	
	// 5. 동일한 tag_id를 가진 board_tag_id의 tag_content 출력
	// 메인화면에 지금 핫한 해시태그에 출력될 내용
	public List<String> findTagsByBoardTagId() {
		return tagRepository.findTagsByBoardTagId();
	}

	
	/*---------------------------------------------------*/
	/// BoardTagRepository 사용하여 출력 확인하기
	@Autowired
	private BoardTagRepository boardTagRepository;
	
	/// BoardTagRepository 사용하여 출력 확인하기
	// 1. 게시물 삭제 시 board_tag 테이블에서 삭제하기
	public void deleteByBoardId(Long boardId) { 
		// boardTagRepository.deleteByBoardTagId(boardTag); // 이력 id로 각각 삭제
		boardTagRepository.deleteByBoardId(boardId); 		// 게시물 id로 한번에 삭제

	}
	
	// 2. 새로운 태그 board_tag 테이블에 추가하기
//	public void saveBoardTag(BoardTag boardTag) {
//		boardTagRepository.saveBoardTag(boardTag);
//	}
	public void saveBoardTag(BoardTag boardTag, String tagContent) {
		// 태그 테이블에 추가하기(saveTag() 사용하여 존재하는 id면 저장)
		// saveTag(tagContent) : tagId를 반환
		boardTag.setTagId(saveTag(tagContent));
		// 이력 테이블에 추가하기
		boardTagRepository.saveBoardTag(boardTag);
	}
}
