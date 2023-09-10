package com.walk.aroundyou.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domain.UploadImage;
import com.walk.aroundyou.dto.UploadImageRequestDTO;
import com.walk.aroundyou.repository.UploadImageRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadImageService {
	
	private final UploadImageRepository uploadImageRepository;
	
	/**
	 * 서버에 upload된 파일을 저장할 경로 -> static 폴더 하위에 upload-images 이름의 폴더 필요!
	 * - System.getProperty("user.dir") 
	 *   : Java 프로그램에서 현재 프로그램이 실행되는 작업 디렉토리를 나타냄.
	 *     프로그램이 파일을 읽거나 쓸 때 파일 경로를 상대 경로로 지정할 때 중요한 역할 
	 */
	private final String rootPath = System.getProperty("user.dir");
	private final String fileDir = rootPath + "/src/main/resources/static/upload-images/";
	
	/**
	 * 파일 경로 얻기; 
	 * upload-images 폴더 하위에, uuid로 서버에 저장된 이름이 매개변수로 들어가서 최종 경로 나타냄.
	 */
	public String getFullPath(String filename) {
		return fileDir + filename;
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
	public List<UploadImage> saveBoardImages(List<UploadImageRequestDTO> imageDTOs, Board board) 
			throws IllegalStateException, IOException {
		
		List<UploadImage> savedImages = new ArrayList<>();
		
		for(int i = 0; i < imageDTOs.size(); i++) {
			UploadImageRequestDTO imageDTO = imageDTOs.get(i);
			
			// 블로그로 참고한 코드에는 매개변수로 MultipartFile multipartFile을 넘기고
			// if 문으로 내용이 시작되었다. DTO 클래스에 MultipartFile을 적어주는 게 어떤 차이를 낳을까?
			MultipartFile multipartFile = imageDTO.getMultipartFile();
			
			if (multipartFile.isEmpty()) {
				continue;     // null;    파일이 비어있으면 건너뛰기
			}
			
			// 원본 파일명; 클라이언트가 업로드한 파일의 원래 파일 이름 반환
			String originalFileName = multipartFile.getOriginalFilename();
			
			// 원본 파일명 -> 서버에 저장된 파일명 (중복 X)
			// 파일명이 중복되지 않도록 UUID로 설정 + 확장자 유지
			String savedFileName = "Course" + UUID.randomUUID() + "." + extractExt(originalFileName);
			
			// 파일 저장 ; multipartFile에 저장된 파일을 서버에 지정된 경로로 저장
			multipartFile.transferTo(new File(getFullPath(savedFileName)));
			
			// 파일 순번 설정
			imageDTO.setImageOrd(i + 1); // i는 0부터 시작하므로 +1을 해서 1부터 시작
			
			// 엔티티로 변환하여 저장
			UploadImage savedImage = uploadImageRepository.save(imageDTO.toEntity());
			
			// Board와 연관관계 설정 -> toEntity할 때 다 되는 것 아닌가?
			//savedImage.setBoard(board);
			
			// 리스트에 저장
			savedImages.add(savedImage);
			
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
	public UploadImage saveUserImage(UploadImageRequestDTO uploadImageRequestDTO, Course course) 
			throws IllegalStateException, IOException {
		
		// 블로그로 참고한 코드에는 매개변수로 MultipartFile multipartFile을 넘기고
		// if 문으로 내용이 시작되었다. DTO 클래스에 MultipartFile을 적어주는 게 어떤 차이를 낳을까?
		MultipartFile multipartFile = uploadImageRequestDTO.getMultipartFile();
		
		if (multipartFile.isEmpty()) {
			return null;
		}
		
		// 원본 파일명; 클라이언트가 업로드한 파일의 원래 파일 이름 반환
		String originalFileName = multipartFile.getOriginalFilename();
		
		// 원본 파일명 -> 서버에 저장된 파일명 (중복 X)
		// 파일명이 중복되지 않도록 UUID로 설정 + 확장자 유지
		String savedFileName = "Course" + UUID.randomUUID() + "." + extractExt(originalFileName);
		
		// 파일 저장 ; multipartFile에 저장된 파일을 서버에 지정된 경로로 저장
		multipartFile.transferTo(new File(getFullPath(savedFileName)));
		
		return uploadImageRepository.save(uploadImageRequestDTO.toEntity());
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
	public UploadImage saveUserImage(UploadImageRequestDTO uploadImageRequestDTO, Member user) 
			throws IllegalStateException, IOException {
		
		// 블로그로 참고한 코드에는 매개변수로 MultipartFile multipartFile을 넘기고
		// if 문으로 내용이 시작되었다. DTO 클래스에 MultipartFile을 적어주는 게 어떤 차이를 낳을까?
		MultipartFile multipartFile = uploadImageRequestDTO.getMultipartFile();
		
		if (multipartFile.isEmpty()) {
			return null;
		}
		
		// 원본 파일명; 클라이언트가 업로드한 파일의 원래 파일 이름 반환
		String originalFileName = multipartFile.getOriginalFilename();
		
		// 원본 파일명 -> 서버에 저장된 파일명 (중복 X)
		// 파일명이 중복되지 않도록 UUID로 설정 + 확장자 유지
		String savedFileName = "User" + UUID.randomUUID() + "." + extractExt(originalFileName);
		
		// 파일 저장 ; multipartFile에 저장된 파일을 서버에 지정된 경로로 저장
		multipartFile.transferTo(new File(getFullPath(savedFileName)));
		
		return uploadImageRepository.save(uploadImageRequestDTO.toEntity());
	}
	
	/**
	 * 이미지 삭제 기능
	 * 1. 데이터베이스에서 해당 이미지 정보 삭제
	 * 2. 서버에 저장된 실제 이미지 파일 삭제; 파일이 없는 경우 아무 작업도 수행하지 않음.
	 */
	@Transactional
	public void deleteImage(UploadImage uploadImage) throws IOException {
		uploadImageRepository.delete(uploadImage);
		Files.deleteIfExists(Paths.get(getFullPath(uploadImage.getSavedFileName())));
	}
	
	/**
	 * 확장자 추출 메소드
	 */
	private String extractExt(String originalFileName) {
		int pos = originalFileName.lastIndexOf(".");
		return originalFileName.substring(pos + 1);
	}
	
	/**
	 * 파일 다운로드 기능은 굳이 필요하지 않을 것 같아서 구현하지 않았음.
	 * 필요하다면 여기서 작성하기
	 */

}
