package com.walk.aroundyou.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.BoardTag;

import jakarta.transaction.Transactional;

@Repository
public interface BoardTagRepository extends JpaRepository<BoardTag, Long>{

	// 1. 게시물 삭제 시 board_tag 테이블에서 삭제하기(기본 메서드 사용)
	@Modifying
	@Transactional
	@Query(value = 
		"DELETE FROM board_tag WHERE board_id = ?1", 
		nativeQuery = true)
	void deleteByBoardId(Long boardId); // 게시물 id로 한번에 삭제하기
	// void deleteByBoardTagId(Long boardTagId); // 이력 id로 각각 삭제하기
	
	// 2. 새로운 게시물 작성시 board_tag 테이블에 추가하기(글 작성시 매번 실행되는 부분)
	// 게시물에 태그가 여러개 작성되는 경우 생각하기
	// PK와 FK만으로 이루어진 테이블이라 쿼리문에서 외래키는 객체로 사용
	@Modifying
	@Transactional
	// 외래키 객체를 사용하는 방법(직접 쿼리를 작성하여 사용하기)
	@Query(value = 
		"INSERT INTO board_tag(board_id, tag_id) "
		+ "VALUES "
		+ "( :#{#boardTag.boardId.boardId}, :#{#boardTag.tagId.tagId})",
		nativeQuery = true)
	void saveBoardTag(@Param("boardTag") BoardTag boardTag);
	
}