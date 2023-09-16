package com.walk.aroundyou.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domain.UploadImage;
import com.walk.aroundyou.dto.IBoardDetailResponse;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.dto.ICommentResponseDto;
import com.walk.aroundyou.service.BoardService;
import com.walk.aroundyou.service.CommentService;
import com.walk.aroundyou.service.CourseService;
import com.walk.aroundyou.service.TagService;
import com.walk.aroundyou.service.UploadImageService;
import com.walk.aroundyou.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BoardViewController {
	
	
	@Autowired
	private TagService tagService;

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CommentService commentService;	
	
	@Autowired
	private CourseService courseService;	
	
	@Autowired
	private UploadImageService uploadImageService;
	
	@Autowired
	private UserService userService;
	
	// 페이지네이션 사이즈
	private final static int PAGINATION_SIZE = 5;
	
	// 게시물 리스트(게시판 타입 선택 가능)
	@GetMapping("/board")
	public String getBoard(
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "type", required = false) String type, 
			@RequestParam(name = "sort", required = false) String sort,
			@RequestParam(value = "page", required=false, defaultValue="0") int page, 
			Model model) {
		
		// Page 객체 선언
		// 타입 선택 여부에 따라 실행되는 함수가 달라지기 때문에 객체만 선언
		Page<IBoardListResponse> boardList;
		
		// type 설정
		if(type == null) {
			boardList = boardService.findBoardAndCntByKeyword(keyword, page, sort);
		} else {
			boardList = boardService.findBoardAndCntByKeywordAndType(type, keyword, page, sort);
		}
		model.addAttribute("boardList", boardList);
		
		log.info("리스트가 비어있나요? : {}", boardList.isEmpty());
		log.info("키워드는? : {}", keyword);
		
		// pagination 설정
		int totalPages = boardList.getTotalPages();
		int pageStart = getPageStart(page, totalPages);
		int pageEnd = 
				(PAGINATION_SIZE < totalPages)? 
						pageStart + PAGINATION_SIZE - 1
						:totalPages;
		
		model.addAttribute("lastPage", totalPages);
		model.addAttribute("currentPage", page + 1);
		model.addAttribute("pageStart", pageStart);
		model.addAttribute("pageEnd", pageEnd);
		model.addAttribute("sort", sort);
		model.addAttribute("keyword", keyword);
		model.addAttribute("type", type);
		
		// 게시물, 작성자 이미지 경로 넘기기 - 0913/0914 지수 작성
		// for 문을 돌려서 리스트 항목 각각의 이미지 경로를 얻어보자.
		List<String> boardsImagePaths = new ArrayList<>();
		List<String> writerMemberImagePaths = new ArrayList<>(); 
		
		for (IBoardListResponse boardResponseDTO : boardList.getContent()) {
			Board board = boardService.findById(boardResponseDTO.getBoardId()).get();
			if (board != null) {
				// 게시물 사진들 불러오기
				List<UploadImage> uploadImages = uploadImageService.findByBoard(board);
				if(uploadImages != null && !uploadImages.isEmpty()) {
					UploadImage uploadImage = uploadImages.get(0);
					String imagePath = 
							uploadImageService.findBoardFullPathById(uploadImage.getFileId());
					boardsImagePaths.add(imagePath);
				} else {
					boardsImagePaths.add("");
				}
				
				// 게시물 각 작성자 프로필 사진 붙이기
				UploadImage writerMemberImage = 
						uploadImageService.findByUser(board.getUserId());
				String writerMemberImagePath = (writerMemberImage != null) ? 
						uploadImageService.
						findUserFullPathById(writerMemberImage.getFileId()) : 
							"/images/defaultUserImage.png";
				
				writerMemberImagePaths.add(writerMemberImagePath);
			}
			
			model.addAttribute("writerMemberImagePaths", writerMemberImagePaths);
			model.addAttribute("boardsImagePaths", boardsImagePaths); 
		}
		
		return "boardList";
	}
	
	// 게시물 검색 결과 화면
	@GetMapping("/board/search")
	public String getSearchBoard(
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "boardType", defaultValue = "ALL") String type,
	        @RequestParam(name = "searchType", defaultValue = "boardTitleAndContent") String searchType,
			@RequestParam(name = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue="0") int page, 
			Model model) {
		
		// Page 객체 선언
		Page<IBoardListResponse> boardList;
				
		// type 설정
		if(type != null && !type.equals("ALL")) {
			boardList = boardService.findBoardAndCntByKeywordAndTypeAndSearchType(type, searchType, keyword, page, sort);
		} else {
			boardList = boardService.findBoardAndCntByKeywordAndSearchType(keyword, searchType, page, sort);
		}
		model.addAttribute("boardList", boardList);
		
		log.info("리스트가 비어있나요? : {}", boardList.isEmpty());
		log.info("키워드는? : {}", keyword);
		
		// pagination 설정
		int totalPages = boardList.getTotalPages();
		int pageStart = getPageStart(page, totalPages);
		int pageEnd = 
				(PAGINATION_SIZE < totalPages)? 
						pageStart + PAGINATION_SIZE - 1
						:totalPages;
		
		model.addAttribute("lastPage", totalPages);
		model.addAttribute("currentPage", page + 1);
		model.addAttribute("pageStart", pageStart);
		model.addAttribute("pageEnd", pageEnd);
		model.addAttribute("sort", sort);
		model.addAttribute("boardList", boardList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("selectedBoardType", type);
        model.addAttribute("selectedSearchType", searchType);
		
        	// 게시물, 작성자 이미지 경로 넘기기 - 0913/0914 지수 작성
     		// for 문을 돌려서 리스트 항목 각각의 이미지 경로를 얻어보자.
     		List<String> boardsImagePaths = new ArrayList<>();
     		List<String> writerMemberImagePaths = new ArrayList<>(); 
     		
     		for (IBoardListResponse boardResponseDTO : boardList.getContent()) {
     			Board board = boardService.findById(boardResponseDTO.getBoardId()).get();
     			if (board != null) {
     				// 게시물 사진들 불러오기
     				List<UploadImage> uploadImages = uploadImageService.findByBoard(board);
     				if(uploadImages != null && !uploadImages.isEmpty()) {
     					UploadImage uploadImage = uploadImages.get(0);
     					String imagePath = 
     							uploadImageService.findBoardFullPathById(uploadImage.getFileId());
     					boardsImagePaths.add(imagePath);
     				} else {
     					boardsImagePaths.add("");
     				}
     				
     				// 게시물 각 작성자 프로필 사진 붙이기
     				UploadImage writerMemberImage = 
     						uploadImageService.findByUser(board.getUserId());
     				String writerMemberImagePath = (writerMemberImage != null) ? 
     						uploadImageService.
     						findUserFullPathById(writerMemberImage.getFileId()) : 
     							"/images/defaultUserImage.png";
     				
     				writerMemberImagePaths.add(writerMemberImagePath);
     			}
     			
     			model.addAttribute("writerMemberImagePaths", writerMemberImagePaths);
     			model.addAttribute("boardsImagePaths", boardsImagePaths); 
     		}
        
		return "boardConditionList";
	}
	
	// 게시물 조회(09/09 - 연서 수정)
	@GetMapping("/board/{id}")
	public String getBoardDetail(
			@PathVariable Long id, 
			@AuthenticationPrincipal User user,
			Model model) {
		// 게시글 내용 불러오기
		Optional<IBoardDetailResponse> board = boardService.findBoardDetail(id);
		log.info("board가 있나요? : {}", board.isPresent());
		model.addAttribute("board", board.get());
		
		// 해시태그 리스트 불러오기(값 없을 때 체크)
		List<String> boardTagList = 
			tagService.findTagsByBoardId(id);
		if(!boardTagList.isEmpty()) {		
			log.info("tag 값이 있음");			
			model.addAttribute("boardTagList", boardTagList);
		}
		
		// 댓글 리스트 불러오기(값 없을 때 체크)
		List<ICommentResponseDto> comments = commentService.findByBoardId(id);
		
		if(!comments.isEmpty()) {			
			log.info("comment 값이 있음");	
			model.addAttribute("comments", comments);
		}
		
		// 산책로 정보 불러오기
		Optional<Course> course = courseService.findByBoardId(id);
		if(course.isPresent()) {			
			log.info("course 값이 있음");
			model.addAttribute("course", course.get());
			
			// ★★★★★★★ 지도 이미지 경로 넘기기 - 0916 지수 작성
			UploadImage courseUploadImage = uploadImageService.findByCourse(course.get());
			String courseImagePath;
			if (courseUploadImage != null) {
				courseImagePath = 
						uploadImageService.findCourseFullPathById(
								courseUploadImage.getFileId());
				log.info("courseImagePath : " + courseImagePath);
				
				model.addAttribute("courseImagePath", courseImagePath);
				
				String savedImageName = courseUploadImage.getSavedFileName();
				model.addAttribute("savedImageName",savedImageName);
			}
		} else {
			log.info("course 값이 없음");	
		}
		
		// 이미지 경로 넘기기 - 0913 지수 작성
		// for 문을 돌려서 리스트 항목 각각의 이미지 경로를 얻어보자.
		Board existedBoard = boardService.findById(id).get();
		List<UploadImage> uploadImages = uploadImageService.findByBoard(existedBoard);
		List<String> imagePaths = new ArrayList<>();
		if(uploadImages != null && !uploadImages.isEmpty()) {
			imagePaths = uploadImageService.findBoardFullPathsById(uploadImages);
			model.addAttribute("imagePaths", imagePaths);
		}
		
		// 게시물 작성자의 이미지, 접속자(댓글작성)의 이미지, 댓글 목록에서 유저 이미지 넘기기 - 0914 지수 작성
		// 매개변수에 @AuthenticationPrincipal User user 추가!! 
		// 1. 게시물 작성자의 이미지
		Member boardWriter = existedBoard.getUserId();
		UploadImage boardWriterImage = uploadImageService.findByUser(boardWriter);
		if (boardWriterImage != null) {
			String boardWriterImagePath = 
					uploadImageService.findUserFullPathById(boardWriterImage.getFileId());
			model.addAttribute("boardWriterImagePath", boardWriterImagePath);
		}
		
		// 2. 접속자(댓글작성)의 이미지
		if (user != null) {
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
		
		// 3. 댓글 목록(comments) 유저 이미지
		List<String> commentMemberImagePaths = new ArrayList<>();
		/// 해당 댓글 목록이 비어있지 않다면 사용자의 목록을 구해오고, 그 순서대로 이미지의 리스트를 얻자...
		if (comments != null && !comments.isEmpty()) {
			for(ICommentResponseDto comment : comments) {
				Member commentMember = userService.findByUserId(comment.getUserId()).get();
				UploadImage commentMemberImage = uploadImageService.findByUser(commentMember);
				if (commentMemberImage != null) {
					String commentMemberImagePath = 
							uploadImageService.findUserFullPathById(commentMemberImage.getFileId());
					commentMemberImagePaths.add(commentMemberImagePath);
				} else {
					commentMemberImagePaths.add("/images/defaultUserImage.png");
				}
			model.addAttribute("commentMemberImagePaths", commentMemberImagePaths);
			} 
		} 
		
		log.info("컨트롤러 끝");
		return "boardDetail";
	}
	
	// 게시물 삭제
	@PostMapping("/board/{id}")
	public String getBoardDelete(@PathVariable Long id, Model model) {
		boardService.deleteById(id);
		return "redirect:/board";
	}
	
	// 게시물 작성 폼
	@GetMapping("/board-editor")
	public String getBoardForm(Model model, Optional<Long> course) {
		// 산책로 지역 선택 항목 가져오기
		List<String> allSignguCn = courseService.findAllSignguCn();
		model.addAttribute("allSignguCn", allSignguCn);
		if(course.isPresent()) {
			Course resultCourse = courseService.findById(course.get());
			model.addAttribute("courseId", resultCourse.getCourseId());
			model.addAttribute("wlkCoursFlagNm", resultCourse.getWlkCoursFlagNm());
			model.addAttribute("wlkCoursNm", resultCourse.getWlkCoursNm());
			
			// ★★★★★★★ 지도 이미지 경로 넘기기 - 0916 지수 작성
			UploadImage courseUploadImage = uploadImageService.findByCourse(resultCourse);
			String courseImagePath;
			if (courseUploadImage != null) {
				courseImagePath = 
						uploadImageService.findCourseFullPathById(
								courseUploadImage.getFileId());
				log.info("courseImagePath : " + courseImagePath);
				model.addAttribute("courseImagePath", courseImagePath);
			}
		}
		
		return "boardForm";
	}
	
	// 게시물 수정 폼
	@GetMapping("/board-editor/{id}")
	public String getBoardFormById(@PathVariable Long id, Model model) {
		Optional<IBoardDetailResponse> board = boardService.findBoardDetail(id);
		model.addAttribute("board", board.get());
		
		// 이미지 경로 넘기기 - 0913 지수 작성
		// for 문을 돌려서 리스트 항목 각각의 이미지 경로를 얻어보자.
		Board existedBoard = boardService.findById(id).get();
		List<UploadImage> uploadImages = uploadImageService.findByBoard(existedBoard);
		List<String> ImagePaths = new ArrayList<>();
		if(uploadImages != null && !uploadImages.isEmpty()) {
			ImagePaths = uploadImageService.findBoardFullPathsById(uploadImages);
			model.addAttribute("imagePaths", ImagePaths);
		}
		
		List<String> allSignguCn = courseService.findAllSignguCn();
		model.addAttribute("allSignguCn", allSignguCn);
		Optional<Course> course = courseService.findByBoardId(id);
		if(course.isPresent()) {
			log.info("course 값이 있음");
			Course resultCourse = course.get();
			model.addAttribute("courseId", resultCourse.getCourseId());
			model.addAttribute("wlkCoursFlagNm", resultCourse.getWlkCoursFlagNm());
			model.addAttribute("wlkCoursNm", resultCourse.getWlkCoursNm());
			
			// ★★★★★★★ 지도 이미지 경로 넘기기 - 0916 지수 작성
			UploadImage courseUploadImage = uploadImageService.findByCourse(course.get());
			String courseImagePath;
			if (courseUploadImage != null) {
				courseImagePath = 
						uploadImageService.findCourseFullPathById(
								courseUploadImage.getFileId());
				log.info("courseImagePath : " + courseImagePath);
				model.addAttribute("courseImagePath", courseImagePath);
			}
		} else {
			log.info("course 값이 없음");	
		}
		if(course.isPresent()) {
		}
		return "boardFormById";
	}
	
	
	// 하나의 게시물에 포함된 해시태그 리스트 출력하기
	@GetMapping("/tagList/{boardId}")
	// @PathVariable 어노테이션을 사용하여 URL에서 추출한 boardId를 파라미터로 전달
	public String tagListInBoardContent(@PathVariable Long boardId, Model model) {
		log.info("/tagList/{boardId} 접근 .... ");
		// 존재하지 않는 boardId를 조회할때도 대비하기, 아직 구현하지 않음
		Optional<Board> boardContent = boardService.findById(boardId);
		List<String> boardTagList = 
			tagService.findTagsByBoardId(boardId);
		model.addAttribute("boardContent", boardContent.get().getBoardContent());
		model.addAttribute("boardTagList", boardTagList);
		return "boardTag";
	}	
	
	
	// 페이지네이션 시작 페이지를 계산해주는 컨트롤러
	private int getPageStart(int currentPage, int totalPages) {
		log.info("currentPage = {}, totalPages = {}", currentPage, totalPages);
		int result = 1;
		if(totalPages < currentPage + (int)Math.ceil((double)PAGINATION_SIZE/2)) {
			log.info("if문 통과");
			result = totalPages - PAGINATION_SIZE + 1;
		}else if(currentPage > (int)Math.floor((double)PAGINATION_SIZE/2)) {
			result = currentPage - (int)Math.floor((double)PAGINATION_SIZE/2) + 1;
			log.info("else if문 통과");
		}
		
		return result;
	}
}