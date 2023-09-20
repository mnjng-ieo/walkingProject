package com.walk.aroundyou.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domain.UploadImage;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.dto.ICourseResponseDTO;
import com.walk.aroundyou.dto.ITagResponse;
import com.walk.aroundyou.repository.TagRepository;
import com.walk.aroundyou.service.CourseService;
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

	@Autowired
	private CourseService courseService;

	@GetMapping("/search")
	public String mainSearch(@RequestParam("keyword") String keyword, Model model, @AuthenticationPrincipal User user) {
		// 헤더에 정보 추가하기 위한 코드
		if (user != null) {
			model.addAttribute("loginId", user.getUsername());
			Member currentUser = userService.findByUserId(user.getUsername()).get();
			if (currentUser != null) {
				model.addAttribute("currentUser", currentUser);
				model.addAttribute("currentUserRole", currentUser.getRole().getRoleName());
				UploadImage currentUserImage = uploadImageService.findByUser(currentUser);
				if (currentUserImage != null) {
					String currentUserImagePath = uploadImageService.findUserFullPathById(currentUserImage.getFileId());
					model.addAttribute("currentUserImagePath", currentUserImagePath);
				}
			}
		}
		// 검색 서비스로부터 검색 결과를 가져온다
		// 1-1. 태그
		List<ITagResponse> tagResults = mainSearchService.findTagByKeyword(keyword);

		// 1-2. board_tag 테이블에 있는 tag_id만 남기기
		List<Long> tagIdsInBoardTag = tagRepository.existsByBoardTag();
		tagResults = tagResults.stream().filter(tag -> tagIdsInBoardTag.contains(tag.getTagId()))
				.collect(Collectors.toList());

		// 2. 코스
		List<ICourseResponseDTO> courseResults = mainSearchService.findCourseByKeyword(keyword);
		if (courseResults.size() > 5) { // 다섯개 이상이면
			courseResults = courseResults.subList(0, 5); // 다섯개 까지만 표시
		}
		// 산책로 이미지 경로 넘기기
		List<String> imagePaths = new ArrayList<>();
		for (ICourseResponseDTO courseResponseDTO : courseResults) {
			// UploadImage uploadImage = courseResponseDTO.getCourseImageId();
			Course course = courseService.findById(courseResponseDTO.getCourseId());
			UploadImage uploadImage = uploadImageService.findByCourse(course);
			if (uploadImage != null) {
				String imagePath = uploadImageService.findCourseFullPathById(uploadImage.getFileId());
				imagePaths.add(imagePath);
			} else {
				// 여기 기본 이미지를 어떤 걸로 해야할지 약간 고민스럽다.
				imagePaths.add("/images/defaultCourseMainImg.jpg");
			}
		}
		// 모델에 이미지 경로 리스트 추가
		model.addAttribute("imagePaths", imagePaths);

		// 3. 게시물
		List<IBoardListResponse> boardResults = mainSearchService.findBoardByKeyword(keyword);
		// ★★★ 0920 지수 추가
		List<String> writerMemberImagePaths = new ArrayList<>();

		if (boardResults.size() > 5) {
			boardResults = boardResults.subList(0, 5);
			// ★★★ 게시물 각 작성자 프로필 사진 붙이기 - 0920 작성
			for (IBoardListResponse boardDTO : boardResults) {
				// Board board = boardService.findById(boardDTO.getBoardId());
				Member writer = userService.findByUserId(boardDTO.getUserId()).get();
				UploadImage writerMemberImage = uploadImageService.findByUser(writer);
				String wirterMemberImagePath = (writerMemberImage != null)
						? uploadImageService.findUserFullPathById(writerMemberImage.getFileId())
						: "/images/defaultUserImage.png";

				writerMemberImagePaths.add(wirterMemberImagePath);
			}
			model.addAttribute("writerMemberImagePaths", writerMemberImagePaths);
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
