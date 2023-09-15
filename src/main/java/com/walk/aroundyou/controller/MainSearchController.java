package com.walk.aroundyou.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domain.UploadImage;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.dto.ICourseResponseDTO;
import com.walk.aroundyou.dto.ITagResponse;
import com.walk.aroundyou.repository.TagRepository;
import com.walk.aroundyou.service.MainSearchService;
import com.walk.aroundyou.service.UploadImageService;
import com.walk.aroundyou.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainSearchController {

	@Autowired
	private MainSearchService mainSearchService;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UploadImageService uploadImageService;
	
	@GetMapping("/search")
	public String mainSearch(
			@RequestParam("keyword" ) String keyword,
			Model model, 
			@AuthenticationPrincipal User user) {
		// 헤더에 정보 추가하기 위한 코드
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
			Member currentUser = userService.findByUserId(user.getUsername()).get();
			if (currentUser != null) {
				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
				if (currentUserImage != null) {
					String currentUserImagePath = 
							uploadImageService.findUserFullPathById(currentUserImage.getFileId());
					model.addAttribute("currentUserImagePath", currentUserImagePath);
				}
			}
		}
		// 검색 서비스로부터 검색 결과를 가져온다 
		// 1-1. 태그
		List<ITagResponse> tagResults = 
				mainSearchService.findTagByKeyword(keyword);
		
		// 1-2. board_tag 테이블에 있는 tag_id만 남기기
	    List<Long> tagIdsInBoardTag = tagRepository.existsByBoardTag();
	    tagResults = tagResults.stream()
	            .filter(tag -> tagIdsInBoardTag.contains(tag.getTagId()))
	            .collect(Collectors.toList());
		
		// 2. 코스
		List<ICourseResponseDTO> courseResults = 
				mainSearchService.findCourseByKeyword(keyword);
		if (courseResults.size() > 5) { // 다섯개 이상이면 
		    courseResults = courseResults.subList(0, 5); // 다섯개 까지만 표시
		}

		// 3. 게시물
		List<IBoardListResponse> boardResults = 
				mainSearchService.findBoardByKeyword(keyword);
		if (boardResults.size() > 5) {
		    boardResults = boardResults.subList(0, 5);
		}
		
		// 전체 개수 구하기
		int totalCourseResults = mainSearchService.countCourseResults(keyword);
		int totalBoardResults = mainSearchService.countBoardResults(keyword);
		
        model.addAttribute("tagResults", tagResults);
        model.addAttribute("courseResults", courseResults);
        model.addAttribute("boardResults", boardResults);
        model.addAttribute("totalCourseResults", totalCourseResults);
        model.addAttribute("totalBoardResults", totalBoardResults);
        model.addAttribute("keyword", keyword);
		
		return "search/mainSearch";
	}

}
