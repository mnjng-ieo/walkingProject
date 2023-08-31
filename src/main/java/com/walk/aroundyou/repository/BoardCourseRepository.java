package com.walk.aroundyou.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardCourse;
import com.walk.aroundyou.domain.Course;

@Repository
public interface BoardCourseRepository extends JpaRepository<BoardCourse, Long> {

	// Board의 식별번호와 Course의 식별번호를 연결해 특정 "보드글(게시글)" 조회
	/* 
		하나의 산책로(courseId)에 대한 
		게시글(boardId)은 여러 개가 나올 수 있으므로 List타입으로 지정해주었다. 
	*/
	@Query(value = "SELECT b.* "
			+ "FROM Board b "
			+ "WHERE boardId IN "
			+ "		(SELECT "
			+ "			bc.boardId "
			+ "		FROM BoardCourse bc "
			+ "		WHERE bc.courseId = :#{#courseId.courseId}")
	List<Board> findByCourseId(@Param(value = "courseId") Course cousreId);
	
	
	
	
	// Board의 식별번호와 Course의 식별번호를 연결해 특정 "산책로" 조회
	/*
		 하나의 게시글(boardId)에 대한 
		 산책로(courseId)는 여러 개가 나올 수 있으므로(하나의 글에 여러 개의 산책로에 대해 쓸 수 있음) List타입으로 지정해주었다.
	 */
	@Query(value = "SELECT c.* FROM Course c WHERE courseId IN (SELECT bc.courseId FROM BoardCourse bc WHERE bc.boardId = :#{#boardId.boardId})")
	// @Query(value = "SELECT c.* FROM Course c WHERE courseId IN (SELECT bc.courseId FROM BoardCourse bc WHERE bc.boardId = :boardId)") 와 동일
	List<Course> findByBoardId(@Param(value = "boardId") Board boardId);
	
}