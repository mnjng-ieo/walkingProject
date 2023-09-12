package com.walk.aroundyou.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	
	private final static int SIZE_OF_PAGE = 20;

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


	public boolean deleteById(Long board) {
		BoardRepo.deleteById(board);
		if(BoardRepo.findById(board) != null) {
			return true;
		} else {			
			return false;
		}
	}


	public boolean update(BoardRequest board) {
		Board getBoard = BoardRepo.findById(board.getBoardId()).get();
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
	
	
}
