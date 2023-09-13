package com.walk.aroundyou.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.CourseResponseDTO;
import com.walk.aroundyou.dto.ICourseResponse;

public interface CourseRepository 
			extends JpaRepository<Course, Long>, 
					JpaSpecificationExecutor<Course> {
	
	/**
	 * 특정 컬럼 값을 검색 조건으로 조회 메소드 작성 + 산책로명으로 정렬 + 페이징 처리
	 * - 지역, 난이도, 코스거리, 소요시간 등 => findAll(Specification) 활용!
	     소요시간의 검색 조건은 1시간 이내, 1~2시간 이내, 2~3시간 이내, 3~4시간 이내, 4시간 이상으로 나눴다.
	 -> 메소드의 파라미터로 소요시간 시작과 끝 범위에 대한 "hh:mm:ss" 형식의 값 두 개 들어와야 함.
	    ex. "00:00:00" - "00:59:00"
	    
	    c.signguCn, c.coursLevelNm, c.coursLtCn, c.coursTimeCn
	    정렬 설정 : order by c.wlkCoursFlagNm ASC, c.wlkCoursNm ASC
	    
	    Specification과 Sort를 사용한 findAll 메소드 -> JPARepository에 기본 정의되어 있음.
	    public List<Course> findAll(Specification<Course> spec, Sort sort);
	    
	    - 페이징 처리는 다음과 같은 JpaRepository 기본 메소드가 지원한다.
	      Page<T> findAll(Specification<T> spec, Pageable pageable);
	 */
	
	/**
	 * 좋아요 수, 게시물언급 수, 댓글 수 계산하기
	 */
	@Query(value = """ 
			SELECT COUNT(*) FROM course_like WHERE course_id = :courseId
			""", nativeQuery = true)
	int countCourseLikesByCourseId(@Param("courseId") Long id);
	
	@Query(value = """ 
			SELECT COUNT(*) FROM board_course WHERE course_id = :courseId
			""", nativeQuery = true)
	int countCourseMentionsByCourseId(@Param("courseId") Long courseId);
	
	@Query(value = """ 
			SELECT COUNT(*) FROM comment WHERE course_id = :courseId
			""", nativeQuery = true)
	int countCourseCommentsByCourseId(@Param("courseId") Long courseId);
	
	/**
	 * 조회 수 1씩 증가하기
	 */
	@Query(value = """
			UPDATE Course c
			SET cours_view_count = cours_view_count + 1
			WHERE course_id = :courseId
			""", nativeQuery = true)
	void updateViewCount(@Param("courseId") Long courseid);
	
	/**
	 * [메인페이지] 검색창에 산책로 정보 검색하기 - 서비스에서 매개변수 앞뒤로 '%' 붙이기!
	 */
	@Query(value = """
			SELECT  
					c.course_id as CourseId
					, c.esntl_id as EsntlId
					, c.wlk_cours_flag_nm as WlkCoursFlagNm
					, c.wlk_cours_nm as WlkCoursNm
					, c.cours_dc as CoursDc
					, c.signgu_cn as SignguCn
					, c.cours_level_nm as CoursLevelNm
					, c.cours_lt_cn as CoursLtCn
					, c.cours_detail_lt_cn as CoursDetailLtCn
					, c.adit_dc as AditDc
					, c.cours_time_cn as CoursTimeCn
					, c.toilet_dc as ToiletDc
					, c.cvntl_nm as CvntlNm
					, c.lnm_addr as LnmAddr
					, c.cours_spot_la as CoursSpotLa
					, c.cours_spot_lo as CoursSpotLo
					, ifnull(comment_cnt, 0) as commentCnt
		            , ifnull(like_cnt, 0) as likeCnt
				FROM course as c
		            LEFT JOIN 
		               (select course_id, count(course_id) as comment_cnt
		                  from comment as c
		                  group by c.course_id) as cc
		            on c.course_id = cc.course_id   
		            LEFT JOIN 
		               (select course_id, count(course_id) as like_cnt
		                  from course_like as cl
		                  group by cl.course_id) as cll
				    on c.course_id = cll.course_id				
	            WHERE REPLACE(wlk_cours_flag_nm, ' ', '') like :#{#keyword}
		         or REPLACE(wlk_cours_nm, ' ', '') like :#{#keyword}
		         or REPLACE(cours_dc, ' ', '') like :#{#keyword}
		         or REPLACE(adit_dc, ' ', '') like :#{#keyword}
			""", nativeQuery = true)
	List<ICourseResponse> findMainCourseByKeyword(
			@Param("keyword") String keyword);
}
