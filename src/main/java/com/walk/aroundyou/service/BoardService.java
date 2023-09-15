package com.walk.aroundyou.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.dto.BoardRequest;
import com.walk.aroundyou.dto.IBoardDetailResponse;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.repository.BoardRepository;
import com.walk.aroundyou.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class BoardService {
	
	@Autowired
	private BoardRepository BoardRepo;
	
	@Autowired
	private UserRepository userRepository;
	
	private final static int SIZE_OF_PAGE = 10;

	// 연서 추가
	// 메인 페이지 검색 결과에서 게시물 더보기 클릭하면 나오는 페이지(게시물 리스트)
	// 게시물 리스트 페이지(타입이 선택되지 않을 경우)
	public Page<IBoardListResponse> findBoardAndCntByKeyword(
			String keyword, int page, String sort) {

		Sort customSort = selectSort(sort); // 자주 쓰이는 함수이므로 하단에 메서드로 뺌

		if (keyword != null) { // 검색어가 있으면 공백 제거
			keyword = '%' + keyword.replace(" ", "") + '%';
		} else { // 검색어가 없으면 전체 내용을 가져옴
			keyword = "%";
		}
		return BoardRepo.findBoardAndCntByKeyword(keyword, PageRequest.of(page, SIZE_OF_PAGE, customSort));

	}
	
	// 연서 추가
	// 게시물 리스트 페이지(타입 선택하여 결과 출력)
	public Page<IBoardListResponse> findBoardAndCntByKeywordAndType(
			String type, String keyword, int page, String sort) {

		Sort customSort = selectSort(sort); 

		if (keyword != null) {
			keyword = '%' + keyword.replace(" ", "") + '%';
		} else {
			keyword = "%";
		}
		
		return BoardRepo.findBoardAndCntByKeywordAndType(type, keyword, PageRequest.of(page, SIZE_OF_PAGE, customSort));
	}

	// 연서 추가
	// 게시물 리스트 페이지(검색어 타입만 선택하여 결과 출력)
	public Page<IBoardListResponse> findBoardAndCntByKeywordAndSearchType(
			String keyword, String searchType, int page, String sort) {

		Sort customSort = selectSort(sort); // 자주 쓰이는 함수이므로 하단에 메서드로 뺌
		
		if (keyword != null) { // 검색어가 있으면 공백 제거
			keyword = '%' + keyword.replace(" ", "") + '%';
		} else { // 검색어가 없으면 전체 내용을 가져옴
			keyword = "%";
		}
		// 검색어타입에 대한 출력 결과를 다르게 하기위해 switch 사용
	    switch (searchType) {
        case "boardTitle":
        	return BoardRepo.findBoardAndCntByTitle(keyword, PageRequest.of(page, SIZE_OF_PAGE, customSort));
            
        case "boardContent":
        	return BoardRepo.findBoardAndCntByContent(keyword, PageRequest.of(page, SIZE_OF_PAGE, customSort));
           
        case "userNickname":
        	return BoardRepo.findBoardAndCntByNickname(keyword, PageRequest.of(page, SIZE_OF_PAGE, customSort));
           
        default:
        	return BoardRepo.findBoardAndCntByKeyword(keyword, PageRequest.of(page, SIZE_OF_PAGE, customSort));
	    }	
	}
	
	// 연서 추가
	// 게시물 리스트 페이지(게시물 타입, 검색어 타입 선택하여 결과 출력)
	public Page<IBoardListResponse> findBoardAndCntByKeywordAndTypeAndSearchType(
			String type, String searchType, String keyword, 
			int page, String sort) {

		Sort customSort = selectSort(sort); 
		
		if (keyword != null) {
			keyword = '%' + keyword.replace(" ", "") + '%';
		} else {
			keyword = "%";
		}
		
		// 게시물타입, 검색어타입에 대한 출력 결과를 다르게 하기위해 switch 사용
	    switch (searchType) {
        case "boardTitle":
        	return BoardRepo.findBoardAndCntByTitleAndType(type, keyword, PageRequest.of(page, SIZE_OF_PAGE, customSort));
            
        case "boardContent":
        	return BoardRepo.findBoardAndCntByContentAndType(type, keyword, PageRequest.of(page, SIZE_OF_PAGE, customSort));
           
        case "userNickname":
        	return BoardRepo.findBoardAndCntByNicknameAndType(type, keyword, PageRequest.of(page, SIZE_OF_PAGE, customSort));
           
        default:
        	return BoardRepo.findBoardAndCntByKeywordAndType(type, keyword, PageRequest.of(page, SIZE_OF_PAGE, customSort));
	    }		
		
	}
	
	
	public Page<IBoardListResponse> findboardAllList(int page) {
		// TODO Auto-generated method stub
		//return BoardRepo.findBoardAndCnt();
		return BoardRepo.findBoardAndCnt(PageRequest.of(page, SIZE_OF_PAGE));
	}
	

	public Page<IBoardListResponse> findboardAllListByType(String type,int page) {
		// TODO Auto-generated method stub
		//return BoardRepo.findBoardAndCnt();
		return BoardRepo.findBoardAndCntByType(type, PageRequest.of(page, SIZE_OF_PAGE));
	}

	public Optional<IBoardDetailResponse> findBoardDetail(Long id) {
		BoardRepo.updateViewCount(id);
		return BoardRepo.findBoardDetailById(id);
	}


	public Optional<Long> save(BoardRequest board) {
		Optional<Member> member = userRepository.findById(board.getUserId());
		if(member.isPresent()) {
			log.info("사용자를 찾았어요");			
			board.setUserNickname(member.get().getUserNickname());
		}else {
			log.info("사용자를 못 찾았어요");	
		}
		return Optional.ofNullable(BoardRepo.save(board.toInsertEntity()).getBoardId());
	}


	public boolean deleteById(Long board, String userId) {
		try {
			Optional<Board> checkBoard = BoardRepo.findById(board);
			if(checkBoard.isEmpty() || !checkBoard.get().getUserId().getUserId().equals(userId)) {
				throw new Exception("잘못된 접근");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		BoardRepo.deleteById(board);
		if(BoardRepo.findById(board) != null) {
			return true;
		} else {			
			return false;
		}
	}

	public boolean update(BoardRequest board) {
		Board getBoard = BoardRepo.findById(board.getBoardId()).get();
		try {
			Optional<Board> checkBoard = BoardRepo.findById(board.getBoardId());
			if(checkBoard.isEmpty() || !checkBoard.get().getUserId().getUserId().equals(board.getUserId())) {
				throw new Exception("잘못된 접근");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		board.setBoardCreatedDate(getBoard.getBoardCreatedDate());
		board.setBoardViewCount(getBoard.getBoardViewCount());
		Optional<Member> member = userRepository.findById(board.getUserId());
		if(member.isPresent()) {			
			board.setUserNickname(member.get().getUserNickname());
		}
		if(BoardRepo.save(board.toUpdateEntity()) != null) {
			return true;
		} else {			
			return false;
		}
	}



	public Optional<Board> findById(Long boardId) {
		return BoardRepo.findById(boardId);
	}
	

	// 연서 추가
	// 정렬 기준 설정
	// HTML의 select option 태그의 value를 같은 이름으로 설정하기
	private Sort selectSort(String sort) {
		Sort customSort;
		if ("boardViewCount".equals(sort)) {
			customSort = Sort.by(Direction.DESC, "boardViewCount");
		} else if("likeCnt".equals(sort)) {
			customSort = Sort.by(Direction.DESC, "likeCnt");
		} else {
			customSort = Sort.by(Direction.DESC, "boardId");
		}
		return customSort;
	}

	////// 좋아요 기능 추가
	public int getBoardLikeCntByBoardId(Long boardId) {
		int boardLikeCnt = BoardRepo.countBoardLikesByBoardId(boardId);
		
		return boardLikeCnt;
	}
	
	
}
