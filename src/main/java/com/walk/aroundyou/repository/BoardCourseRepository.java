package com.walk.aroundyou.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardCourse;
import com.walk.aroundyou.domain.Course;

import jakarta.transaction.Transactional;

@Repository
public interface BoardCourseRepository extends JpaRepository<BoardCourse, Long> {

	@Modifying
	@Transactional
	@Query(value = """
			DELETE FROM board_course
				WHERE board_id = :#{#id}
			""", nativeQuery = true)
	void deleteByBoardId(@Param("id")Long id);
	
	// Board의 식별번호와 Course의 식별번호를 연결해 특정 "보드글(게시글)" 조회
	/* 
		하나의 산책로(courseId)에 대한 
		게시글(boardId)은 여러 개가 나올 수 있으므로 List타입으로 지정해주었다. 
	*/
	// BoardRepository에서 실행해야함
//	@Query(value = "SELECT b.* "
//			+ "FROM board b "
//			+ "WHERE b.board_id IN ("
//			+ "		SELECT "
//			+ "			bc.board_id "
//			+ "		FROM board_course bc "
//			+ "		WHERE bc.course_id = :#{#courseId.courseId})"
//			, nativeQuery = true)
//	List<Board> findByCourseId(@Param(value = "courseId") Course cousreId);
//	
	
	
	// Board의 식별번호와 Course의 식별번호를 연결해 특정 "산책로" 조회
	/*
		 하나의 게시글(boardId)에 대한 
		 산책로(courseId)는 여러 개가 나올 수 있으므로(하나의 글에 여러 개의 산책로에 대해 쓸 수 있음) List타입으로 지정해주었다.
	 */

	// CourseRepository에서 실행해야함
//	@Query(value = "SELECT c.* "
//			+ "FROM Course c "
//			+ "WHERE course_id IN ("
//			+ 	"SELECT bc.course_id "
//			+ 	"FROM Board_course bc "
//			+ 	"WHERE bc.board_id = :#{#boardId.boardId})"
//			, nativeQuery = true)
//	// @Query(value = "SELECT c.* FROM Course c WHERE courseId IN (SELECT bc.courseId FROM BoardCourse bc WHERE bc.boardId = :boardId)") 와 동일
//	List<Course> findByBoardId(@Param(value = "boardId") Board boardId);
//	
}