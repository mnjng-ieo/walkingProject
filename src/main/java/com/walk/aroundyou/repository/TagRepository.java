package com.walk.aroundyou.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.Tag;

import jakarta.transaction.Transactional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{
	
	// 1. 기존 태그 tag 테이블에서 삭제하기(게시물 삭제로 더 이상 존재하지 않는 tagContent)
	// (기본 메서드도 사용 가능)
	@Modifying
	@Transactional
	void deleteByTagId(Long TagId); // 기본 메소드
	// 작동하는 삭제 쿼리문이나 기본 메서드를 사용하여 주석처리
	// 직접 쿼리문 보내기
//	@Query(value = 
//		"DELETE FROM tag WHERE tag_id = ?1", 
//		nativeQuery = true)
//	void deleteByTagId(Long tagId);
	
	// 2. 새로운 태그 tag 테이블에 추가하기(중복 값도 저장되니 새로운 태그인지 확인 후 사용)
	@Modifying
	@Transactional
	// 외래키 객체를 사용하는 방법(DTO 객체를 만들어서 사용하기)
	@Query(value = 
		"INSERT INTO tag(tag_content) VALUES( :#{#tagContent})", 
		nativeQuery = true)
	void saveTag(@Param("tagContent") String tagContent);
	
	// 3. 게시물 하나에 작성된 해시태그를 조회
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
	
	// 4. 태그를 테이블에 저장할 때 존재하는 태그인지 조회하고 저장하기
//	@Query(value = "SELECT t.*"
//			+ "    FROM tag t "
//			+ "    WHERE t.tag_content = :#{#tagContent}", 
//			nativeQuery = true)
//	Optional<Tag> findByTagContent(@Param("tagContent") String tagContent);
	
	// 4. 해시태그가 있는지 확인하는 메서드(기본메소드 활용, 상단의 주석 처리한 메소드와 같은 역할)
	boolean existsByTagContent(String tagContent);

}
