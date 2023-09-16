package com.walk.aroundyou.controller;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardCourse;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.UploadImage;
import com.walk.aroundyou.dto.BoardRequest;
import com.walk.aroundyou.service.BoardCourseService;
import com.walk.aroundyou.service.BoardService;
import com.walk.aroundyou.service.TagService;
import com.walk.aroundyou.service.UploadImageService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BoardRestController {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private TagService tagService;

	@Autowired
	private BoardCourseService boardCourseService;
	
	@Autowired
	private UploadImageService uploadImageService;
	

	@PutMapping("/api/board-editor")
	public ResponseEntity<Board> creatBoard(
			@RequestPart(value = "dto") BoardRequest board,
			@RequestPart(value = "files", required=false)
			List<MultipartFile> files,
			@AuthenticationPrincipal User user) 
					throws IllegalStateException, IOException {
//		// 게시물 등록
		log.info("게시물 등록 시작");
		board.setUserId(user.getUsername());
		Optional<Long> result = boardService.save(board);
		
		if(result.isPresent()){
			log.info("저장 결과가 있어요");	
			Long boardId = result.get();
			
			// 해시태그 추가 처리
			// 등록된 board_id로 해시태그 이력 추가
			List<String> createTagList = tagService.createTagList(boardId);
	        log.info("createList : {}", createTagList);
	        // for문 사용하여 태그 하나씩 저장 
	        for (String tagContent : createTagList) {
	        	tagService.saveBoardTag(boardId, tagContent);
	        }
	        log.info("해시태그 추가 처리 완료");
	        
	        // 산책로 추가 처리
	        if(board.getCourseId() != null) {
		        boardCourseService.save(
		        		BoardCourse.builder()
		        		.boardId(Board.builder()
		        				.boardId(boardId).build())
		        		.courseId(Course.builder()
		        				.courseId(board.getCourseId()).build())
		        		.build());
		        log.info("산책로 추가 처리 완료");
	        }
	        // 이미지 추가 처리 (0912 - 지수 작성)
	        Board savingBoard = boardService.findById(boardId).get();
	        
	        if(files != null && !files.isEmpty()) {
	        	List<UploadImage> uploadImages = 
	        			uploadImageService.saveBoardImages(files, savingBoard);
	        	log.info("게시물의 이미지 업로드 처리 완료");
	        }
	        
	        log.info("저장이 성공하였습니다.");
			//return "저장이 성공하였습니다";
	        return ResponseEntity.status(HttpStatus.CREATED)
	        		.header("boardId", boardId.toString())
	        		.body(savingBoard);
		} else {
			log.info("저장 결과가 없어요");	
			//return "저장이 실패하였습니다";
			throw new RuntimeException("저장이 실패하였습니다.");
		}
	}
	
////게시판 수정
	@PutMapping("/api/board-editor/{id}")
	public ResponseEntity<Board> updateBoard(
			@PathVariable Long id, 
			@RequestPart(value = "dto") BoardRequest board,
			@RequestPart(value = "files", required=false)
			List<MultipartFile> files,
			@RequestParam("ifNewImageExists") int ifNewImageExists,
			@AuthenticationPrincipal User user) throws IOException {
		
		board.setBoardId(id);
		board.setUserId(user.getUsername());
		
		// 이미지 수정 처리 - 0912 지수 작성
		// 이미지를 수정할 때는 우선 기존 이미지를 삭제하고 다시 저장하는 순서를 겪는다.
		// 먼저 기존 이미지 정보와 이미지 파일이 새로 업로드 되었는지 확인
		Board existedBoard = boardService.findById(id).get();
		List<UploadImage> existedImages = 
				uploadImageService.findByBoard(existedBoard);
		
		// 수정페이지에서 최종 업로드 취소 상태로 수정 요청했을 시
		if(ifNewImageExists == 0) {
			for (UploadImage existedImage : existedImages) {
				uploadImageService.deleteImage(existedImage);
			}
		}
		
		/// 수정된 게시물!!!!
		if(boardService.update(board)) {
			
			// 해시태그 수정 처리
			// board_id의 해시태그 이력 일괄 삭제
			tagService.deleteByBoardId(id);
	        log.info("해시태그 삭제 처리 완료");
			// 등록된 board_id로 해시태그 이력 추가
			tagService.deleteByBoardId(id);
			List<String> createTagList = tagService.createTagList(id);
	        log.info("createList : {}", createTagList);
	        // for문 사용하여 태그 하나씩 저장 
	        for (String tagContent : createTagList) {
	        	tagService.saveBoardTag(id, tagContent);
	        }
	        log.info("해시태그 추가 처리 완료");
	        
	        // 산책로 삭제 처리
	        boardCourseService.deleteByBoardId(id);
	        
	        
	        // 산책로 추가 처리
	        if(board.getCourseId() != null) {
	        	boardCourseService.save(BoardCourse.builder()
		        		.boardId(Board.builder().boardId(id).build())
		        		.courseId(Course.builder().courseId(board.getCourseId()).build())
		        		.build());
		        log.info("산책로 추가 처리 완료");
	        }
	        // 이미지 추가 처리
	        Board updatedBoard = boardService.findById(id).get();
	        
	        if(files != null && !files.isEmpty()) {
	        	log.info("이미지의 수정이 있는 경우입니다.");
	        	if(existedImages != null && !existedImages.isEmpty()) {
	        		log.info("기존 이미지는 삭제합니다.");
	        		for (UploadImage existedImage : existedImages) {
	        			uploadImageService.deleteImage(existedImage);
	        		}
	        	} else {
	        		log.info("기존 이미지가 없으므로 삭제 처리 없이 업로드됩니다.");
	        	}
	        	// 이미지 업로드 로직
	        	List<UploadImage> uploadImages = 
	        			uploadImageService.saveBoardImages(files, updatedBoard);
	        } else {
	        	log.info("이미지의 수정이 없는 경우입니다.");
	        	if(existedImages != null && !existedImages.isEmpty()) {
	        		log.info("기존 이미지를 유지합니다.");
	        	}
	        }
			//return "수정이 성공하였습니다";
	        return ResponseEntity.ok()
	        		.header("boardId", id.toString())
	        		.body(updatedBoard);
		} else {
			//return "수정이 실패하였습니다";
			throw new RuntimeException("수정에 실패하였습니다.");
		}
	}
	
	
	//// 게시판 삭제
	//// 편의상 임시적으로 게시물 생성에 만든 json을 사용하기 위해 데이터형을 사용
	//// 나중에 Long으로 변경해도 됨
//	@DeleteMapping("/api/board-editor")
//	public Object deleteBoard(@RequestBody BoardRequest board) throws IOException {
//		log.info("board의 id : {}", board.toUpdateEntity().getBoardId());
//		
//		// 이미지 삭제 (이미지 정보는 자연히 삭제되지만 서버 파일 삭제를 위해) - 0913 지수 작성
//		Board deletingBoard = boardService.findById(board.toUpdateEntity().getBoardId()).get();
//		List<UploadImage> existedImages = uploadImageService.findByBoard(deletingBoard);
//		if (existedImages != null && !existedImages.isEmpty()) {
//			for(UploadImage existedImage : existedImages) {
//				uploadImageService.deleteImage(existedImage);
//			}
//		}
//		
//		if(boardService.deleteById(board.toUpdateEntity().getBoardId())){
//			return "삭제가 성공하였습니다";
//		} else {
//			return "삭제가 실패하였습니다";
//		}
//		
//	}
}