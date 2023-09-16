package com.walk.aroundyou.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.UploadImage;
import com.walk.aroundyou.dto.CourseRequestDTO;
import com.walk.aroundyou.dto.CourseResponseDTO;
import com.walk.aroundyou.service.CourseService;
import com.walk.aroundyou.service.UploadImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class CourseApiController {
	
	private final CourseService courseService;
	private final UploadImageService uploadImageService;
	
	// id로 산책로 하나로 조회
	@GetMapping("/api/courses/{id}")
	public ResponseEntity<Course> findCourse(@PathVariable long id){
		Course course = courseService.findById(id);
		
		return ResponseEntity.ok()
				.body(course);
	}

	/**
	 * [산책로 상세 조회페이지] 좋아요, 언급, 댓글 수 포함 산책로 상세 정보 조회
	 */
	@GetMapping("/api/courses/detail/{id}")
	public ResponseEntity<CourseResponseDTO> findDetailCourse(@PathVariable Long id){
		log.info("courses/detail/{id} 들어감");
		CourseResponseDTO courseResponseDTO = courseService.findByIdWithCounts(id);
		return ResponseEntity.ok().body(courseResponseDTO);
	}
	
	/**
	 * [산책로 목록 조회페이지] 검색 조건에 따른 전체 목록 조회
	 * 요청에 대해 page 정보까지 반환받으려면 ResponseEntity<List<CourseResponseDTO>>이 아닌,
	 * ResponseEntity<Object> 으로 바꾸자.
	 * 일단 반환을 object로 하면 dto를 어떻게 매핑해야 할지 모르겠어서 그대로 냅뒀다.
	 */
	@GetMapping("/api/courses/search")
	public ResponseEntity<Page<CourseResponseDTO>> findAllCourses(
			// @RequestParam : 요청객체로부터 요청파라미터 자동추출
			@RequestParam(required = false) String region,
		    @RequestParam(required = false) String level,
		    @RequestParam(required = false) String distance,
		    @RequestParam(required = false) String startTime,
		    @RequestParam(required = false) String endTime,
		    @RequestParam(required = false) String searchTargetAttr,
		    @RequestParam(required = false) String searchKeyword,
		    @RequestParam(required = false) String sort,
		    @RequestParam(required = false, defaultValue = "0") Integer page
			){
			
		Page<CourseResponseDTO> coursePage = 
				courseService.findAllByCondition(
				region, level, distance, startTime, endTime,
				searchTargetAttr, searchKeyword, sort, page);
		
		return ResponseEntity.ok()
				.body(coursePage);
	}

	/**
	 * [관리자페이지] 산책로 생성 요청 ; 이미지 업로드 처리
	 * - @RequestPart : Multipart/form-data 에 특화된 어노테이션
	 * - 원래 매개변수인 CourseRequestDTO request에 @RequestBody 어노테이션을 쓰고 
	 *   headers에 "Content-Type": "application/json"을 적어줬는데,
	 *   파일 데이터와 함께 formData 객체를 활용해 보내기 위해서 @RequestPart 사용.
	 *   자바스크립트에서 formData.append('courseData', JSON 데이터)라고 적어 값을 받아온다.
	 */
	@PostMapping("/api/admin/courses")
	public ResponseEntity<Course> addCourse(
			@RequestPart(value = "dto") CourseRequestDTO request,
			@RequestPart(value = "file", required=false) MultipartFile file)
					throws IllegalStateException, IOException{
		
		Course savedCourse = courseService.save(request);
		
		// savedCourse 객체에서 id 가져오기
		Long courseId = savedCourse.getCourseId();
		log.info("courseId : " + courseId);
		
		if (file != null) {
			UploadImage uploadImage = 
				uploadImageService.saveCourseImage(file, savedCourse);
			//savedCourse.setCourseImageId(uploadImage);
		}
		return ResponseEntity.status(HttpStatus.CREATED)
				// id를 응답헤더에 추가해서 최종 생성페이지 확인하도록 하기
				.header("courseId", courseId.toString()) 
				.body(savedCourse);
	}
	
	/**
	 * [관리자페이지] 산책로 수정 요청
	 */
	@PatchMapping("/api/admin/courses/{id}")
	public ResponseEntity<Course> updateCourse(
			@PathVariable long id, 
			@RequestPart(value = "dto") CourseRequestDTO request,
			@RequestPart(value = "file", required=false) MultipartFile file,
			@RequestParam("ifNewImageExists") int ifNewImageExists) 
					throws IllegalStateException, IOException{
		
		Course existedCourse = courseService.findById(id);
		UploadImage existedImage = uploadImageService.findByCourse(existedCourse);
		
		// 수정페이지에서 최종 업로드 취소 상태로 수정 요청했을 시
		if(ifNewImageExists == 0) {
			if (existedImage != null) {
				uploadImageService.deleteImage(existedImage);
			}
		}
		
		// 이미지를 수정할 때는 우선 기존 이미지를 삭제하고 다시 저장하는 순서를 겪는다.
		// 먼저 이미지 파일이 새로 업로드 되었는지 확인
		// ↳ 만약 이미지 파일이 새로 업로드 되지 않았다면 이미지 관련 로직을 건너뛰고 나머지 산책로 데이터만 업데이트
		
		Course updatedCourse = courseService.updateCourse(id, request);
		// (1) 이미지 수정됨
		if (file != null && !file.isEmpty()) {
			log.info("(1) 이미지가 새로 업로드되었어요.");
			if(existedImage != null) {
				log.info("(1-1) 기존 이미지가 수정되었을 경우 삭제부터 합니다.");
				log.info("existedImage의 원래 이름 : " + existedImage.getOriginalFileName());
				uploadImageService.deleteImage(existedImage);
			} else {
				log.info("(1-2) 원래 이미지가 없을 경우 삭제 없이 업로드됩니다.");
			}
			// 이미지 업로드 로직
			UploadImage uploadImage = 
					uploadImageService.saveCourseImage(file, updatedCourse);
			log.info("uploadImage의 original 이름 : " + uploadImage.getOriginalFileName());
			
		} else {  // (2) 이미지 수정 안 됨
			log.info("(2) 이미지의 수정이 없는 경우입니다.");
			if(existedImage != null) {
				log.info("(2-1) 기존 이미지를 유지합니다.");
			} else {
				// (2 - 2) 기존 이미지가 없을 경우 : 그냥 dto만 업데이트!
			}
		}
		return ResponseEntity.ok()
				.body(updatedCourse);
	}
	
	/**
	 * [관리자페이지] 산책로 삭제 요청
	 */
	@DeleteMapping("/api/admin/courses/{id}")
	public ResponseEntity<Course> deleteCourse(@PathVariable long id) throws IOException{
		
		// 이미지 삭제 (이미지 정보는 자연히 삭제되지만 서버 파일 삭제를 위해) 
		Course course = courseService.findById(id);
		UploadImage existedImage = uploadImageService.findByCourse(course);
		if (existedImage != null) {
			uploadImageService.deleteImage(existedImage);
		}
		
		courseService.deleteCourse(id);
		
		return ResponseEntity.ok()
				.build();
	}
	
	/**
	 * 이미지 업로드 요청 처리 : saveImage() 메소드에 외래키 매핑하지 않았을 경우 작동
	 * -> 외래키가 붙지 않고, 지정되어 있는 서버 경로로 파일만 저장됨.
	 * -> 산책로 생성, 수정, 삭제 요청에 이미지 업로드 처리를 같이 넣었으므로 사용할 필요 없음.
	 */
/*	@PostMapping("/api/uploadCourseImage")
	public ResponseEntity<?> uploadImage(
			// Multipart/form-data 에 특화된 어노테이션
			@RequestPart(value = "file", required=false) 
			MultipartFile file
			) throws IllegalStateException, IOException  {
		if (file != null) {
			UploadImage uploadImage = 
					uploadImageService.saveCourseImage(file);
			
			return ResponseEntity.status(HttpStatus.OK)
					.body(uploadImage);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("No file provided");
		}
	}
*/		
	// 지역에 따른 산책로이름(산책로 큰분류) 가져오기
	@GetMapping("/api/courses/flagname")
	public ResponseEntity<List<String>> getWlkCoursFlagNm(String signguCn){
		log.info("getWlkCoursFlagNm() 컨트롤러 접근");
		return ResponseEntity.ok().body(courseService.findFlagNameBySignguCn(signguCn));
	}
	
	// 산책로이름에 따른 코스이름(산책로 작은분류) 정보 가져오기
	@GetMapping("/api/courses/coursename")
	public ResponseEntity<List<Course>> getWlkCoursNm(String wlkCoursFlagNm){
		log.info("getWlkCoursNm() 컨트롤러 접근");
		return ResponseEntity.ok().body(courseService.findCourseNameByWlkCoursFlagNm(wlkCoursFlagNm));
	}
}