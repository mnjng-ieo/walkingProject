package com.walk.aroundyou.dto;

import org.springframework.web.multipart.MultipartFile;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domain.UploadImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
// 업로드이미지 save 요청을 처리할 DTO 클래스
// DTO는 데이터 전송 객체!
// - 데이터 전송과 관련된 필드를 캡슐화해 관리. 더 간결하고 확장 가능한 코드 작성 가능.
public class UploadImageRequestDTO {
	
	// id 관련 필드는 추가하지 않았다.
	private String originalFileName;
	private String savedFileName;
	private Integer imageOrd;
	private Board board;
	private Course course;
	private Member user;
	
	// 업로드된 파일의 정보를 담고 있는 객체
	private MultipartFile multipartFile;
	
	// 게시물, 산책로, 유저 프로필 등 경우에 따라 필요한 필드만 가져와서 build
	public UploadImage toEntity() {
		
		UploadImage.UploadImageBuilder builder = UploadImage.builder()
				.originalFileName(originalFileName)
				.savedFileName(savedFileName);
		
		if(board != null) {
			builder.board(board);
			if (imageOrd != null) {
				builder.imageOrd(imageOrd);
			}
		}
		
		if(course != null) {
			builder.course(course);
		}
		
		if(user != null) {
			builder.user(user);
		}
		
		return builder.build();
	}
}
