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
		
		if (keyword != null) { // 검색어가 있으면 공백 제거
			keyword = '%' + keyword.replace(" ", "") + '%';
		} else { // 검색어가 없으면 전체 내용을 가져옴
			keyword = "%";
		}
		return (List<ICourseResponseDTO>) courseRepository.findMainCourseByKeyword(keyword);
	}
	
	/**
	 * [메인페이지] 검색창으로 게시물 정보 조회
	 */
	public List<IBoardListResponse> findBoardByKeyword(String keyword){

		if (keyword != null) { // 검색어가 있으면 공백 제거
			keyword = '%' + keyword.replace(" ", "") + '%';
		} else { // 검색어가 없으면 전체 내용을 가져옴
			keyword = "%";
		}
		//PageRequest pageRequest = PageRequest.of(PAGE, sizeOfPage);
		return (List<IBoardListResponse>) boardRepository.findMainBoardByKeyword(keyword);
	}
	
	/**
	 * [메인페이지] 검색창으로 해시태그 정보 조회
	 */
	public List<ITagResponse> findTagByKeyword(String keyword){		
		
		if (keyword != null) { // 검색어가 있으면 공백 제거
			keyword = '%' + keyword.replace(" ", "") + '%';
		} else { // 검색어가 없으면 전체 내용을 가져옴
			keyword = "%";
		}
		return (List<ITagResponse>) tagRepository.findMainTagByKeyword(keyword);
	}

	// 개수 출력을 위한 메서드
	public int countCourseResults(String keyword) {
		if (keyword != null) { // 검색어가 있으면 공백 제거
			keyword = '%' + keyword.replace(" ", "") + '%';
		} else { // 검색어가 없으면 전체 내용을 가져옴
			keyword = "%";
		}
		return courseRepository.countCourseResults(keyword);
	}
	// 개수 출력을 위한 메서드
	public int countBoardResults(String keyword) {
		if (keyword != null) { // 검색어가 있으면 공백 제거
			keyword = '%' + keyword.replace(" ", "") + '%';
		} else { // 검색어가 없으면 전체 내용을 가져옴
			keyword = "%";
		}
		return boardRepository.countBoardResults(keyword);
	}
	
	
}