package com.walk.aroundyou.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardTag;
import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.dto.IBoardListResponse;
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
	
	// 화면에 보이는 최대 게시글 수 10개
	// 게시물 목록페이지에서 하나의 게시물이 차지하는 비율이 커졌으므로 10으로 수정
	private final static int SIZE_OF_PAGE = 10;
	
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
	
	// 4. 새로운 게시글에 저장된 해시태그 파싱하기
	public List<String> createTagList(Long boardId) {
		// 정규식 사용
		// 정규식을 활용해 문자열을 검증, 탐색을 돕는 Pattern, Matcher 클래스
		Pattern MY_PATTERN = Pattern.compile("#([^\\s#]+)"); // 패턴 생성(#해시태그)
		// Optional<Board> post = boardRepository.findById(board.getBoardId());
		Board post = boardRepository.findById(boardId).get();
		Matcher mat = MY_PATTERN.matcher(post.getBoardContent()); // 게시물 가져오기
		List<String> tagList = new ArrayList<>(); // 배열 생성
		
		while(mat.find()) { // find() : 패턴이 일치하는 다음 문자열 찾기
			// List컬렉션의 add메소드 사용
			// Matcher 클래스의 group() : 매칭되는 문자열 중 첫번째 그룹의 문자열 반환
			tagList.add((mat.group(1))); 
		}
		// 중복 값 지우기
		Set<String> set = new HashSet<>(tagList);
		List<String> newTagList = new ArrayList<>(set);
		
		log.info("생성된 TagList : {}", newTagList.toString());	
		return newTagList;
	}
	
	// 5. 동일한 tag_id를 가진 board_tag_id의 tag_content 출력
	// 메인화면에 '지금 핫한 해시태그'에 출력될 내용
	public List<String> findTagsByBoardTagId() {
		return tagRepository.findTagsByBoardTagId();
	}
	
	// 6. (수정)해시태그 클릭 시 게시물 목록 페이지 출력하는데 좋아요 수, 댓글 수를 포함한 게시물
	public Page<IBoardListResponse> findBoardAndCntByTagId(Tag tagId, int page, String sort) {
		// 최신순, 조회수, 좋아요순으로 정렬
		// HTML의 select option 태그의 value를 같은 이름으로 설정하기
		Sort customSort;
		if ("boardViewCount".equals(sort)) {
			customSort = Sort.by(Direction.DESC, "boardViewCount");
		} else if("likeCnt".equals(sort)) {
			customSort = Sort.by(Direction.DESC, "likeCnt");
		} else {
			customSort = Sort.by(Direction.DESC, "boardId");
		}
		// public static PageRequest of(int pageNumber, int pageSize, Sort sort) 사용
		return boardRepository.findBoardAndCntByTagId(tagId, PageRequest.of(page, SIZE_OF_PAGE, customSort));
	}
	
	
	// [메인페이지] - tag_content으로 tag_id 추출하기 (메인페이지에서 핫한 태그 클릭 시, ajax 구현 과정에서 쓰임)
	public Tag findIdByTagContent(String tagContent) {
		return tagRepository.findIdByTagContent(tagContent);
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

	
	// [메인페이지] - 핫한 해시태그 클릭 시, 관련된 게시물 출력 
	public Page<IBoardListResponse> findBoardAndCntByMainTagId(Tag tagId) {
		// 최신순, 조회수, 좋아요순으로 정렬
		// HTML의 select option 태그의 value를 같은 이름으로 설정하기
		Sort customSort = Sort.by(Direction.DESC, "likeCnt");
		final int BOARD_OF_MAIN_TAG = 5;
		// public static PageRequest of(int pageNumber, int pageSize, Sort sort) 사용
		return boardRepository.findBoardAndCntByTagId(tagId, PageRequest.of(0, BOARD_OF_MAIN_TAG, customSort));
	}
	
	
	// [메인페이지] - 핫한 해시태그가 포함된 게시물 목록에 출력되는 디폴트 게시물 리스트 
	public List<IBoardListResponse> findBoardAndCntByMainTagDefault(String tagContent) {
		return boardRepository.findBoardAndCntByMainTagDefault(tagContent);
	}
	
}