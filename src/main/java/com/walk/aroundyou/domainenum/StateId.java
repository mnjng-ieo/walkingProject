package com.walk.aroundyou.domainenum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
@Getter
@ToString
@AllArgsConstructor
public enum StateId {
	NORMAL("이상없음"),
	NEED_TO_CHECK("문제검토"),
	NEED_TO_DELETE("삭제예정"),
	SELF_WITHDRAW("회원탈퇴"),
	FORCE_WITHDRAW("회원강퇴"),
	ETC("기타");
	
	private final String kor;
	
	//// 아래 코드는 ENUM 클래스에 빨간줄이 나오면 작성해야할 코드입니다
	//// 주석을 지우고 사용하세요
	// @AllArgsConstructor가 자동 생성해주는 코드
//	StateId(String kor){
//		this.kor = kor;
//	}
	// @Getter가 자동으로 만들어주는 코드
//	public String getKor() {
//		return this.kor;
//	}
}