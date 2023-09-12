package com.walk.aroundyou.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.dto.ICourseResponseDTO;
import com.walk.aroundyou.service.CourseService;
import com.walk.aroundyou.service.TagService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainViewController {
	
	private final CourseService courseService;
	private final TagService tagService;
	
	
	@GetMapping("/")
	public String getMain(Model model) {
		
		// BEST 9개 출력
		Page<ICourseResponseDTO> coursePage = courseService.findCoursesOrderByLikes();
		
		// 가장 많이 사용된 태그 메인화면에 출력하기 
		List<String> hotTagList = tagService.findTagsByBoardTagId();

		// 좋아요 순이 가장 많은 태그(=hotTagList.get(0))의 게시물 출력됨 
		List<IBoardListResponse> tagBoardList = tagService.findBoardAndCntByMainTagDefault(hotTagList.get(0));
		
		model.addAttribute("courses", coursePage);  
		model.addAttribute("hotTagList", hotTagList);
		model.addAttribute("tagBoardList", tagBoardList);

		return "main";
	}
	
	// Ajax로 처리됨 
	@GetMapping("/api/tag/board")
	public String getTagBoard(@RequestParam(name="tagContent") String tagContent, Model model) {
		// tag_content으로 tag_id 추출하기
		Tag tagId = tagService.findIdByTagContent(tagContent);
		// 위에서 추출한 tag_id로 관련된 게시물 출력
		Page<IBoardListResponse> tagBoardList = tagService.findBoardAndCntByMainTagId(tagId);
		
		model.addAttribute("tagBoardList", tagBoardList);
		model.addAttribute("tagContent", tagContent);
		
		return "tagBoardList";
	}
}