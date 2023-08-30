package com.walk.aroundyou.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.repository.BoardRepository;


@Service
public class BoardService {
	
	@Autowired
	private BoardRepository BoardRepo;

	public Object findboardAllList(int page, int size) {
		// TODO Auto-generated method stub
		//return BoardRepo.findBoardAndCnt();
		return BoardRepo.findBoardAndCnt(PageRequest.of(page, size));
	}
	
	
}
