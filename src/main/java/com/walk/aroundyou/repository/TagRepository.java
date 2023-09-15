package com.walk.aroundyou.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.dto.ITagResponse;

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
	
	// 2. 새로운 태그 tag 테이블에 추가하기
	@Modifying
	@Transactional
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
	// boardId를 파라미터로 받아 해당 게시물에 해당하는 모든 해시태그를 반환
	List<String> findTagsByBoardId(@Param("boardId") Long boardId);
	
	// 4. 해시태그가 있는지 확인하는 메서드
//	@Query(value = "SELECT t.*"
//			+ "    FROM tag t "
//			+ "    WHERE t.tag_content = :#{#tagContent}", 
//			nativeQuery = true)
//	Optional<Tag> findByTagContent(@Param("tagContent") String tagContent);
	
	// 4. 해시태그가 있는지 확인하는 메서드(tagContent로 tagId 조회하는 쿼리)
	boolean existsByTagContent(String tagContent);
	
	// 4. tag_content으로 tag_id 추출하기(save메서드, searchBoardAndCnt(컨트롤러)에서 활용)
	@Query(value = "SELECT"
			+ " *"
			+ " FROM tag"
			+ " WHERE tag_content = :tagContent", nativeQuery = true)
	Tag findIdByTagContent(@Param("tagContent")String tagContent);
	
	// 5. 동일한 tag_id를 가진 board_tag_id의 tag_content 출력
	// 메인화면에 지금 핫한 해시태그에 출력될 내용
	@Query(value = "SELECT t.tag_content"
			+ "    FROM tag t"
			+ "    join board_tag bt"
			+ "        on t.tag_id = bt.tag_id"
			+ "    WHERE t.tag_id"
			+ "    GROUP BY bt.tag_id"
			+ "    ORDER BY COUNT(bt.tag_id) desc"
			+ "    limit 12"
			, nativeQuery = true)
	List<String> findTagsByBoardTagId();
	
	/**
	 * [메인페이지] 검색창에 태그 정보 검색하기 - 서비스에서 매개변수 앞뒤로 '%' 붙이기!
	 */
	@Query(value = """
			SELECT 
				t.tag_id as tagId
				,t.tag_content as tagContent
			 FROM tag as t
	          WHERE REPLACE(tag_content, ' ', '') like :#{#keyword}
			""", nativeQuery = true)
	List<ITagResponse> findMainTagByKeyword(@Param("keyword") String keyword);
	
	// 검색 결과 태그이력테이블에 사용되는 tag_id만 출력
	@Query(value = "SELECT tag_id"
			+ " FROM tag t"
			+ " WHERE t.tag_id IN (SELECT DISTINCT bt.tag_id FROM board_tag bt)"
			, nativeQuery = true)
	List<Long> existsByBoardTag();

}
