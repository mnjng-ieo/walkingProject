package com.walk.aroundyou.dto;

import java.sql.Time;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

// 산책로 목록 조회에 대한 응답을 위한 DTO
// (좋아요 수, 게시물 언급 수, 댓글 수 포함 전송)
// 코스 리포지토리에서 쓸 수 있게 인터페이스로 새로 만들었다.
// jpa 쿼리문에서 인식하도록 쿼리문 작성 시 as로 별칭주기!
public interface ICourseResponseDTO {
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
	
	// 이 어노테이션 없으면 Json으로 조회했을 때 ISO 8601 형식으로 보이는 문제 발생
	// "HH:mm:ss" 형식으로 바꾸고, timezone 설정으로 원래 데이터와 15시간 차이나던 것 바로 잡았다.
	@JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
	Time getCoursTimeCn();
	
	String getToiletDc();
	String getCvntlNm();
	String getLnmAddr();
	Double getCoursSpotLa();
	Double getCoursSpotLo();
	
	// [뷰 추가 정보]
	Long getCoursViewCount();
	Integer getLikeCnt();     // 좋아요 수
	Integer getMentionCnt();  // 게시물 언급 수
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