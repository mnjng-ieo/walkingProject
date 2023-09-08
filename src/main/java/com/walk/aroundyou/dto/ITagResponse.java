package com.walk.aroundyou.dto;

// 검색 결과를 가져오기 위한 인터페이스(DTO)
// 내가 정의한 인터페이스를 기준으로 적절한 데이터만 가져오는것을 이용하여 Repository에 있는 반환값에 매핑
public interface ITagResponse {

	Long getTagId();
	String getTagContent();

}
