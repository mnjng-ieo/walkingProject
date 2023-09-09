package com.walk.aroundyou.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.dto.ICourseResponseDTO;
import com.walk.aroundyou.dto.ITagResponse;
import com.walk.aroundyou.repository.BoardRepository;
import com.walk.aroundyou.repository.CourseRepository;
import com.walk.aroundyou.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MainSearchService {

	// 페이징 처리에 필요한, 페이지 번호 (default로 0 부여했다.)
	private final static int PAGE = 0;
	private final CourseRepository courseRepository;
	private final BoardRepository boardRepository;
	private final TagRepository tagRepository;
	
	/**
	 * [메인페이지] 검색창으로 산책로 정보 조회
	 */
	public List<ICourseResponseDTO> findCourseByKeyword(String keyword){
		
		// 검색 키워드의 공백 제거 + SQL LIKE 연산에 쓰기 위해 앞뒤로 '%' 붙임
		String formattedKeyword = '%' + keyword.replace(" ", "") + '%';
		
		return (List<ICourseResponseDTO>) courseRepository.findMainCourseByKeyword(formattedKeyword);
	}
	
	/**
	 * [메인페이지] 검색창으로 게시물 정보 조회
	 */
	public List<IBoardListResponse> findBoardByKeyword(String keyword){
		//int sizeOfPage = 5;
		
		// 검색 키워드의 공백 제거 + SQL LIKE 연산에 쓰기 위해 앞뒤로 '%' 붙임
		String formattedKeyword = '%' + keyword.replace(" ", "") + '%';
		
		//PageRequest pageRequest = PageRequest.of(PAGE, sizeOfPage);
		return (List<IBoardListResponse>) boardRepository.findMainBoardByKeyword(formattedKeyword);
	}
	
	/**
	 * [메인페이지] 검색창으로 해시태그 정보 조회
	 */
	public List<ITagResponse> findTagByKeyword(String keyword){		
		// 검색 키워드의 공백 제거 + SQL LIKE 연산에 쓰기 위해 앞뒤로 '%' 붙임
		String formattedKeyword = '%' + keyword.replace(" ", "") + '%';
		
		return (List<ITagResponse>) tagRepository.findMainTagByKeyword(formattedKeyword);
	}
	
	
}