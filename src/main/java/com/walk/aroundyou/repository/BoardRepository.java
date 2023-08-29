package com.walk.aroundyou.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.walk.aroundyou.domain.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
		
	// 게시글 비공개 처리
	@Modifying
    @Transactional
    @Query("UPDATE Board b SET b.boardSecret = false WHERE b.boardId = :BoardId")
    Long PostAsPrivate(Long BoardId);
}
