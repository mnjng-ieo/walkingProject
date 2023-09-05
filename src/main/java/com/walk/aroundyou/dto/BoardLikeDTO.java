package com.walk.aroundyou.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


// @Data 
// DTO이지만 응답을 하기 위한 DTO 이므로, @Setter 속성 함수는 쓰이지 않음 (@Setter를 내장하고 있는 @Data 어노테이션은 주석 처리)
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardLikeDTO {
	private long boardId;
	private String userId;

}
