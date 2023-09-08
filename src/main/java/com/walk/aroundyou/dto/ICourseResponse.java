package com.walk.aroundyou.dto;

import java.sql.Time;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.walk.aroundyou.domain.Course;

import lombok.Getter;
import lombok.Setter;

// 메인페이지 검색 응답을 위한 DTO 클래스 
// (좋아요 수, 게시물 언급 수, 댓글 수 포함 전송)
// jpa 쿼리문에서 인식하지 못하므로 쿼리문 작성 시 as로 별칭주기
public interface ICourseResponse {

	Long getCourseId();
	String getEsntlId(); 
	String getWlkCoursFlagNm();
	String getWlkCoursNm();
	String getCoursDc();
	String getSignguCn();
	String getCoursLevelNm();
	String getCoursLtCn();
	Float getCoursDetailLtCn();
	String getAditDc();
	
	@JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
	Time getCoursTimeCn();
	
	String getToiletDc();
	String getCvntlNm();
	String getLnmAddr();
	Double getCoursSpotLa();
	Double getCoursSpotLo();
	
	// [뷰 추가 정보]
	Integer getLikeCnt();     // 좋아요 수
	Integer getMentionCnt();  // 게시글 언급 수
	Integer getCommentCnt();  // 댓글 수
	
	// 쿼리문에 작성 할 별칭
//	CourseId
//	EsntlId
//	WlkCoursFlagNm
//	WlkCoursNm
//	CoursDc
//	SignguCn
//	CoursLevelNm
//	CoursLtCn
//	CoursDetailLtCn
//	AditDc
//	CoursTimeCn
//	ToiletDc
//	CvntlNm
//	LnmAddr
//	CoursSpotLa
//	CoursSpotLo
//	LikeCnt
//	CommentCnt

}
