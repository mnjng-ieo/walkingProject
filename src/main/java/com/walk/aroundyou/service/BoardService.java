package com.walk.aroundyou.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.dto.BoardRequest;
import com.walk.aroundyou.dto.IBoardDetailResponse;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.repository.BoardRepository;


@Service
public class BoardService {
	
	@Autowired
	private BoardRepository BoardRepo;
	
	private final static int SIZE_OF_PAGE = 10;

	// 연서 추가
	// 메인 페이지 검색 결과에서 게시물 더보기 클릭하면 나오는 페이지(게시물 리스트)
	public Page<IBoardListResponse> findBoardAndCntByKeyword(String keyword, int page, String sort) {
		// 최신순, 조회수, 좋아요순으로 정렬
		// 검색 키워드 공백 제거
		String formattedKeyword = '%' + keyword.replace(" ", "") + '%';
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
		return BoardRepo.findBoardAndCntByKeyword(formattedKeyword, PageRequest.of(page, SIZE_OF_PAGE, customSort));
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


	public boolean save(BoardRequest board) {
		if(BoardRepo.save(board.toEntity()) != null) {
			return true;
		} else {			
			return false;
		}
	}


	public boolean deleteById(Long board) {
		BoardRepo.deleteById(board);
		if(BoardRepo.findById(board) != null) {
			return true;
		} else {			
			return false;
		}
	}


	public boolean update(BoardRequest board) {
		if(BoardRepo.save(board.toEntity()) != null) {
			return true;
		} else {			
			return false;
		}
	}


	public Optional<Board> findById(Long boardId) {
		return BoardRepo.findById(boardId);
	}
	
	
}