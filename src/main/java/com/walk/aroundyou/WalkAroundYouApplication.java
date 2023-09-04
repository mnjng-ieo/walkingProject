package com.walk.aroundyou;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.repository.CommentRepository;

@SpringBootApplication
public class WalkAroundYouApplication implements CommandLineRunner{

	private static final Logger log = 
			LoggerFactory.getLogger(WalkAroundYouApplication.class);
	
	@Autowired
	private CommentRepository commentRepo;

	
	public static void main(String[] args) {
		SpringApplication.run(WalkAroundYouApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		User createdId =  new User();
		createdId.setUserId("wayid50"); // user_id 객체 생성 
		Board boardId = new Board();
		boardId.setBoardId(10L);		// board_id 객체 생성 
		Course course = new Course();
		course.setCourseId(20L);      // course 객체 생성 
		
//		// 1. Board에서 Comment 추가(작성)		
//		Comment comment = Comment.builder()
//					.userId(createdId)
//					.userNickname("가나다라마바사")
//					.commentContent("코멘트 내용 테스트입니다.")
//					.boardId(boardId)
//					.commentType(CommentType.BOARD)
//					.build();
//		
//		commentRepo.saveCommentAsBoard(comment);
//		
//		Comment resultComment = commentRepo.findById(100L).get();
//		log.info("코멘트 작성자 : {}", resultComment.getUserId().getUserId());
//		log.info("코멘트 내용 : {}", resultComment.getCommentContent());
//		log.info("코멘트 타입 : {}", resultComment.getCommentType());	// 질문 -> CommentType.BOARD
		
		
		
		// 2. Board에서 board_id를 이용한 Comment 목록 조회 
//		Comment comment1 = Comment.builder()
//					.userId(createdId)
//					.userImg("이미지")
//					.userNickname("가나다라")
//					.boardId(boardId)
//					.commentContent("코멘트 내용 테스트입니다.")
//					.build();
//		Optional<Comment> list = commentRepo.findById(10L);
//		
//		log.info("게시판 식별번호 : {}", comment1.getBoardId());
//		log.info("사용자 이미지 : {}", comment1.getUserImg());
//		log.info("코멘트 내용 : {}", comment1.getCommentContent());
		

		
		// 3. Comment 삭제 (delete)
//		Comment comment2 = Comment.builder()
//				.userId(createdId)
//				.commentId(10L)
//				.commentContent("코멘트 삭제 테스트입니다.")
//				.build();
//		log.info("삭제될 comment 식별번호 : {}",  comment2.getCommentId());
//		log.info("삭제될 comment 내용 : {}", comment2.getCommentContent());

		
		
//		// 4. comment 수정 
//		Comment comment3 = Comment.builder()
//				.commentId(15L)
//				.commentContent("코멘트 수정 테스트입니다.")
//				.build();
//		log.info("수정될 comment 식별번호 : {}", comment3.getCommentId());
//		log.info("수정될 comment 내용 : {}", comment3.getCommentContent());
		
		
		// 5. comment 개수 조회 
		// 위에서 객체 생성했으므로 아래 주석처리된 코드 필요 없음 
//		Comment comment4 = Comment.builder()
//					.courseId(courseId)
//					.commentContent("개수 조회 테스트")
//					.commentType(CommentType.COURSE)
//					.build();
		log.info("산책로 상세페이지에서 해당 산책로에 대한 코멘트 개수 : {}", 
				commentRepo.countCourseCommentByCourseId(course));
		// 레포지토리의 메서드 매개변수 타입이 Course -> 위에서 생성한 객체를 주입하면 됨 
	}

}
