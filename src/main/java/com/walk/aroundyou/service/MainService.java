package com.walk.aroundyou.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.dto.CourseResponseDTO;
import com.walk.aroundyou.dto.IBoardDetailResponse;
import com.walk.aroundyou.repository.BoardRepository;
import com.walk.aroundyou.repository.CourseRepository;
import com.walk.aroundyou.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MainService {

	// 페이징 처리에 필요한, 페이지 번호 (default로 0 부여했다.)
	private final static int PAGE = 0;
	private final CourseRepository courseRepository;
	private final BoardRepository boardRepository;
	private final TagRepository tagRepository;
	
	/**
	 * [메인페이지] 검색창으로 산책로 정보 조회
	 */
	public List<CourseResponseDTO> findCourseByKeyword(String keyword){
		int sizeOfPage = 5;
		
		// 검색 키워드의 공백 제거 + SQL LIKE 연산에 쓰기 위해 앞뒤로 '%' 붙임
		String formattedKeyword = '%' + keyword.replace(" ", "") + '%';
		
		PageRequest pageRequest = PageRequest.of(PAGE, sizeOfPage);
		return (List<CourseResponseDTO>) courseRepository.findMainCourseByKeyword(formattedKeyword, pageRequest);
	}
	
	/**
	 * [메인페이지] 검색창으로 게시물 정보 조회
	 */
	public List<IBoardDetailResponse> findBoardByKeyword(String keyword){
		int sizeOfPage = 5;
		
		// 검색 키워드의 공백 제거 + SQL LIKE 연산에 쓰기 위해 앞뒤로 '%' 붙임
		String formattedKeyword = '%' + keyword.replace(" ", "") + '%';
		
		PageRequest pageRequest = PageRequest.of(PAGE, sizeOfPage);
		return (List<IBoardDetailResponse>) boardRepository.findMainCourseByKeyword(formattedKeyword, pageRequest);
	}
	
	/**
	 * [메인페이지] 검색창으로 해시태그 정보 조회
	 */
	public List<Tag> findTagByKeyword(String keyword){
		int sizeOfPage = 10;
		
		// 검색 키워드의 공백 제거 + SQL LIKE 연산에 쓰기 위해 앞뒤로 '%' 붙임
		String formattedKeyword = '%' + keyword.replace(" ", "") + '%';
		
		PageRequest pageRequest = PageRequest.of(PAGE, sizeOfPage);
		return (List<Tag>) tagRepository.findMainCourseByKeyword(formattedKeyword, pageRequest);
	}
}
