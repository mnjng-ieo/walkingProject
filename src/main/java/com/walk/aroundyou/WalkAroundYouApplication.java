package com.walk.aroundyou;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.repository.BoardCourseRepository;
import com.walk.aroundyou.repository.BoardLikeRepository;
import com.walk.aroundyou.repository.BoardRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class WalkAroundYouApplication implements CommandLineRunner{

	@Autowired
	private BoardRepository boardRepo;
	@Autowired
	private BoardCourseRepository boardCourseRepo;
	@Autowired
	private BoardLikeRepository boardLikeRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(WalkAroundYouApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
//		for(Board board : boardRepo.findByBoardType(BoardType.REVIEW)) {
//			log.info(board.toString());
//		}
		
		////// 게시글 작성하는 경우
//		User createdId = new User();
//		createdId.setUserId("wayid50"); //user_id 객체 생성
//		// Long id = 301L;
//		String title = "안녕하세요 감사해요 잘있어요 다시만나요";
//		String content = "실전! 스프링 부트와 리액트로 시작하는 "
//				+ "모든 웹 애플리케이션 개발, "
//				+ "자바 웹 개발 워크북 : 성장하는 개발자를 "
//				+ "만드는 실무형 로드맵";
//		String nickname = "이히리기우구추";
//		Board board = Board.builder()
//		//		.boardId(id)
//				.boardTitle(title)
//				.boardContent(content)
//				.userNickname(nickname)
//				.userId(createdId) // 	User 객체째로 입력
//				.build();
//		
//		em.persist(board);
//		boardRepo.saveByTitleAndContentAndUserInfo(board);
//		
//		log.info("새로 생성된 id : " 
//				+ em.createNativeQuery("INSERT INTO board "
//					+ "(board_title, board_content, user_nickname, user_id, board_created_date) "
//					+ "VALUES "
//					+ "( "
//					+ ", :#{#post.boardTitle}, :#{#post.boardContent}"
//					+ ", :#{#post.userNickname}, :#{#post.userId.userId}"
//					+ ", now() ) ")
//					.getSingleResult().toString());
//
//		em.detach(em);
//		em.remove(em);
//		
//		save에서 반환값 테스트 하는 중
		
		
//		Board resultBoard = boardRepo.findById(301L).get();
//		log.info("게시물 제목 : {}",resultBoard.getBoardTitle());
//		log.info("게시물 내용 : {}",resultBoard.getBoardContent());
//		log.info("작성일시 : {}",resultBoard.getBoardUpdatedDate());
//		log.info("작성자 이름 : {}",resultBoard.getUserNickname());
//		// 출력할때는 User객체니까 User 객체의 값을 다시 불러오기
//		log.info("작성자 아이디 : {}",resultBoard.getUserId().getUserId());

		///// 게시물을 수정하는 경우
//		Board board = Board.builder()
//				.boardId(301L)
//				.boardTitle("비오는 날 산책길 걷기")
//				.boardContent("웅덩이가 많아요 장화를 신고 나오는게 좋겠어요")
//				.userNickname("아메리카노")
//				.build();
//		boardRepo.updateByTitleAndContentAndUserInfo(board);
//		
//		resultBoard = boardRepo.findById(301L).get();
//		log.info("게시물 제목 : {}",resultBoard.getBoardTitle());
//		log.info("게시물 내용 : {}",resultBoard.getBoardContent());
//		log.info("작성일시 : {}",resultBoard.getBoardUpdatedDate());
//		log.info("작성자 이름 : {}",resultBoard.getUserNickname());
//		// 출력할때는 User객체니까 User 객체의 값을 다시 불러오기
//		log.info("작성자 아이디 : {}",resultBoard.getUserId().getUserId());
		
		///// 게시물을 삭제하는 경우
//		boardRepo.deleteById(301L);
//		log.info("삭제 결과 : {}", 
//				(boardRepo.findById(301L).isEmpty())? 
//						"삭제 성공":"삭제 실패");
		

//		log.info(boardRepo.findById(301L).get().toString());
		
		// 산책로별 게시판 리스트 출력
		
//		Course course = new Course();
//		course.setCourseId(91L);
//		log.info("course 객체 생성 끝");
//		for(Board board : boardRepo.findBoardByCourse(course)) {
//			log.info("게시물 제목 : {}",board.getBoardTitle());
//			log.info("게시물 내용 : {}",board.getBoardContent());
//			log.info("작성일시 : {}",board.getBoardUpdatedDate());
//			log.info("작성자 이름 : {}",board.getUserNickname());
//			// 출력할때는 User객체니까 User 객체의 값을 다시 불러오기
//			log.info("작성자 아이디 : {}",board.getUserId().getUserId());
//		}
//		
//		for(Long boardId : boardRepo.findBoardByCourse(course)) {
//			log.info("게시물 번호 : {}", boardId );
//		}
		
		//
		
//		for(BoardListResponse board : boardRepo.findBoardAndCnt()) {
//			log.info("게시물 번호 : {}",board.getBoardId());
//			log.info("게시물 제목 : {}",board.getBoardTitle());
//			log.info("작성일시 : {}",board.getBoardUpdatedDate());
//			log.info("작성자 이름 : {}",board.getUserNickname());
//			log.info("조회수 : {}",board.getBoardViewCount());
//			log.info("댓글수 : {}",board.getCommentCnt());
//			log.info("좋아요수 : {}",board.getLikeCnt());
//		}
		
//		Board boardId = new Board();
//		boardId.setBoardId(100L);
//		for(Course board : boardCourseRepo.findByBoardId(boardId)) {
//			log.info(board.toString());
//		}
		
//		Long boardId = 100L;
//		for(String userId : boardLikeRepo.findUserIdByBoardId(boardId)) {
//			log.info("{}번 게시물에 좋아요를 누른 회원 : {}", boardId, userId);
//		}
//		log.info("{}번 게시물에 좋아요를 누른 회원 수 : {}", boardId, boardLikeRepo.countUserIdByBoardId(boardId));
//		
//		String userId = "wayid50";
//		for(Long num : boardLikeRepo.findLikedBoardIdByUserId(userId)) {
//			log.info("회원 {}이 좋아요를 누른 게시물 : {}", userId, num);
//		}
//		log.info("회원 {}이 좋아요를 누른 게시물 수 : {}", userId, boardLikeRepo.countLikedBoardIdByUserId(userId));
//		
//		User user = new User();
//		user.setUserId(userId);
//		Board board = new Board();
//		board.setBoardId(171L);
//		
//		log.info("회원 {}이 {}번 게시물에 좋아요를 눌렀나요? : {}"
//				, userId, board.getBoardId(), 
//				boardLikeRepo.findByUserIdAndBoardId(
//						user, board).isPresent()?
//								"네" : "아니요");
//		
		
	}
}
