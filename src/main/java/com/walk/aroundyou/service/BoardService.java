package com.walk.aroundyou.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.repository.BoardRepository;

public class BoardService {

	private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 게시글 생성
    public Board createBoard(String title, String content, Timestamp createdAt) {
        Board board = new Board();
        board.setBoardTitle(title);
        board.setBoardContent(content);
        board.setBoardCreatedDate(createdAt);
        return boardRepository.save(board);
    }
    
    // 비공개 키
    @Transactional
    public Long PostAsPrivate(Long boardId) {
        return boardRepository.PostAsPrivate(boardId);
    }
}
