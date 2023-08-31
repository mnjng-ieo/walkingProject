package com.walk.aroundyou.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.Tag;

import jakarta.transaction.Transactional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{
	
	// 1. 새로운 태그 tag 테이블에 추가하기(중복 값도 저장되니 새로운 태그인지 확인 후 사용)
	@Modifying
	@Transactional
	@Query(value = 
		"INSERT INTO tag(tag_content) VALUES(?1)", 
		nativeQuery = true)
	void saveByTag(String tagContent);
	
	// 2. 기존 태그 tag 테이블에서 삭제하기(게시물 삭제로 더 이상 존재하지 않는 tagContent)
	// (기본 메서드도 사용 가능)
	@Modifying
	@Transactional
	@Query(value = 
		"DELETE FROM tag WHERE tag_content = ?1", 
		nativeQuery = true)
	void deleteByTag(String tagContent);
	
	// 3. 게시물 하나에 작성된 해시태그를 조회(컨트롤러 까지 확인)
	@Query(value = "SELECT t.tag_content "
		+ "	   FROM tag t"
		+ "    INNER JOIN ("
		+ "			SELECT bt.tag_id"
		+ "				FROM board_tag bt"
		+ "				JOIN board b"
		+ "        			ON bt.board_id = b.board_id"
		+ "				WHERE b.board_id = :#{#boardId}"
		+ "			) bb"
		+ "		ON bb.tag_id = t.tag_id"
		, nativeQuery = true)
	List<String> findTagsByBoardId(@Param("boardId") Long boardId);

}
