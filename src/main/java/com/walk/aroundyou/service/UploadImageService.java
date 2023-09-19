package com.walk.aroundyou.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domain.UploadImage;
import com.walk.aroundyou.repository.UploadImageRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadImageService {

	private final UploadImageRepository uploadImageRepository;

	/**
	 * 서버에 upload된 파일을 저장할 경로 
	 * -> static 폴더 하위에 upload-images 이름의 폴더 필요! 
	 * - System.getProperty("user.dir") 
	 *   : Java 프로그램에서 현재 프로그램이 실행되는 작업 디렉토리를 나타냄.
	 *   프로그램이 파일을 읽거나 쓸 때 파일 경로를 상대 경로로 지정할 때 중요한 역할
	 */
	private final String rootPath = System.getProperty("user.dir");
	private final String fileDir = rootPath + "/src/main/resources/static/upload-images/";

	/**
	 * 파일 경로 얻기(만들기); 
	 * upload-images 폴더 하위에, uuid로 서버에 저장된 이름이 매개변수로 들어가서 최종 경로 나타냄.
	 */
	public String getBoardFullPath(String filename) {
		return fileDir + "board/" + filename;
	}

	public String getCourseFullPath(String filename) {
		return fileDir + "course/" + filename;
	}

	public String getUserFullPath(String filename) {
		return fileDir + "user/" + filename;
	}

	/**
	 * UploadImageId로 지정 경로의 파일 찾기
	 */
	public String findCourseFullPathById(Long UploadImageId) {
		Optional<UploadImage> uploadImage = uploadImageRepository.findById(UploadImageId);
		if (uploadImage.isPresent()) {
			String savedFileName = uploadImage.get().getSavedFileName();
			return "/upload-images/course/" + savedFileName;
		} else {
			return null; // null 외에 더 좋은 처리 방법이 있을까?
		}
	}
	
	public List<String> findBoardFullPathsById(List<UploadImage> uploadImages) {
		if (uploadImages != null && !uploadImages.isEmpty()) {
			List<String> imagesPaths = new ArrayList<>();
			for (UploadImage uploadImage : uploadImages) {
				String savedFileName = uploadImage.getSavedFileName();
				imagesPaths.add("/upload-images/board/" + savedFileName);
			}
			return imagesPaths;
		} else {
			return null; // null 외에 더 좋은 처리 방법이 있을까?
		}
	}
	
	public String findBoardFullPathById(Long UploadImageId) {
		Optional<UploadImage> uploadImage = uploadImageRepository.findById(UploadImageId);
		if (uploadImage.isPresent()) {
			String savedFileName = uploadImage.get().getSavedFileName();
			return "/upload-images/board/" + savedFileName;
		} else {
			return null; // null 외에 더 좋은 처리 방법이 있을까?
		}
	}
	
	public String findUserFullPathById(Long UploadImageId) {
		Optional<UploadImage> uploadImage = uploadImageRepository.findById(UploadImageId);
		if (uploadImage.isPresent()) {
			String savedFileName = uploadImage.get().getSavedFileName();
			return "/upload-images/user/" + savedFileName;
		} else {
			return null; // null 외에 더 좋은 처리 방법이 있을까?
		}
	}

	///// 이미지 저장할 때 UUID 앞에 Board, Course, User라 붙여서 서로 식별되도록 했다.
	/**
	 * 게시물 이미지 저장 -> 여러 개의 사진 동시 저장 가능! DTO롤 LIST로 묶어서 동시 처리 및 순번 부여해줬다.
	 * : HTTP 요청으로 전송된 파일(multipartFile)을 서버에 저장하고 디비에 파일 정보를 저장하는 역할
	 * - MultipartFile 
	 *   : Spring에서 제공하는 클래스. HTTP 요청으로부터 전송된 파일의 정보와 내용 담고 있는 객체.
	 *   > getOriginalFilename() : 클라이언트가 업로드한 파일의 원래 파일 이름 반환
	 *   > transferTo() : 업로드된 파일 내용을 지정된 파일로 복사 또는 이동할 때 사용. 
	 *   		클라이언트가 업로드한 파일을 서버의 특정 경로에 안전하게 저장.
	 *          File 객체를 매개변수로 받아서 업로드된 파일 내용을 
	 *          해당 File 객체가 나타내는 파일로 복사하거나 이동시킴.
	 *          File 객체에는 파일의 경로와 파일명이 포함되어 있어야 한다!
	 *          복사 또는 이동 작업 중에 발생 가능한 IOException 처리해야!
	 */
	public List<UploadImage> saveBoardImages(List<MultipartFile> multipartFiles, Board board)
			throws IllegalStateException, IOException {

		List<UploadImage> savedImages = new ArrayList<>();
		int imageOrd = 1;   // 이미지 순번 초기값
		
		for (MultipartFile multipartFile : multipartFiles) {
			
			if (multipartFile.isEmpty()) {
				throw new IllegalArgumentException("업로드된 파일이 비어 있습니다.");
				// continue;   // 파일이 비어있으면 건너뛰기
			}
			
			// 원본 파일명; 클라이언트가 업로드한 파일의 원래 파일 이름 반환
			String originalFileName = multipartFile.getOriginalFilename();
			log.info("upload file : {}", originalFileName);
			
			// 원본 파일명 -> 서버에 저장된 파일명 (중복 X)
			// 파일명이 중복되지 않도록 UUID로 설정 + 확장자 유지
			String savedFileName = "Board" + UUID.randomUUID() + "." + extractExt(originalFileName);
			
			// 파일 저장 ; multipartFile에 저장된 파일을 서버에 지정된 경로로 저장
			multipartFile.transferTo(new File(getBoardFullPath(savedFileName)));
			
			// UploadImage 엔티티 생성 - 파일 순번 설정
			UploadImage uploadImage = UploadImage.builder()
					.originalFileName(originalFileName)
					.savedFileName(savedFileName)
					.imageOrd(imageOrd++)   // 순번 증가
					.board(board)
					.build();
			
			// 엔티티 저장
			savedImages.add(uploadImageRepository.save(uploadImage));
		}

		return savedImages;
	}

	/**
	 * 산책로 메인 이미지 저장 
	 * : HTTP 요청으로 전송된 파일(multipartFile)을 서버에 저장하고 디비에 파일 정보를 저장하는 역할
	 * - MultipartFile 
	 *   : Spring에서 제공하는 클래스. HTTP 요청으로부터 전송된 파일의 정보와 내용 담고 있는 객체.
	 *   > getOriginalFilename() : 클라이언트가 업로드한 파일의 원래 파일 이름 반환
	 *   > transferTo() : 업로드된 파일 내용을 지정된 파일로 복사 또는 이동할 때 사용. 
	 *   		클라이언트가 업로드한 파일을 서버의 특정 경로에 안전하게 저장.
	 *          File 객체를 매개변수로 받아서 업로드된 파일 내용을 
	 *          해당 File 객체가 나타내는 파일로 복사하거나 이동시킴.
	 *          File 객체에는 파일의 경로와 파일명이 포함되어 있어야 한다!
	 *          복사 또는 이동 작업 중에 발생 가능한 IOException 처리해야!
	 */
	public UploadImage saveCourseImage(MultipartFile multipartFile, Course course)
			throws IllegalStateException, IOException {

		if (multipartFile.isEmpty()) {
			throw new IllegalArgumentException("업로드된 파일이 비어 있습니다. 다른 파일을 올려주세요.");
		}

		// 원본 파일명; 클라이언트가 업로드한 파일의 원래 파일 이름 반환
		String originalFileName = multipartFile.getOriginalFilename();
		log.info("upload file : {}", originalFileName);

		// 원본 파일명 -> 서버에 저장된 파일명 (중복 X)
		// 파일명이 중복되지 않도록 UUID로 설정 + 확장자 유지
		String savedFileName = "Course" + UUID.randomUUID() + "." + extractExt(originalFileName);

		// 파일 저장 ; multipartFile에 저장된 파일을 서버에 지정된 경로로 저장
		multipartFile.transferTo(new File(getCourseFullPath(savedFileName)));

		// UploadImage 엔티티 생성 -> 여기에 setCourse(course) 추가 필요함
		UploadImage uploadImage = UploadImage.builder()
									.originalFileName(originalFileName)
									.savedFileName(savedFileName)
									.course(course)
									.build();

		// 엔티티 저장
		return uploadImageRepository.save(uploadImage);
	}

	/**
	 * 유저 프로필 이미지 저장
	 * : HTTP 요청으로 전송된 파일(multipartFile)을 서버에 저장하고 디비에 파일 정보를 저장하는 역할
	 * - MultipartFile 
	 *   : Spring에서 제공하는 클래스. HTTP 요청으로부터 전송된 파일의 정보와 내용 담고 있는 객체.
	 *   > getOriginalFilename() : 클라이언트가 업로드한 파일의 원래 파일 이름 반환
	 *   > transferTo() : 업로드된 파일 내용을 지정된 파일로 복사 또는 이동할 때 사용. 
	 *   		클라이언트가 업로드한 파일을 서버의 특정 경로에 안전하게 저장.
	 *          File 객체를 매개변수로 받아서 업로드된 파일 내용을 
	 *          해당 File 객체가 나타내는 파일로 복사하거나 이동시킴.
	 *          File 객체에는 파일의 경로와 파일명이 포함되어 있어야 한다!
	 *          복사 또는 이동 작업 중에 발생 가능한 IOException 처리해야!
	 */
	public UploadImage saveUserImage(MultipartFile multipartFile, Member user)
			throws IllegalStateException, IOException {

		if (multipartFile.isEmpty()) {
			throw new IllegalArgumentException("업로드된 파일이 비어 있습니다. 다른 파일을 올려주세요.");
		}

		// 원본 파일명; 클라이언트가 업로드한 파일의 원래 파일 이름 반환
		String originalFileName = multipartFile.getOriginalFilename();
		log.info("upload file : {}", originalFileName);

		// 원본 파일명 -> 서버에 저장된 파일명 (중복 X)
		// 파일명이 중복되지 않도록 UUID로 설정 + 확장자 유지
		String savedFileName = "User" + UUID.randomUUID() + "." + extractExt(originalFileName);

		// 파일 저장 ; multipartFile에 저장된 파일을 서버에 지정된 경로로 저장
		multipartFile.transferTo(new File(getUserFullPath(savedFileName)));
		
		// UploadImage 엔티티 생성
		UploadImage uploadImage = UploadImage.builder()
				.originalFileName(originalFileName)
				.savedFileName(savedFileName)
				.user(user)
				.build();
		
		// 엔티티 저장
		return uploadImageRepository.save(uploadImage);
	}

	////// Course, Board, User로 이미지 찾기 -> 컨트롤러에서 null 처리 반드시 해야 함!
	/**
	 * Course로 이미지 찾기
	 */
	public UploadImage findByCourse(Course course) {
		return uploadImageRepository.findByCourse(course);
	}

	/**
	 * Board로 이미지 찾기
	 */
	public List<UploadImage> findByBoard(Board board) {
		return uploadImageRepository.findByBoard(board);
	}

	/**
	 * User로 이미지 찾기
	 */
	public UploadImage findByUser(Member user) {
		return uploadImageRepository.findByUser(user);
	}

	/**
	 * 이미지 삭제 기능 
	 * 1. 데이터베이스에서 해당 이미지 정보 삭제 
	 * 2. 서버에 저장된 실제 이미지 파일 삭제; 파일이 없는 경우 아무 작업도 수행하지 않음. 
	 * - Files.exists() : 파일 존재 여부 확인 
	 * - Files.deleteIfExists() : 주어진 경로에서 파일 삭제. 
	 * 		> 파일이 존재하면 해당 파일 삭제하고, 파일이 없는 경우 아무 작업 없이 true 반환
	 * 		> 파일 삭제에 실패하면 false 반환 
	 * - Paths.get() : 파일 경로 생성.
	 */
	 @Transactional 
	 public void deleteImage(UploadImage uploadImage) throws IOException {
	  
		 boolean fileExists = 
				 Files.exists(Paths.get(
						 getCourseFullPath(uploadImage.getSavedFileName())));
	 
		 // 파일이 존재하는 경우에만 삭제 시도 
		 if(fileExists) { 
			 log.info("파일이 존재한다!"); 
		 } 
		 log.info("파일 삭제 직전: " + getCourseFullPath(uploadImage.getSavedFileName()));
		 try {
			// 게시판, 산책로, 유저 파일 경로 중에 하나에서 찾아서 파일을 삭제하라!
			 Files.deleteIfExists(
					 Paths.get(getBoardFullPath(uploadImage.getSavedFileName())));
			 Files.deleteIfExists(
					 Paths.get(getCourseFullPath(uploadImage.getSavedFileName())));
			 Files.deleteIfExists(
					 Paths.get(getUserFullPath(uploadImage.getSavedFileName()))); 
			 log.info("파일 삭제 성공");
		 } catch (IOException e) {
			 log.error("파일 삭제 오류 : " + e.getMessage());
		 }
		 log.info("삭제 시도 후 파일 존재 여부 확인 : " + 
				 Files.exists(Paths.get(
						 getCourseFullPath(uploadImage.getSavedFileName())))); 
		 
		 uploadImageRepository.delete(uploadImage);
		 log.info("UploadImage 테이블에서 이미지 정보가 삭제되었다.");
	}

	/**
	 * 확장자 추출 메소드
	 */
	private String extractExt(String originalFileName) {
		int pos = originalFileName.lastIndexOf(".");
		return originalFileName.substring(pos + 1);
	}

	public List<UploadImage> findMyImageByUserId(String userId) {
		// TODO Auto-generated method stub
		return uploadImageRepository.findMyImageByUserId(userId);
	}

	/**
	 * 파일 다운로드 기능은 굳이 필요하지 않을 것 같아서 구현하지 않았음. 필요하다면 여기서 작성하기
	 */

}