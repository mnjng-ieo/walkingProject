package com.walk.aroundyou.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardTag;
import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.repository.BoardRepository;
import com.walk.aroundyou.repository.BoardTagRepository;
import com.walk.aroundyou.repository.TagRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TagService {
	@Autowired
	private TagRepository tagRepository;
	@Autowired
	private BoardRepository boardRepository;
	
	/// TagRepository 사용하여 출력 확인하기
	// 1. 기존 태그 tag 테이블에서 삭제하기
	public void deleteByTagId(Long tagId) {
		tagRepository.deleteByTagId(tagId);
	}
	
	// 2. 새로운 태그 tag 테이블에 추가하기
	// 동일한 태그가 있으면 tag 테이블에 저장하지 않는 방식
	public Tag saveTag(String tagContent) { 
		log.info("saveTag() 들어옴....");

		// 저장하지 않는 경우를 else에 적용하기 위해 부정문을 사용함
		if(!tagRepository.existsByTagContent(tagContent)) {
			log.info("tag테이블에 {}가 없음...", tagContent);
			// 해당 tagContent가 존재하지 않는 경우 저장
			tagRepository.saveTag(tagContent);
//		} else {
//			// 해당 tagContent가 존재하는 경우 저장하지 않음
		} log.info("tag테이블에 {}가 있음...", tagContent);
		
		log.info("saveTag() 조건문 완료....");
		return tagRepository.findIdByTagContent(tagContent);
	}
	
	// 3. 게시물 하나의 해시태그 리스트 조회하기
	public List<String> findTagsByBoardId(Long boardId) {
		return tagRepository.findTagsByBoardId(boardId);
	}
	
	// 4. 태그를 테이블에 저장할 때 존재하는 태그인지 조회하고 저장하기
	// 4-1. 게시글에 저장된 해시태그 파싱하기
	public List<String> createTagList(Long boardId) {
		// 정규식 사용
		// # : 으로 시작하는(해시태그를 나타냄)
		// (\\S+) : 첫 번째 그룹에 하나 이상의 공백이 아닌 문자가 하나 이상 나온다는 의미
		// 정규식을 활용해 문자열을 검증, 탐색을 돕는 Pattern, Matcher 클래스
		Pattern MY_PATTERN = Pattern.compile("#(\\S+)"); // 패턴 생성(#해시태그)
		// Optional<Board> post = boardRepository.findById(board.getBoardId());
		Board post = boardRepository.findById(boardId).get();
		Matcher mat = MY_PATTERN.matcher(post.getBoardContent()); // 게시물 가져오기
		List<String> tagList = new ArrayList<>(); // 배열 생성
		
		while(mat.find()) { // find() : 패턴이 일치하는 다음 문자열 찾기
			// List컬렉션의 add메소드 사용
			// Matcher 클래스의 group() : 매칭되는 문자열 중 첫번째 그룹의 문자열 반환
			tagList.add((mat.group(1))); 
		}
		log.info("생성된 TagList : {}", tagList.toString());	
		return tagList;
	}
	// 4-2. 저장하기 구현
	
	// 5. 동일한 tag_id를 가진 board_tag_id의 tag_content 출력
	// 메인화면에 '지금 핫한 해시태그'에 출력될 내용
	public List<String> findTagsByBoardTagId() {
		return tagRepository.findTagsByBoardTagId();
	}
	
	// (추가)해시태그 클릭 시 게시물 목록 페이지 출력
	public List<Board> findBoardByTag(String tagContent) {
		return boardRepository.findBoardByTag(tagContent);
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
	public void saveBoardTag(Long boardId, String tagContent) {
		// 태그 테이블에 추가하기(saveTag() 사용하여 존재하는 id면 저장)
		// saveTag(tagContent) : tagId를 반환
		log.info("saveBoardTag() 들어옴....");
		//
		BoardTag boardTag = new BoardTag();
		// Board.builder().boardId(boardId).build()
		// == Board board = new Board();
		//    board.setBoardId(boardId);
		// boardTag 멤버에 값 부여
		boardTag.setBoardId( // boardId는 Board객체이기 때문에 빌더패턴으로 객체 생성
					Board.builder()
						.boardId(boardId) // 빌더패턴으로 값 부여
						.build()
					);
		boardTag.setTagId(saveTag(tagContent));
//		Tag tag = saveTag(tagContent); // 상단의 코드와 동일한 내용 풀어쓰는 법
//		boardTag.setTagId(tag);

		log.info("saveBoardTag() boardTag에 TagId인 {}를 입력함....", boardId);
		// 이력 테이블에 추가하기
		boardTagRepository.saveBoardTag(boardTag);
	}
}
