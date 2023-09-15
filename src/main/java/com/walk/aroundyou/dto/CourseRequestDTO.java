package com.walk.aroundyou.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.UploadImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
// 관리자 페이지에서 산책로 생성/수정 요청을 처리할 DTO 클래스
public class CourseRequestDTO {
	
	// id 관련 필드는 추가하지 않았다.
	private String wlkCoursFlagNm;
	private String wlkCoursNm;
	private String coursDc;
	private String signguCn;
	private String coursLevelNm;
	private String coursLtCn;
	private Float coursDetailLtCn;
	private String aditDc;
	
	@JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp coursTimeCn;
	
	private String toiletDc;
	private String cvntlNm;
	private String lnmAddr;
	private double coursSpotLa;
	private double coursSpotLo;
	//private UploadImage courseImageId;
	
	public Course toEntity() {
		return Course.builder()
				.wlkCoursFlagNm(wlkCoursFlagNm)
				.wlkCoursNm(wlkCoursNm)
				.coursDc(coursDc)
				.signguCn(signguCn)
				.coursLevelNm(coursLevelNm)
				.coursLtCn(coursLtCn)
				.coursDetailLtCn(coursDetailLtCn)
				.aditDc(aditDc)
				.coursTimeCn(coursTimeCn)
				.toiletDc(toiletDc)
				.cvntlNm(cvntlNm)
				.lnmAddr(lnmAddr)
				.coursSpotLa(coursSpotLa)
				.coursSpotLo(coursSpotLo)
				//.courseImageId(courseImageId)
				.coursViewCount(0)
				.build();
	}
}
