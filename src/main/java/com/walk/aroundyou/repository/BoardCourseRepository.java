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
public interface BoardCourseRepository extends JpaRepository<BoardCourse, Long>{

	// Board의 식별번호와 Course의 식별번호를 연결해 특정 산책로 조회
	@Query(value = "SELECT b.* FROM board b WHERE IN (SELECT bc.boardId FROM BoardCousre bc WHERE bc.courseId =: courseId)")
	List<Board> findByCourseId(@Param(value = "courseId") Course cousreId);
	
}
